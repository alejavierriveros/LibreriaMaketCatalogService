package com.example.libreriaMarketCatalogService.repository;

import com.example.libreriaMarketCatalogService.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // buscar proveedor por nombre
    Optional<Proveedor> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}