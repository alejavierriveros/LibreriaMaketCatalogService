package com.example.libreriaMarketCatalogService.controllerV2;

import com.example.libreriaMarketCatalogService.assemblers.ProveedorModelAssembler;
import com.example.libreriaMarketCatalogService.controller.ProveedorControllerV2;
import com.example.libreriaMarketCatalogService.dto.ProveedorDTO;
import com.example.libreriaMarketCatalogService.exceptions.BadRequestException;
import com.example.libreriaMarketCatalogService.exceptions.RecursoNoEncontradoException;
import com.example.libreriaMarketCatalogService.service.ProveedorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@WebMvcTest(controllers = ProveedorControllerV2.class, excludeAutoConfiguration = ServletWebSecurityAutoConfiguration.class)
@Import(ProveedorModelAssembler.class)
public class ProveedorControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProveedorService proveedorService;

    private ProveedorDTO.Response proveedorResponseUno;
    private ProveedorDTO.Response proveedorResponseDos;
    private ProveedorDTO.Request proveedorRequest;

    private final RecursoNoEncontradoException recursoNoEncontradoExceptionProveedor = new RecursoNoEncontradoException("Proveedor no encontrado");
    private final BadRequestException badRequestExceptionProveedorYaExiste = new BadRequestException("Ya existe un proveedor con ese nombre");
    private final BadRequestException badRequestExceptionProveedorConProductos = new BadRequestException("No se puede eliminar proveedor con productos asociados");

    Long id;

    @BeforeEach
    void setUp(){
        id = 1L;
        proveedorRequest = new ProveedorDTO.Request("Distribuidora Libros", "libros@distro.com", List.of());

        proveedorResponseUno = new ProveedorDTO.Response(1L, "Distribuidora Libros", "libros@distro.com");
        proveedorResponseDos = new ProveedorDTO.Response(2L, "Proveedor Test", "libros@distro.com");
    }

    @Test
    void listAllTest() throws Exception{
        when(proveedorService.listar()).thenReturn(List.of(proveedorResponseUno, proveedorResponseDos));

        mockMvc.perform(get("/api/v2/proveedores").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.responseList.length()").value(2))
                .andExpect(jsonPath("$._embedded.responseList[0].nombre").value("Distribuidora Libros"))
                .andExpect(jsonPath("$._embedded.responseList[1].nombre").value("Proveedor Test"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(proveedorService).listar();
    }

    @Test
    void findAllEmptyTest() throws Exception{
        when(proveedorService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/v2/proveedores").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNoContent());

        verify(proveedorService).listar();
    }

    @Test
    void findByIdTest() throws Exception{
        when(proveedorService.obtenerPorId(id)).thenReturn(proveedorResponseUno);

        mockMvc.perform(get("/api/v2/proveedores/{id}", id).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Distribuidora Libros"))
                .andExpect(jsonPath("$._links.self.href").value(org.hamcrest.Matchers.containsString("/api/v2/proveedores/1")))
                .andExpect(jsonPath("$._links.proveedores.href").exists());

        verify(proveedorService).obtenerPorId(id);
    }

    @Test
    void findByIdNotFound() throws Exception{
        when(proveedorService.obtenerPorId(id)).thenThrow(recursoNoEncontradoExceptionProveedor);

        mockMvc.perform(get("/api/v2/proveedores/{id}", id).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProveedor.getMessage()));

        verify(proveedorService).obtenerPorId(id);
    }

    @Test
    void crearTest() throws Exception{
        when(proveedorService.guardar(any(ProveedorDTO.Request.class))).thenReturn(proveedorResponseUno);

        mockMvc.perform(post("/api/v2/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(proveedorRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value(proveedorRequest.getNombre()))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(header().exists("Location"));

        verify(proveedorService).guardar(any(ProveedorDTO.Request.class));
    }

    @Test
    void crearProveedorYaExisteTests() throws Exception{
        when(proveedorService.guardar(any(ProveedorDTO.Request.class))).thenThrow(badRequestExceptionProveedorYaExiste);

        mockMvc.perform(post("/api/v2/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(proveedorRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(badRequestExceptionProveedorYaExiste.getMessage()));

        verify(proveedorService).guardar(any(ProveedorDTO.Request.class));
    }

    @Test
    void actualizarTest() throws Exception{
        when(proveedorService.actualizar(any(Long.class), any(ProveedorDTO.Request.class))).thenReturn(proveedorResponseUno);

        mockMvc.perform(put("/api/v2/proveedores/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(proveedorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(proveedorService).actualizar(any(Long.class), any(ProveedorDTO.Request.class));
    }

    @Test
    void actualizarProveedorNoEncontradoTest() throws Exception{
        when(proveedorService.actualizar(any(Long.class), any(ProveedorDTO.Request.class))).thenThrow(recursoNoEncontradoExceptionProveedor);

        mockMvc.perform(put("/api/v2/proveedores/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(proveedorRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProveedor.getMessage()));

        verify(proveedorService).actualizar(any(Long.class), any(ProveedorDTO.Request.class));
    }

    @Test
    void eliminarTest() throws Exception{
        when(proveedorService.eliminar(id)).thenReturn(true);

        mockMvc.perform(delete("/api/v2/proveedores/{id}", id).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNoContent());

        verify(proveedorService).eliminar(id);
    }

    @Test
    void eliminarNoEncontradoTest() throws Exception{
        when(proveedorService.eliminar(id)).thenThrow(recursoNoEncontradoExceptionProveedor);

        mockMvc.perform(delete("/api/v2/proveedores/{id}", id).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProveedor.getMessage()));

        verify(proveedorService).eliminar(id);
    }

    @Test
    void eliminarConProductosAsociadosTest() throws Exception{
        when(proveedorService.eliminar(id)).thenThrow(badRequestExceptionProveedorConProductos);

        mockMvc.perform(delete("/api/v2/proveedores/{id}", id).accept(MediaTypes.HAL_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(badRequestExceptionProveedorConProductos.getMessage()));

        verify(proveedorService).eliminar(id);
    }
}