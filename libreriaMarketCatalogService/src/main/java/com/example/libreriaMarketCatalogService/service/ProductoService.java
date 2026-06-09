package com.example.libreriaMarketCatalogService.service;

import com.example.libreriaMarketCatalogService.dto.ProductoInputDTO;
import com.example.libreriaMarketCatalogService.dto.ProductoResponseDTO;
import com.example.libreriaMarketCatalogService.mappers.ProductoMapper;
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
    public List<ProductoResponseDTO> listar() {
        return repo.findAll().stream().map(ProductoMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO obtener(Long id) {
        return repo.findById(id).map(ProductoMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
    }

    public ProductoResponseDTO guardar(ProductoInputDTO dto) {

        if (repo.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new BadRequestException("Ya existe un producto con ese ISBN");
        }

        if (!proveedorRepo.existsById(dto.getProveedorId())){
                throw new RecursoNoEncontradoException("Proveedor no existe");
        }

        return ProductoMapper.toDto(repo.save(ProductoMapper.toEntity(dto)));
    }

    @Transactional(readOnly = true)
    public Boolean existsById(Long id) {
        return repo.existsById(id);
    }


    public ProductoResponseDTO actualizar(Long id, Producto p, Long proveedorId) {

        Producto existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

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

        return ProductoMapper.toDto(repo.save(existente));
    }

    public Boolean eliminar(Long id) {
        if(repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }else{
            throw new RecursoNoEncontradoException("ID de producto no existe");
        }
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