package com.example.libreriaMarketCatalogService.controller;

import com.example.libreriaMarketCatalogService.dto.ProveedorDTO;
import com.example.libreriaMarketCatalogService.exceptions.BadRequestException;
import com.example.libreriaMarketCatalogService.exceptions.RecursoNoEncontradoException;
import com.example.libreriaMarketCatalogService.service.ProveedorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProveedorController.class, excludeAutoConfiguration = ServletWebSecurityAutoConfiguration.class)
public class ProveedorControllerTest {
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
    Long id; //id ejemplo

    @BeforeEach
    void setUp(){
        id = 1L;
        //para actualizar tiene los mismos datos por conveniencia
        proveedorRequest = new ProveedorDTO.Request ("Distribuidora Libros", "libros@distro.com", List.of());

        proveedorResponseUno = new ProveedorDTO.Response(1L, "Distribuidora Libros", "libros@distro.com");
        proveedorResponseDos = new ProveedorDTO.Response(2L, "Proveedor Test", "libros@distro.com");
    }

    @Test
    void listAllTest() throws Exception{
        when(proveedorService.listar()).thenReturn(List.of(proveedorResponseUno, proveedorResponseDos));
        mockMvc.perform(get("/api/v1/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Distribuidora Libros"))
                .andExpect(jsonPath("$[1].nombre").value("Proveedor Test"));
        verify(proveedorService).listar();
    }

    @Test
    void findAllEmptyTest() throws Exception{
        when(proveedorService.listar()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/proveedores"))
                .andExpect(status().isNoContent());
        verify(proveedorService).listar();
    }

    @Test
    void findByIdTest() throws Exception{
        when(proveedorService.obtenerPorId(id)).thenReturn(proveedorResponseUno);
        mockMvc.perform(get("/api/v1/proveedores/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Distribuidora Libros"));
        verify(proveedorService).obtenerPorId(id);
    }
    @Test
    void findByIdNotFound() throws Exception{
        when(proveedorService.obtenerPorId(id)).thenThrow(recursoNoEncontradoExceptionProveedor);
        mockMvc.perform(get("/api/v1/proveedores/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProveedor.getMessage()));
        verify(proveedorService).obtenerPorId(id);
    }

    @Test
    void crearTest() throws Exception{
        when(proveedorService.guardar(any(ProveedorDTO.Request.class))).thenReturn(proveedorResponseUno);
        mockMvc.perform(post("/api/v1/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proveedorRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value(proveedorRequest.getNombre()));
        verify(proveedorService).guardar(any(ProveedorDTO.Request.class));
    }

    @Test
    void crearProveedorYaExisteTests() throws Exception{
        when(proveedorService.guardar(any(ProveedorDTO.Request.class))).thenThrow(badRequestExceptionProveedorYaExiste);
        mockMvc.perform(post("/api/v1/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proveedorRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(badRequestExceptionProveedorYaExiste.getMessage()));
        verify(proveedorService).guardar(any(ProveedorDTO.Request.class));
    }

    @Test
    void actualizarTest() throws Exception{
        when(proveedorService.actualizar(any(Long.class), any(ProveedorDTO.Request.class))).thenReturn(proveedorResponseUno);
        mockMvc.perform(put("/api/v1/proveedores/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proveedorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
        verify(proveedorService).actualizar(any(Long.class), any(ProveedorDTO.Request.class));
    }

    @Test
    void actualizarProveedorNoEncontradoTest() throws Exception{
        when(proveedorService.actualizar(any(Long.class), any(ProveedorDTO.Request.class))).thenThrow(recursoNoEncontradoExceptionProveedor);
        mockMvc.perform(put("/api/v1/proveedores/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(proveedorRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProveedor.getMessage()));
        verify(proveedorService).actualizar(any(Long.class), any(ProveedorDTO.Request.class));
    }

    @Test
    void eliminarTest() throws Exception{
        when(proveedorService.eliminar(id)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/proveedores/{id}", id))
                .andExpect(status().isNoContent());
        verify(proveedorService).eliminar(id);
    }

    @Test
    void eliminarNoEncontradoTest() throws Exception{
        when(proveedorService.eliminar(id)).thenThrow(recursoNoEncontradoExceptionProveedor);
        mockMvc.perform(delete("/api/v1/proveedores/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProveedor.getMessage()));
        verify(proveedorService).eliminar(id);
    }

    @Test
    void eliminarConProductosAsociadosTest() throws Exception{
        when(proveedorService.eliminar(id)).thenThrow(badRequestExceptionProveedorConProductos);
        mockMvc.perform(delete("/api/v1/proveedores/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(badRequestExceptionProveedorConProductos.getMessage()));
        verify(proveedorService).eliminar(id);
    }
}
