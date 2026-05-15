package com.example.libreriaMarketCatalogService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<ProductoResponseDTO> listar() {
        return service.listar().stream().map(ProductoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ProductoResponseDTO obtener(@PathVariable Long id) {
        return ProductoMapper.toDto(service.obtener(id));
    }

    @GetMapping("/exists-by-id/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @PostMapping
    public ProductoResponseDTO crear(@Valid @RequestBody ProductoInputDTO dto) {
        return service.guardar(dto);
    }

    @PutMapping("/{id}")
    public ProductoResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProductoInputDTO dto) {
        return ProductoMapper.toDto(
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

//    @PostMapping("/{id}/comprar")
//    public ProductoResponseDTO comprar(@PathVariable Long id, @RequestParam int cantidad) {
//        return ProductoMapper.toDTO(service.comprar(id, cantidad));
//    }
}