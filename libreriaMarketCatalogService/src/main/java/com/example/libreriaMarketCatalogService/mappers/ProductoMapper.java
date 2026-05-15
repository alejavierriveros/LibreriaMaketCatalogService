package com.example.libreriaMarketCatalogService.mappers;

import com.example.libreriaMarketCatalogService.dto.ProductoInputDTO;
import com.example.libreriaMarketCatalogService.dto.ProductoResponseDTO;
import com.example.libreriaMarketCatalogService.model.Producto;

public class ProductoMapper {

    public static ProductoResponseDTO toDto(Producto p) {
        ProductoResponseDTO dto = new ProductoResponseDTO();

        dto.setId(p.getId());
        dto.setTitulo(p.getTitulo());
        dto.setAutor(p.getAutor());
        dto.setEditorial(p.getEditorial());
        dto.setCategoria(p.getCategoria());
        dto.setAnioPublicacion(p.getAnioPublicacion());
        dto.setPrecio(p.getPrecio());
//        dto.setStock(p.getStock());
        dto.setIsbn(p.getIsbn());
        dto.setDescripcion(p.getDescripcion());

        if (p.getProveedor() != null) {
            dto.setProveedorId(p.getProveedor().getId());
            dto.setProveedorNombre(p.getProveedor().getNombre());
        }

        return dto;
    }

    public static Producto toEntity(ProductoInputDTO dto) {
        Producto p = new Producto();


        p.setTitulo(dto.getTitulo());
        p.setAutor(dto.getAutor());
        p.setEditorial(dto.getEditorial());
        p.setCategoria(dto.getCategoria());
        p.setAnioPublicacion(dto.getAnioPublicacion());
        p.setPrecio(dto.getPrecio());
//        p.setStock(dto.getStock());
        p.setIsbn(dto.getIsbn());
        p.setDescripcion(dto.getDescripcion());

        return p;
    }
}