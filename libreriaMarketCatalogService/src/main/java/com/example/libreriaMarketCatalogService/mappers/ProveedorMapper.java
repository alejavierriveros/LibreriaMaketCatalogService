package com.example.libreriaMarketCatalogService.mappers;

import com.example.libreriaMarketCatalogService.dto.ProveedorDTO;
import com.example.libreriaMarketCatalogService.model.Proveedor;

public class ProveedorMapper {

    public static ProveedorDTO toDTO(Proveedor p) {
        return new ProveedorDTO(p.getId(), p.getNombre(), p.getContacto());
    }

    public static Proveedor toEntity(ProveedorDTO dto) {
        return new Proveedor(dto.getId(), dto.getNombre(), dto.getContacto(), null);
    }
}