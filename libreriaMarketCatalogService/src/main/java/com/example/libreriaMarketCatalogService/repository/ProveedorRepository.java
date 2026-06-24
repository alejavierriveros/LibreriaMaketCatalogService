package com.example.libreriaMarketCatalogService.repository;

import com.example.libreriaMarketCatalogService.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    boolean existsByNombre(String nombre);
    Optional<Proveedor> findByNombre(String nombre);
}