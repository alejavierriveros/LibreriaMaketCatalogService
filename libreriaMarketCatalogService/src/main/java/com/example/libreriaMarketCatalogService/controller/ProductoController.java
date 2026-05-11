package com.example.libreriaMarketCatalogService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import com.example.libreriaMarketCatalogService.dto.*;
import com.example.libreriaMarketCatalogService.mappers.*;
import com.example.libreriaMarketCatalogService.service.*;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoDTO> listar() {
        return service.listar().stream().map(ProductoMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ProductoDTO obtener(@PathVariable Long id) {
        return ProductoMapper.toDTO(service.obtener(id));
    }

    @PostMapping
    public ProductoDTO crear(@Valid @RequestBody ProductoDTO dto) {
        return ProductoMapper.toDTO(
                service.guardar(
                        ProductoMapper.toEntity(dto),
                        dto.getProveedorId()
                )
        );
    }

    @PutMapping("/{id}")
    public ProductoDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProductoDTO dto) {
        return ProductoMapper.toDTO(
                service.actualizar(
                        id,
                        ProductoMapper.toEntity(dto),
                        dto.getProveedorId()
                )
        );
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Producto eliminado";
    }

    @PostMapping("/{id}/comprar")
    public ProductoDTO comprar(@PathVariable Long id, @RequestParam int cantidad) {
        return ProductoMapper.toDTO(service.comprar(id, cantidad));
    }
}