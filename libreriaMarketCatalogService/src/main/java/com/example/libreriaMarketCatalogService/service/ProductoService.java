package com.example.libreriaMarketCatalogService.service;

import org.springframework.instrument.classloading.jboss.JBossLoadTimeWeaver;
import org.springframework.stereotype.Service;
import java.util.List;

import com.example.libreriaMarketCatalogService.model.*;
import com.example.libreriaMarketCatalogService.repository.*;
import com.example.libreriaMarketCatalogService.exceptions.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    private final ProductoRepository repo;
    private final ProveedorRepository proveedorRepo;

    public ProductoService(ProductoRepository repo, ProveedorRepository proveedorRepo) {
        this.repo = repo;
        this.proveedorRepo = proveedorRepo;
    }

    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Producto obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
    }

    public Producto guardar(Producto p, Long proveedorId) {

        if (repo.findByIsbn(p.getIsbn()).isPresent()) {
            throw new BadRequestException("Ya existe un producto con ese ISBN");
        }

        Proveedor proveedor = proveedorRepo.findById(proveedorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no existe"));

        p.setProveedor(proveedor);
        return repo.save(p);
    }

    @Transactional(readOnly = true)
    public Boolean existsById(Long id) {
        return repo.existsById(id);
    }


    public Producto actualizar(Long id, Producto p, Long proveedorId) {

        Producto existente = obtener(id);

        repo.findByIsbn(p.getIsbn())
                .filter(prod -> !prod.getId().equals(id))
                .ifPresent(prod -> {
                    throw new BadRequestException("ISBN ya registrado");
                });

        Proveedor proveedor = proveedorRepo.findById(proveedorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no existe"));

        existente.setTitulo(p.getTitulo());
        existente.setAutor(p.getAutor());
        existente.setEditorial(p.getEditorial());
        existente.setCategoria(p.getCategoria());
        existente.setAnioPublicacion(p.getAnioPublicacion());
        existente.setPrecio(p.getPrecio());
//        existente.setStock(p.getStock());
        existente.setIsbn(p.getIsbn());
        existente.setDescripcion(p.getDescripcion());
        existente.setProveedor(proveedor);

        return repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.delete(obtener(id));
    }

//    public Producto comprar(Long id, int cantidad) {
//
//        if (cantidad <= 0) {
//            throw new BadRequestException("Cantidad inválida");
//        }
//
//        Producto p = obtener(id);
//
//        if (p.getStock() < cantidad) {
//            throw new BadRequestException("Stock insuficiente");
//        }
//
//        p.setStock(p.getStock() - cantidad);
//        return repo.save(p);
//    }
}