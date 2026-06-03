package com.example.libreriaMarketCatalogService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import com.example.libreriaMarketCatalogService.dto.*;
import com.example.libreriaMarketCatalogService.mappers.*;
import com.example.libreriaMarketCatalogService.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Producto", description = "Gestion de productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todos", description = "Muestra todos los registros de productos")
    public List<ProductoResponseDTO> listar() {
        return service.listar().stream().map(ProductoMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca por ID", description = "Encuentra producto por ID")
    public ProductoResponseDTO obtener(@PathVariable Long id) {
        return ProductoMapper.toDto(service.obtener(id));
    }

    @GetMapping("/exists-by-id/{id}")
    @Operation(summary = "Valida existencia", description = "Verifica si el producto existe o no según su ID")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @PostMapping
    @Operation(summary = "Crea registro producto", description = "Guarda nuevo producto")
    public ProductoResponseDTO crear(@Valid @RequestBody ProductoInputDTO dto) {
        return service.guardar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar informacion", description = "Actualiza datos de producto encontrado por ID")
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
    @Operation(summary = "Eliminar registro", description = "Borra registro de producto")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Producto eliminado";
    }

//    @PostMapping("/{id}/comprar")
//    public ProductoResponseDTO comprar(@PathVariable Long id, @RequestParam int cantidad) {
//        return ProductoMapper.toDTO(service.comprar(id, cantidad));
//    }
}