package com.example.libreriaMarketCatalogService.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {

    private Long id;

    @NotBlank(message = "Título obligatorio")
    private String titulo;

    @NotBlank(message = "Autor obligatorio")
    private String autor;

    @NotBlank(message = "Editorial obligatoria")
    private String editorial;

    @NotBlank(message = "Categoría obligatoria")
    private String categoria;

    @NotNull @Min(1000) @Max(2100)
    private Integer anioPublicacion;

    @NotNull @Positive
    private Double precio;

    @NotNull @Min(0)
    private Integer stock;

    @NotBlank
    private String isbn;

    private String descripcion;

    @NotNull(message = "Proveedor requerido")
    private Long proveedorId;

    private String proveedorNombre;
}