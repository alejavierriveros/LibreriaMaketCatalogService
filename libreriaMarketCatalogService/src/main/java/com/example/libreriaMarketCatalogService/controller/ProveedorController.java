package com.example.libreriaMarketCatalogService.controller;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import com.example.libreriaMarketCatalogService.dto.*;
import com.example.libreriaMarketCatalogService.mappers.*;
import com.example.libreriaMarketCatalogService.service.*;

@RestController
@RequestMapping("/api/v1/proveedores")
public class ProveedorController {

    private final ProveedorService service;

    public ProveedorController(ProveedorService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProveedorDTO> listar() {
        return service.listar().stream().map(ProveedorMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public ProveedorDTO obtener(@PathVariable Long id) {
        return ProveedorMapper.toDTO(service.obtener(id));
    }

    @PostMapping
    public ProveedorDTO crear(@Valid @RequestBody ProveedorDTO dto) {
        return ProveedorMapper.toDTO(service.guardar(ProveedorMapper.toEntity(dto)));
    }

    @PutMapping("/{id}")
    public ProveedorDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorDTO dto) {
        return ProveedorMapper.toDTO(service.actualizar(id, ProveedorMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Proveedor eliminado";
    }
}