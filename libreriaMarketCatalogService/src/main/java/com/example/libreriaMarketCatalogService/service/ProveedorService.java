package com.example.libreriaMarketCatalogService.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.libreriaMarketCatalogService.model.Proveedor;
import com.example.libreriaMarketCatalogService.repository.ProveedorRepository;



@Service
public class ProveedorService {

    private final ProveedorRepository repository;

    public ProveedorService(ProveedorRepository repository) {
        this.repository = repository;
    }

    // listar proveedores
    public List<Proveedor> listar() {
        return repository.findAll();
    }

    // buscar por ID
    public Proveedor obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
    }

    // crear proveedor
    public Proveedor guardar(Proveedor proveedor){

    // VALIDACIÓN PRIMERO
    if(proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()){
        throw new RuntimeException("El nombre no puede estar vacío");
    }

    // luego recién consultas BD
    if(repository.existsByNombre(proveedor.getNombre())){
        throw new RuntimeException("Ya existe un proveedor con ese nombre");
    }

    return repository.save(proveedor);
    }

    // actualizar proveedor
    public Proveedor actualizar(Long id, Proveedor proveedor) {

        Proveedor existente = obtenerPorId(id);

        existente.setNombre(proveedor.getNombre());
        existente.setContacto(proveedor.getContacto());

        return repository.save(existente);
    }

    // eliminar proveedor
    public String eliminar(Long id) {
        Proveedor proveedor = obtenerPorId(id);
        repository.delete(proveedor);
        return "Proveedor eliminado";
    }
}
