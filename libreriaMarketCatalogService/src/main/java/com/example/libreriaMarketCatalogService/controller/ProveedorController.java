package com.example.libreriaMarketCatalogService.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.libreriaMarketCatalogService.model.Proveedor;
import com.example.libreriaMarketCatalogService.service.ProveedorService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/proveedores")
public class ProveedorController {

    private final ProveedorService service;

    public ProveedorController(ProveedorService service) {
        this.service = service;
    }

    // listar proveedores
    @GetMapping
    public List<Proveedor> listar() {
        return service.listar();
    }

    // buscar proveedor
    @GetMapping("/{id}")
    public Proveedor obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    public String crear(@Valid @RequestBody Proveedor proveedor) {
    service.guardar(proveedor);
    return "Proveedor creado correctamente";
    }

    // actualizar
    @PutMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @RequestBody Proveedor proveedor) {
        service.actualizar(id, proveedor);
        return "Proveedor actualizado";
    }

    // eliminar
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        return service.eliminar(id);
    }

}
