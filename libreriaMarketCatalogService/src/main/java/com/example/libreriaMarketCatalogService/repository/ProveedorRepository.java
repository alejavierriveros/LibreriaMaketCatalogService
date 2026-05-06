package com.example.libreriaMarketCatalogService.repository;

import com.example.libreriaMarketCatalogService.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    boolean existsByNombre(String nombre);
}