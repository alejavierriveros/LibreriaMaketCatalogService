package com.example.libreriaMarketCatalogService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class ProveedorDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Request{
        @NotBlank(message = "Nombre obligatorio")
        private String nombre;
        private String contacto;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Response{
        private Long id;
        private String nombre;
        private String contacto;
    }

}