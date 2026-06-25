package com.example.libreriaMarketCatalogService.service;

import com.example.libreriaMarketCatalogService.dto.ProveedorDTO;
import com.example.libreriaMarketCatalogService.exceptions.BadRequestException;
import com.example.libreriaMarketCatalogService.exceptions.RecursoNoEncontradoException;
import com.example.libreriaMarketCatalogService.model.Producto;
import com.example.libreriaMarketCatalogService.model.Proveedor;
import com.example.libreriaMarketCatalogService.repository.ProveedorRepository;
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
public class ProveedorServiceTest {
    @MockitoBean
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProveedorService proveedorService;

    private Proveedor proveedorUno;
    private Proveedor proveedorDos;
    private ProveedorDTO.Request proveedorRequest;

    Long id = 1L; //id de ejemplo

    private final RecursoNoEncontradoException recursoNoEncontradoException = new RecursoNoEncontradoException("Proveedor no encontrado");
    private final BadRequestException badRequestExceptionProveedorYaExiste =  new BadRequestException("Ya existe un proveedor con ese nombre");
    private final BadRequestException badRequestExceptionProductosAsociados = new BadRequestException("No se puede eliminar proveedor con productos asociados");

    @BeforeEach
    void setUp(){
        //maquetas de entidades y DTOs
        proveedorUno = new Proveedor(1L, "Distribuidora Libros", "libros@distro.com", null);
        proveedorDos = new Proveedor( 2L, "Proveedor Test", "contacto@test.cl", null);

        Producto productoUno = new Producto(3L, "Java for Dummies", "Terry A. Burd", "Wiley", "Programación",
                2022, 26725.0, "978-111-98-6164-5", "desc", proveedorUno);

        Producto productoDos = new Producto(5L, "Clean Code", "Robert C. Martin", "Prentice Hall", "Programación",
                2008, 30000.0, "978-013-23-5088-4", "desc", proveedorDos);

        proveedorUno.setProductos(List.of(productoUno, productoDos));

        proveedorRequest = new ProveedorDTO.Request("Distribuidora Libros", "libros@distro.com", List.of());
    }

    @Test
    void listarTest(){
        when(proveedorRepository.findAll()).thenReturn(List.of(proveedorUno, proveedorDos));

        List<ProveedorDTO.Response> resultados = proveedorService.listar();

        assertFalse(resultados.isEmpty());
        assertEquals(proveedorUno.getNombre(), resultados.getFirst().getNombre());
        verify(proveedorRepository).findAll();
    }

    @Test
    void listarVacioTest(){
        when(proveedorRepository.findAll()).thenReturn(List.of());

        List<ProveedorDTO.Response> resultados = proveedorService.listar();

        assertTrue(resultados.isEmpty());
        verify(proveedorRepository).findAll();
    }

    @Test
    void obtenerPorIdTest(){
        when(proveedorRepository.findById(id)).thenReturn(Optional.of(proveedorUno));

        ProveedorDTO.Response resultado = proveedorService.obtenerPorId(id);

        assertEquals(id, resultado.getId());
        verify(proveedorRepository).findById(id);
    }

    @Test
    void obtenerPorIdNoEncontradoTest(){
        when(proveedorRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> proveedorService.obtenerPorId(id));
        String expectedMessage = recursoNoEncontradoException.getMessage();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(proveedorRepository).findById(id);
    }

    @Test
    void guardarTest(){
        when(proveedorRepository.findByNombre(proveedorRequest.getNombre())).thenReturn(Optional.empty());
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProveedorDTO.Response resultado = proveedorService.guardar(proveedorRequest);

        assertEquals(proveedorRequest.getNombre(), resultado.getNombre());
        verify(proveedorRepository).findByNombre(proveedorRequest.getNombre());
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @Test
    void guardarYaExisteTest(){
        when(proveedorRepository.findByNombre(proveedorRequest.getNombre())).thenReturn(Optional.of(proveedorUno));

        Exception exception = assertThrows(BadRequestException.class, () -> proveedorService.guardar(proveedorRequest));
        String expectedMessage = badRequestExceptionProveedorYaExiste.getMessage();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(proveedorRepository).findByNombre(proveedorRequest.getNombre());
        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    @Test
    void actualizarTest(){
        when(proveedorRepository.findById(id)).thenReturn(Optional.of(proveedorUno));
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedorUno);

        ProveedorDTO.Response resultado = proveedorService.actualizar(id, proveedorRequest);

        assertEquals(id, resultado.getId());
        verify(proveedorRepository).findById(id);
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @Test
    void actualizarNoExisteTest(){
        when(proveedorRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> proveedorService.actualizar(id, proveedorRequest));
        String expectedMessage = recursoNoEncontradoException.getMessage();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(proveedorRepository).findById(id);
        verify(proveedorRepository, never()).save(any(Proveedor.class));
    }

    @Test
    void eliminarTest(){
        id = 2L;
        when(proveedorRepository.findById(id)).thenReturn(Optional.of(proveedorDos));

        boolean resultado = proveedorService.eliminar(id);

        assertTrue(resultado);
        verify(proveedorRepository).findById(id);
        verify(proveedorRepository).delete(proveedorDos);
    }

    @Test
    void eliminarNoEncontradoTest(){
        id = 2L;
        when(proveedorRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecursoNoEncontradoException.class, () -> proveedorService.eliminar(id));
        String expectedMessage = recursoNoEncontradoException.getMessage();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(proveedorRepository).findById(id);
        verify(proveedorRepository, never()).delete(proveedorDos);
    }

    @Test
    void eliminarProductosAsociadosExceptionTest(){
        id = 2L;
        when(proveedorRepository.findById(id)).thenReturn(Optional.of(proveedorUno));

        Exception exception = assertThrows(BadRequestException.class, () -> proveedorService.eliminar(id));
        String expectedMessage = badRequestExceptionProductosAsociados.getMessage();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(proveedorRepository).findById(id);
        verify(proveedorRepository, never()).delete(proveedorDos);
    }
}
