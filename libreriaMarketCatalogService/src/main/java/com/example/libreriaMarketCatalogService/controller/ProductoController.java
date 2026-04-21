package com.example.libreriaMarketCatalogService.controller;

import org.springframework.web.bind.annotation.*;

import com.example.libreriaMarketCatalogService.model.Producto;
import com.example.libreriaMarketCatalogService.service.ProductoService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

     private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // listar productos
    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    // buscar por id
    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    // crear producto
    @PostMapping
    public String crear(@Valid @RequestBody Producto producto) {
        service.guardar(producto);
        return "Producto creado";
    }

    // actualizar
    @PutMapping("/{id}")
    public String actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        service.actualizar(id, producto);
        return "Producto actualizado";
    }

    // eliminar
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        return service.eliminar(id);
    }

    // comprar
    @PostMapping("/{id}/comprar")
    public String comprar(@PathVariable Long id, @RequestParam int cantidad) {
        service.comprar(id, cantidad);
        return "Compra realizada";
    }
}
