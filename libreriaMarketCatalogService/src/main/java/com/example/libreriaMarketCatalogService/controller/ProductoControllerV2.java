package com.example.libreriaMarketCatalogService.controller;

import com.example.libreriaMarketCatalogService.assemblers.ProductoModelAssembler;
import com.example.libreriaMarketCatalogService.dto.ProductoInputDTO;
import com.example.libreriaMarketCatalogService.dto.ProductoResponseDTO;
import com.example.libreriaMarketCatalogService.mappers.ProductoMapper;
import com.example.libreriaMarketCatalogService.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/productos")
public class ProductoControllerV2 {

    @Autowired
    private ProductoService service;
    @Autowired
    private ProductoModelAssembler assembler;

    @Operation(summary = "Lista todos", description = "Muestra todos los registros de productos")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public List<ProductoResponseDTO> listar() {
        return service.listar().stream().map(ProductoMapper::toDto).toList();
    }

    @Operation(summary = "Busca por ID", description = "Encuentra producto por ID")
    @GetMapping("/{id}")
    public ProductoResponseDTO obtener(@PathVariable Long id) {
        return ProductoMapper.toDto(service.obtener(id));
    }

    @Operation(summary = "Valida existencia", description = "Verifica si el producto existe o no según su ID")
    @GetMapping("/exists-by-id/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @Operation(summary = "Crea registro producto", description = "Guarda nuevo producto")
    @PostMapping
    public ProductoResponseDTO crear(@Valid @RequestBody ProductoInputDTO dto) {
        return service.guardar(dto);
    }

    @Operation(summary = "Actualizar informacion", description = "Actualiza datos de producto encontrado por ID")
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

    @Operation(summary = "Eliminar registro", description = "Borra registro de producto")
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Producto eliminado";
    }
}
