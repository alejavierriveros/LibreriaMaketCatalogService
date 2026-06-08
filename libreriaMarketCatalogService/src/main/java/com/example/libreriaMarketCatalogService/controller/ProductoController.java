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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Productos encontrados",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado productos",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Lista todos", description = "Muestra todos los registros de productos")
    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return service.listar().stream().map(ProductoMapper::toDto).toList();
    }

    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Producto encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoResponseDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado el producto",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Busca por ID", description = "Encuentra producto por ID")
    @GetMapping("/{id}")
    public ProductoResponseDTO obtener(@PathVariable Long id) {
        return ProductoMapper.toDto(service.obtener(id));
    }

    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Sí existe",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoResponseDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Valida existencia", description = "Verifica si el producto existe o no según su ID")
    @GetMapping("/exists-by-id/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "201",
                description = "Producto guardado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoResponseDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto al hacer solicitud",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Crea registro producto", description = "Guarda nuevo producto")
    @PostMapping
    public ProductoResponseDTO crear(@Valid @RequestBody ProductoInputDTO dto) {
        return service.guardar(dto);
    }

     @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Producto actualizado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoResponseDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado producto",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
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

    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "204",
                description = "Producto eliminado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoResponseDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado producto",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Eliminar registro", description = "Borra registro de producto")
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