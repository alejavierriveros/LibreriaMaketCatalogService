package com.example.libreriaMarketCatalogService.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.example.libreriaMarketCatalogService.model.Proveedor;
import com.example.libreriaMarketCatalogService.repository.ProveedorRepository;
import com.example.libreriaMarketCatalogService.exceptions.*;

@Service
public class ProveedorService {

    private final ProveedorRepository repo;

    public ProveedorService(ProveedorRepository repo) {
        this.repo = repo;
    }

    public List<Proveedor> listar() {
        return repo.findAll();
    }

    public Proveedor obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado"));
    }

    public Proveedor guardar(Proveedor p) {

        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new BadRequestException("Nombre obligatorio");
        }

        if (repo.existsByNombre(p.getNombre())) {
            throw new BadRequestException("Proveedor ya existe");
        }

        return repo.save(p);
    }

    public Proveedor actualizar(Long id, Proveedor p) {

        Proveedor existente = obtener(id);
        existente.setNombre(p.getNombre());
        existente.setContacto(p.getContacto());

        return repo.save(existente);
    }

    public void eliminar(Long id) {

        Proveedor proveedor = obtener(id);

        if (proveedor.getProductos() != null && !proveedor.getProductos().isEmpty()) {
            throw new BadRequestException("No se puede eliminar proveedor con productos asociados");
        }

        repo.delete(proveedor);
    }
}