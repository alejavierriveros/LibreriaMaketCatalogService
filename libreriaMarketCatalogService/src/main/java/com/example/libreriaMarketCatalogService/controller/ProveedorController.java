package com.example.libreriaMarketCatalogService.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/proveedores")
@Tag(name = "Proveedor", description = "Gestion de proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService service;

    public ProveedorController(ProveedorService service) {
        this.service = service;
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado registros",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProveedorDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado el registros",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Lista todos", description = "Muestra todos los registros de proveedores")
    @GetMapping
    public List<ProveedorDTO> listar() {
        return service.listar().stream().map(ProveedorMapper::toDTO).toList();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha encontrado el registro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado el registro de proveedor",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Busca por ID", description = "Encuentra proveedor por ID")
    @GetMapping("/{id}")
    public ProveedorDTO obtener(@PathVariable Long id) {
        return ProveedorMapper.toDTO(service.obtener(id));
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Se ha guardado el registro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto al guardar",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Crea registro proveedor", description = "Guarda nuevo proveedor")
    @PostMapping
    public ProveedorDTO crear(@Valid @RequestBody ProveedorDTO dto) {
        return ProveedorMapper.toDTO(service.guardar(ProveedorMapper.toEntity(dto)));
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha actualizado el registro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado registros",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Actualizar informacion", description = "Actualiza datos de proveedor encontrado por ID")
    @PutMapping("/{id}")
    public ProveedorDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorDTO dto) {
        return ProveedorMapper.toDTO(service.actualizar(id, ProveedorMapper.toEntity(dto)));
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Se ha eliminado el registro de proveedor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado registro de proveedor",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Eliminar registro", description = "Borra registro de proveedor")
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Proveedor eliminado";
    }
}