package com.example.libreriaMarketCatalogService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "productos") // nombre de la tabla en la BD
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // identificador único del producto

    @NotBlank(message = "El título es obligatorio")
    private String titulo; // nombre del libro

    @NotBlank(message = "El autor es obligatorio")
    private String autor;

    @NotBlank(message = "La editorial es obligatoria")
    private String editorial;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1000, message = "Año inválido")
    @Max(value = 2100, message = "Año inválido")
    private Integer anioPublicacion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotBlank(message = "El ISBN es obligatorio")
    private String isbn;

    private String descripcion;

    // relación: muchos productos pueden tener un proveedor
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    @JsonIgnoreProperties("productos") // evita errores al mostrar JSON
    private Proveedor proveedor;
}
