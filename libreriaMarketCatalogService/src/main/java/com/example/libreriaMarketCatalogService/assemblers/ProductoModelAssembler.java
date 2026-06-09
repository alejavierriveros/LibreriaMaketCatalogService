package com.example.libreriaMarketCatalogService.assemblers;

import com.example.libreriaMarketCatalogService.controller.ProductoControllerV2;
import com.example.libreriaMarketCatalogService.dto.ProductoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoResponseDTO, EntityModel<ProductoResponseDTO>> {

    @Override
    public EntityModel<ProductoResponseDTO> toModel(ProductoResponseDTO producto){
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoControllerV2.class).obtener(producto.getId())).withSelfRel(),
            linkTo(methodOn(ProductoControllerV2.class).listar()).withRel("productos")
        );
    }
}
