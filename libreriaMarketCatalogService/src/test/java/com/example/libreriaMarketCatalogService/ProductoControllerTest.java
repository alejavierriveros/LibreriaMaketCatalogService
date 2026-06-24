package com.example.libreriaMarketCatalogService;

import com.example.libreriaMarketCatalogService.controller.ProductoController;
import com.example.libreriaMarketCatalogService.dto.ProductoInputDTO;
import com.example.libreriaMarketCatalogService.dto.ProductoResponseDTO;
import com.example.libreriaMarketCatalogService.exceptions.BadRequestException;
import com.example.libreriaMarketCatalogService.exceptions.RecursoNoEncontradoException;
import com.example.libreriaMarketCatalogService.model.Producto;
import com.example.libreriaMarketCatalogService.service.ProductoService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductoController.class, excludeAutoConfiguration = ServletWebSecurityAutoConfiguration.class)
public class ProductoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoService productoService;

    @Autowired
    private ProductoController productoController;

    private ProductoInputDTO productoInputDTO;
    private ProductoResponseDTO productoResponseDTOuno;
    private ProductoResponseDTO productoResponseDTOdos;

    private final RecursoNoEncontradoException recursoNoEncontradoExceptionProducto = new RecursoNoEncontradoException("Producto no encontrado");
    private final RecursoNoEncontradoException recursoNoEncontradoExceptionProveedor = new RecursoNoEncontradoException("Proveedor no existe");
    private final BadRequestException badRequestExceptionProductoYaExiste = new BadRequestException("Ya existe un producto con ese ISBN");
    private final BadRequestException badRequestExceptionISBNRegistrado = new BadRequestException("ISBN ya registrado");
    Long id;
    @BeforeEach
    void setUp(){
        id = 3L;
        //datos distintos para simular update
        productoInputDTO = new ProductoInputDTO("Java for Dummies", "Terry A. Burd", "Wiley", "Programación",
                2015, 15000.0, "978-111-98-6164-5", "DESC",1L, "Distribuidora Libros");

        productoResponseDTOuno = new ProductoResponseDTO(3L,"Java for Dummies", "Terry A. Burd", "Wiley", "Programación",
                2022, 26725.0, "978-111-98-6164-5", "desc",1L, "Distribuidora Libros");

        productoResponseDTOdos = new ProductoResponseDTO(5L, "Clean Code", "Robert C. Martin", "Prentice Hall", "Programación",
                2008, 30000.0, "978-013-23-5088-4", "desc",2L, "Proveedor Test");
    }

    @Test
    void listAllTest() throws Exception{
        when(productoService.listar()).thenReturn(List.of(productoResponseDTOuno,productoResponseDTOdos));
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Java for Dummies"))
                .andExpect(jsonPath("$[1].titulo").value("Clean Code"));
        verify(productoService).listar();
    }

    @Test
    void findAllEmptyTest() throws Exception{
        when(productoService.listar()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isNoContent());
        verify(productoService).listar();
    }

    @Test
    void findByIdTest() throws Exception{
        when(productoService.obtenerPorId(id)).thenReturn(productoResponseDTOuno);
        mockMvc.perform(get("/api/v1/productos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.titulo").value("Java for Dummies"));
        verify(productoService).obtenerPorId(id);
    }

    @Test
    void findByIdNotFound() throws Exception{
        when(productoService.obtenerPorId(id)).thenThrow(recursoNoEncontradoExceptionProducto);
        mockMvc.perform(get("/api/v1/productos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProducto.getMessage()));
        verify(productoService).obtenerPorId(id);
    }

    @Test
    void crearTest() throws Exception{
        when(productoService.guardar(any(ProductoInputDTO.class))).thenReturn(productoResponseDTOuno);
        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoInputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value(productoInputDTO.getTitulo()));
        verify(productoService).guardar(productoInputDTO);
    }

    @Test
    void crearProductoYaExisteTests() throws Exception{
        when(productoService.guardar(any(ProductoInputDTO.class))).thenThrow(badRequestExceptionProductoYaExiste);
        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoInputDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(badRequestExceptionProductoYaExiste.getMessage()));
        verify(productoService).guardar(any(ProductoInputDTO.class));
    }

    @Test
    void crearProveedorNoExiste() throws Exception{
        when(productoService.guardar(any(ProductoInputDTO.class))).thenThrow(recursoNoEncontradoExceptionProveedor);
        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoInputDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProveedor.getMessage()));
        verify(productoService).guardar(any(ProductoInputDTO.class));
    }

    @Test
    void actualizarTest() throws Exception{
        when(productoService.actualizar(any(Long.class), any(Producto.class), any(Long.class))).thenReturn(productoResponseDTOuno);
        mockMvc.perform(put("/api/v1/productos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoInputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
        verify(productoService).actualizar(any(Long.class), any(Producto.class), any(Long.class));
    }

    @Test
    void actualizarProductoNoEncontradoTest() throws Exception{
        when(productoService.actualizar(any(Long.class), any(Producto.class), any(Long.class))).thenThrow(recursoNoEncontradoExceptionProducto);
        mockMvc.perform(put("/api/v1/productos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoInputDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProducto.getMessage()));
        verify(productoService).actualizar(any(Long.class), any(Producto.class), any(Long.class));
    }

    @Test
    void actualizarISBNyaRegistradoTest() throws Exception{
        when(productoService.actualizar(any(Long.class), any(Producto.class), any(Long.class))).thenThrow(badRequestExceptionISBNRegistrado);
        mockMvc.perform(put("/api/v1/productos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoInputDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(badRequestExceptionISBNRegistrado.getMessage()));
        verify(productoService).actualizar(any(Long.class), any(Producto.class), any(Long.class));
    }

    @Test
    void actualizarProveedorNoEncontradoTest() throws Exception{
        when(productoService.actualizar(any(Long.class), any(Producto.class), any(Long.class))).thenThrow(recursoNoEncontradoExceptionProveedor);
        mockMvc.perform(put("/api/v1/productos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoInputDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProveedor.getMessage()));
        verify(productoService).actualizar(any(Long.class), any(Producto.class), any(Long.class));
    }

    @Test
    void eliminarTest() throws Exception{
        when(productoService.eliminar(id)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/productos/{id}", id))
                .andExpect(status().isNoContent());
        verify(productoService).eliminar(id);
    }

    @Test
    void eliminarNoEncontradoTest() throws Exception{
        when(productoService.eliminar(id)).thenThrow(recursoNoEncontradoExceptionProducto);
        mockMvc.perform(delete("/api/v1/productos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(recursoNoEncontradoExceptionProducto.getMessage()));
        verify(productoService).eliminar(id);
    }
}
