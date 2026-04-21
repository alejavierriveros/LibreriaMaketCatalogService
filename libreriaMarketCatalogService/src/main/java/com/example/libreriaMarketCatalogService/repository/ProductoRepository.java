package com.example.libreriaMarketCatalogService.repository;

import com.example.libreriaMarketCatalogService.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // buscar producto por título
    Optional<Producto> findByTitulo(String titulo);

    // buscar por ISBN (mejor opción real)
    Optional<Producto> findByIsbn(String isbn);
}