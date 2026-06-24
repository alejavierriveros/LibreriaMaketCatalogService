package com.example.libreriaMarketCatalogService;

import com.example.libreriaMarketCatalogService.dto.ProveedorDTO;
import com.example.libreriaMarketCatalogService.repository.ProveedorRepository;
import com.example.libreriaMarketCatalogService.service.ProveedorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class ProveedorServiceTest {
    @MockitoBean
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProveedorService proveedorService;

    private ProveedorDTO.Response proveedorResponse;
    private ProveedorDTO.Request proveedorRequest;

    @Test
    void listarTest(){

    }

    @Test
    void listarVacioTest(){

    }

    @Test
    void obtenerPorIdTest(){

    }

    @Test
    void obtenerPorIdNoEncontradoTest(){

    }

    @Test
    void guardarTest(){

    }

    @Test
    void guardarYaExisteTest(){

    }

    @Test
    void actualizarTest(){

    }

    @Test
    void actualizarNoExisteTest(){

    }

    @Test
    void eliminarTest(){

    }

    @Test
    void eliminarNoEncontradoTest(){

    }

    @Test
    void eliminarProductosAsociadosExceptionTest(){
        
    }
}
