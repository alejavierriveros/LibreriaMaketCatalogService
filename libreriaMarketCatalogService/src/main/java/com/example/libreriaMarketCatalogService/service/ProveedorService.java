package com.example.libreriaMarketCatalogService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.libreriaMarketCatalogService.model.Proveedor;
import com.example.libreriaMarketCatalogService.repository.ProveedorRepository;
import com.example.libreriaMarketCatalogService.dto.ProveedorDTO;
import com.example.libreriaMarketCatalogService.exceptions.*;
import com.example.libreriaMarketCatalogService.mappers.ProveedorMapper;

@Service
@Transactional
public class ProveedorService {

    @Autowired
    private ProveedorRepository repo;

    public List<ProveedorDTO.Response> listar() {
        return repo.findAll().stream().map(ProveedorMapper::toDTO).toList();
    }

    public ProveedorDTO.Response obtenerPorId(Long id) {
        return repo.findById(id).map(ProveedorMapper::toDTO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado"));
    }

    public ProveedorDTO.Response guardar(ProveedorDTO.Request p) {

        Proveedor guardado = new Proveedor();

        guardado.setNombre(p.getNombre());
        guardado.setContacto(p.getContacto());
        guardado.setProductos(p.getProductos());

        return ProveedorMapper.toDTO(repo.save(guardado));
    }

    public ProveedorDTO.Response actualizar(Long id, ProveedorDTO.Request p) {

        Proveedor actualizar = repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado"));
        actualizar.setNombre(p.getNombre());
        actualizar.setContacto(p.getContacto());
        actualizar.setProductos(p.getProductos());

        return ProveedorMapper.toDTO(repo.save(actualizar));
    }

    public boolean eliminar(Long id) {

        Proveedor proveedor = repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado"));

        if (proveedor.getProductos() != null && !proveedor.getProductos().isEmpty()) {
            throw new BadRequestException("No se puede eliminar proveedor con productos asociados");
        }

        repo.delete(proveedor);
        return true;
    }
}