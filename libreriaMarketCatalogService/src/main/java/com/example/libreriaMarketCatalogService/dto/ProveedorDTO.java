package com.example.libreriaMarketCatalogService.dto;

import java.util.List;

import com.example.libreriaMarketCatalogService.model.Producto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class ProveedorDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        @NotBlank(message = "Nombre obligatorio")
        private String nombre;
        private String contacto;
        private List<Producto> productos;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private Long id;
        private String nombre;
        private String contacto;
    }

}