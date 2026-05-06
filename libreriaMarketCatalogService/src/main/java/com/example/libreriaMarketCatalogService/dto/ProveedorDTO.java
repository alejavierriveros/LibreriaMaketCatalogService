package com.example.libreriaMarketCatalogService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorDTO {

    private Long id;

    @NotBlank(message = "Nombre obligatorio")
    private String nombre;

    private String contacto;
}