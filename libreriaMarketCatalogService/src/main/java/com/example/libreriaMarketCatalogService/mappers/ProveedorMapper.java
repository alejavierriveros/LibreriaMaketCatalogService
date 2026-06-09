package com.example.libreriaMarketCatalogService.mappers;

import com.example.libreriaMarketCatalogService.dto.ProveedorDTO;
import com.example.libreriaMarketCatalogService.model.Proveedor;

public class ProveedorMapper {

    public static ProveedorDTO.Response toDTO(Proveedor p) {
        return new ProveedorDTO.Response(p.getId(), p.getNombre(), p.getContacto());
    }

    public static Proveedor toEntity(ProveedorDTO.Request dto) {
        Proveedor entityProv = new Proveedor();
        entityProv.setNombre(dto.getNombre());
        entityProv.setContacto(dto.getNombre());
        entityProv.setProductos(null);
        return entityProv;
    }
}