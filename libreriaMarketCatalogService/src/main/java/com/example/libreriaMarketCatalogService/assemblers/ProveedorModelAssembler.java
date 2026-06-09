package com.example.libreriaMarketCatalogService.assemblers;

import com.example.libreriaMarketCatalogService.controller.ProveedorControllerV2;
import com.example.libreriaMarketCatalogService.dto.ProveedorDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProveedorModelAssembler implements RepresentationModelAssembler<ProveedorDTO.Response, EntityModel<ProveedorDTO.Response>> {

    @Override
    public EntityModel<ProveedorDTO.Response> toModel(ProveedorDTO.Response proveedor){
        return EntityModel.of(proveedor,
                linkTo(methodOn(ProveedorControllerV2.class).obtener(proveedor.getId())).withSelfRel(),
                linkTo(methodOn(ProveedorControllerV2.class).listar()).withRel("proveedores")
        );
    }
}