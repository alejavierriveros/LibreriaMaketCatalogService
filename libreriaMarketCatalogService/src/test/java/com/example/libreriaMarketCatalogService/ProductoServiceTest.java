package com.example.libreriaMarketCatalogService;

import com.example.libreriaMarketCatalogService.dto.ProductoInputDTO;
import com.example.libreriaMarketCatalogService.dto.ProductoResponseDTO;
import com.example.libreriaMarketCatalogService.exceptions.BadRequestException;
import com.example.libreriaMarketCatalogService.exceptions.RecursoNoEncontradoException;
import com.example.libreriaMarketCatalogService.model.Producto;
import com.example.libreriaMarketCatalogService.model.Proveedor;
import com.example.libreriaMarketCatalogService.repository.ProductoRepository;
import com.example.libreriaMarketCatalogService.repository.ProveedorRepository;
import com.example.libreriaMarketCatalogService.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductoServiceTest {
    @MockitoBean
    private ProductoRepository productoRepository;

    @MockitoBean
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProductoService productoService;

    private ProductoInputDTO productoInputDTO;
    private Producto productoUno;
    private Producto productoDos;
    private Producto productoUpdate;

    private Proveedor proveedor;

    @BeforeEach
    void setUp(){
        proveedor = new Proveedor(1L, "Distribuidora Libros", "libros@distro.com", List.of());

        productoUno = new Producto(3L, "Java for Dummies", "Terry A. Burd", "Wiley", "Programación",
                2022, 26725.0, "978-111-98-6164-5", "desc", proveedor);

        productoDos = new Producto(5L, "Clean Code", "Robert C. Martin", "Prentice Hall", "Programación",
                2008, 30000.0, "978-013-23-5088-4", "desc", proveedor);

        proveedor.setProductos(List.of(productoUno, productoDos));

        productoInputDTO = new ProductoInputDTO("Java for Dummies", "Terry A. Burd", "Wiley", "Programación",
                2022, 26725.0, "978-111-98-6164-5", "desc", 1L, "Distribuidora Libros");

        productoUpdate = new Producto(3L, "Java for Dummies", "Terry A. Burd", "Wiley", "Programación",
                2017, 20000.0, "978-111-98-6164-5", "Descripción", proveedor);
    }

    @Test
    void listarTest(){
        when(productoRepository.findAll()).thenReturn(List.of(productoUno, productoDos));

        List<ProductoResponseDTO> productos = productoService.listar();

        assertNotNull(productos);
        assertFalse(productos.isEmpty());
        verify(productoRepository).findAll();
    }

    @Test
    void listarVacioTest(){
        when(productoRepository.findAll()).thenReturn(List.of());
        List<ProductoResponseDTO> productos = productoService.listar();

        assertTrue(productos.isEmpty());
        verify(productoRepository).findAll();
    }

    @Test
    void obtenerPorIdTest(){
        Long id = 3L;
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoUno));

        ProductoResponseDTO productoResponseDTO = productoService.obtenerPorId(id);

        assertEquals(productoResponseDTO.getId(), productoUno.getId());
        verify(productoRepository).findById(id);
    }

    @Test
    void obtenerPorIdNoEncontradoTest(){
        Long id = 3L;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> productoService.obtenerPorId(id));

        String expectedMessage = "Producto no encontrado";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(productoRepository).findById(id);
    }

    @Test
    void guardarTest(){
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productoRepository.findByIsbn(productoUno.getIsbn())).thenReturn(Optional.empty());
        when(proveedorRepository.existsById(proveedor.getId())).thenReturn(true);

        ProductoResponseDTO productoResponseDTO = productoService.guardar(productoInputDTO);

        assertNotNull(productoResponseDTO);
        verify(productoRepository).save(any(Producto.class));
        verify(productoRepository).findByIsbn(productoUno.getIsbn());
        verify(proveedorRepository).existsById(proveedor.getId());
    }

    @Test
    void guardarYaExistenteTest(){
        when(productoRepository.findByIsbn((productoUno.getIsbn()))).thenReturn(Optional.of(productoDos));

        Exception exception = assertThrows(BadRequestException.class, () -> productoService.guardar(productoInputDTO));

        String expectedMessage = "Ya existe un producto con ese ISBN";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(productoRepository, never()).save(any(Producto.class));
        verify(productoRepository).findByIsbn(productoUno.getIsbn());
    }

    @Test
    void guardarProveedorNoExistenteTest(){
        when(proveedorRepository.existsById(proveedor.getId())).thenReturn(false);

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> productoService.guardar(productoInputDTO));

        String expectedMessage = "Proveedor no existe";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(productoRepository, never()).save(any(Producto.class));
        verify(proveedorRepository).existsById(proveedor.getId());
    }

    @Test
    void existsByIdTest(){
        Long id = 3L;
        when(productoRepository.existsById(id)).thenReturn(true);
        boolean resultado = productoService.existsById(id);
        assertTrue(resultado);
        verify(productoRepository).existsById(id);
    }

    @Test
    void actualizarTest(){
        Long id = 3L;
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoUno));
        when(productoRepository.findByIsbn(productoUpdate.getIsbn())).thenReturn(Optional.empty());
        when(proveedorRepository.findById(proveedor.getId())).thenReturn(Optional.of(proveedor));

        ProductoResponseDTO productoResponseDTO = productoService.actualizar(id, productoUpdate, proveedor.getId());

        assertNotNull(productoResponseDTO);
        assertEquals(productoResponseDTO.getId(), productoUpdate.getId());
        verify(productoRepository).save(any(Producto.class));
        verify(productoRepository).findById(id);
        verify(productoRepository).findByIsbn(productoUpdate.getIsbn());
        verify(proveedorRepository).findById(proveedor.getId());
    }

    @Test
    void actualizarProductoNoEncontradoTest(){
        Long id = 3L;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> productoService.actualizar(id, productoUpdate, proveedor.getId()));

        String expectedMessage = "Producto no encontrado";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(productoRepository).findById(id);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void actualizarISBNyaRegistrado(){
        Long id = 3L;
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoUno));
        when(productoRepository.findByIsbn(productoUpdate.getIsbn())).thenReturn(Optional.of(productoDos));

        Exception exception = assertThrows(BadRequestException.class, () -> productoService.actualizar(id, productoUpdate, proveedor.getId()));

        String expectedMessage = "ISBN ya registrado";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(productoRepository).findByIsbn(productoUpdate.getIsbn());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void actualizarProveedorNoExisteTest(){
        Long id = 3L;
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoUno));
        when(productoRepository.findByIsbn(productoUpdate.getIsbn())).thenReturn(Optional.empty());
        when(proveedorRepository.findById(proveedor.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> productoService.actualizar(id, productoUpdate, proveedor.getId()));

        String expectedMessage = "Proveedor no existe";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(productoRepository).findById(id);
        verify(productoRepository).findByIsbn(productoUpdate.getIsbn());
        verify(proveedorRepository).findById(proveedor.getId());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void deleteTest(){
        Long id = 3L;
        when(productoRepository.existsById(id)).thenReturn(true);
        boolean resultado = productoService.eliminar(id);
        assertTrue(resultado);
        verify(productoRepository).existsById(id);
        verify(productoRepository).deleteById(id);
    }

    @Test
    void deleteNoEncontradoTest(){
        Long id = 3L;
        when(productoRepository.existsById(id)).thenReturn(false);
        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> productoService.eliminar(id));

        String expectedMessage = "ID de producto no existe";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(productoRepository).existsById(id);
        verify(productoRepository, never()).deleteById(id);
    }
}
