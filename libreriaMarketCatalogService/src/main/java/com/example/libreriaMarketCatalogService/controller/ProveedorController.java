package com.example.libreriaMarketCatalogService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import com.example.libreriaMarketCatalogService.dto.*;
import com.example.libreriaMarketCatalogService.mappers.*;
import com.example.libreriaMarketCatalogService.service.*;

import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping
    @Operation(summary = "Lista todos", description = "Muestra todos los registros de proveedores")
    public List<ProveedorDTO> listar() {
        return service.listar().stream().map(ProveedorMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca por ID", description = "Encuentra proveedor por ID")
    public ProveedorDTO obtener(@PathVariable Long id) {
        return ProveedorMapper.toDTO(service.obtener(id));
    }

    @PostMapping
    @Operation(summary = "Crea registro proveedor", description = "Guarda nuevo proveedor")
    public ProveedorDTO crear(@Valid @RequestBody ProveedorDTO dto) {
        return ProveedorMapper.toDTO(service.guardar(ProveedorMapper.toEntity(dto)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar informacion", description = "Actualiza datos de proveedor encontrado por ID")
    public ProveedorDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorDTO dto) {
        return ProveedorMapper.toDTO(service.actualizar(id, ProveedorMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro", description = "Borra registro de proveedor")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Proveedor eliminado";
    }
}