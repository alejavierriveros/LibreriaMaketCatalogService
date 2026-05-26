package com.example.libreriaMarketCatalogService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(message = "Debe ingresar un nombre de título de mínimo 2 caracteres y máximo 100.", min = 2 , max = 100)
    private String titulo;

    @NotBlank
    @Length(message = "Debe ingresar un nombre de autor de mínimo 2 caracteres y máximo 100.", min = 2 , max = 100)
    private String autor;

    @NotBlank
    @Length(message = "Debe ingresar un nombre de editorial de mínimo 2 caracteres y máximo 100.", min = 2 , max = 100)
    private String editorial;

    @NotBlank
    @Length(message = "Debe ingresar un nombre de categoría de mínimo 2 caracteres y máximo 20.", min = 2 , max = 20)
    private String categoria;

    @NonNull
    @PositiveOrZero
    @Column(name = "anio_publicacion", nullable = false)
    private Integer anioPublicacion;

    @NonNull
    @PositiveOrZero
    private Double precio;

    @NotBlank
    @Size(message = "Debe ingresar un isbn de 13 dígitos en 5 bloques separados por guiones.", min = 17 , max = 17)
    private String isbn;


    @Size(message = "Puede ingresar una descripción de máximo 300 caracteres.", max = 300)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    @JsonIgnoreProperties("productos")
    private Proveedor proveedor;
}