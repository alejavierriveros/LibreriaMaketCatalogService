package com.example.libreriaMarketCatalogService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private String editorial;
    private String categoria;
    private Integer anioPublicacion;
    private Double precio;
    private Integer stock;
    private String isbn;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    @JsonIgnoreProperties("productos")
    private Proveedor proveedor;
}