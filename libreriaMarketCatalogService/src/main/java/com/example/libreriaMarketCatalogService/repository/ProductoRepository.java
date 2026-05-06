package com.example.libreriaMarketCatalogService.repository;

import com.example.libreriaMarketCatalogService.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByIsbn(String isbn);
}