package com.example.libreriaMarketCatalogService.controller;

import com.example.libreriaMarketCatalogService.assemblers.ProductoModelAssembler;
import com.example.libreriaMarketCatalogService.dto.ProductoInputDTO;
import com.example.libreriaMarketCatalogService.dto.ProductoResponseDTO;
import com.example.libreriaMarketCatalogService.mappers.ProductoMapper;
import com.example.libreriaMarketCatalogService.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/productos")
public class ProductoControllerV2 {
    

    private static final Logger logger = LoggerFactory.getLogger(ProductoControllerV2.class.getName());

    @Autowired
    private ProductoService service;

    @Autowired
    private ProductoModelAssembler assembler;

    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Productos encontrados",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado productos",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Lista todos", description = "Muestra todos los registros de productos")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ProductoResponseDTO>>> listar() {
        String logMsgRequest = "Recibiendo solicitud para buscar listado de productos.";
        String logMsg = "Solicitud para buscar listado de productos.";
        logger.info(logMsgRequest);
        List<EntityModel<ProductoResponseDTO>> listadoDTO = service.listar().stream().map(assembler::toModel).collect(Collectors.toList());

        if (!listadoDTO.isEmpty()){
            logger.info(logMsg + "=> encontrado(s) y enlistado(s).");
            return ResponseEntity.ok(CollectionModel.of(listadoDTO, linkTo(methodOn(ProductoControllerV2.class).listar()).withSelfRel()));
        }
        logger.info(logMsg + "=> sin coincidencias (vacío).");
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Producto encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoResponseDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado el producto",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Busca por ID", description = "Encuentra producto por ID")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoResponseDTO>> obtener(@PathVariable Long id) {
        String logMsgRequest = "Recibiendo solicitud para buscar producto por ID: " + id + ".";
        String logMsg = "Solicitud para buscar producto por ID: " + id + ".";
        logger.info(logMsgRequest);
        ProductoResponseDTO dto = service.obtener(id);
        if (dto != null){
            logger.info(logMsg + "=> encontrado.");
            return ResponseEntity.ok(assembler.toModel(dto));
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }

    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Sí existe",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoResponseDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Valida existencia", description = "Verifica si el producto existe o no según su ID")
    @GetMapping(value = "/exists-by-id/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "201",
                description = "Producto guardado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoResponseDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto al hacer solicitud",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Crea registro producto", description = "Guarda nuevo producto")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoResponseDTO>> crear(@Valid @RequestBody ProductoInputDTO dto) {
        String logMsgRequest = "Recibiendo solicitud para crear/guardar producto.";
        String logMsg = "Solicitud para crear/guardar producto.";
        logger.info(logMsgRequest);
        ProductoResponseDTO created = service.guardar(dto);
        URI location = linkTo(methodOn(ProductoControllerV2.class).obtener(created.getId())).toUri();
        
        logger.info(logMsg + 
            "=> creado con ID Producto: {}, ID Producto: {}, Titulo: {}, Autor: {}, Editorial: {}, Categoria: {}, Anio Publicacion: {}, ISBN: {}, Descripcion: {}, ID Proveedor: {}, Nombre Proveedor: {}", 
                                                                                                            created.getId(), 
                                                                                                            created.getTitulo(), 
                                                                                                            created.getAutor(), 
                                                                                                            created.getEditorial(),
                                                                                                            created.getCategoria(),
                                                                                                            created.getAnioPublicacion(),
                                                                                                            created.getIsbn(),
                                                                                                            created.getDescripcion(),
                                                                                                            created.getProveedorId(),
                                                                                                            created.getProveedorNombre()
                                                                                                        );
        return ResponseEntity.created(location).body(assembler.toModel(created));
    }

     @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Producto actualizado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoResponseDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado producto",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Actualizar informacion", description = "Actualiza datos de producto encontrado por ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProductoResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoInputDTO dto) {

        String logMsgRequest = "Recibiendo solicitud para actualizar producto con ID: " + id + ".";
        String logMsg = "Solicitud para actualizar producto con ID: " + id + ".";
        logger.info(logMsgRequest);
        
        ProductoResponseDTO updated = service.actualizar(id, ProductoMapper.toEntity(dto), dto.getProveedorId());
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updated.getId()).toUri();
        //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        
        logger.info(logMsg + " => actualizado.");
        return ResponseEntity.status(200).location(location).body(assembler.toModel(updated));
        //devuelve el estado y la locación //devuelve el objeto creado
    }

    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "204",
                description = "Producto eliminado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoResponseDTO.class)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado producto",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Eliminar registro", description = "Borra registro de producto")
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        String logMsgRequest = "Recibiendo solicitud para borrar producto con ID: " + id + ".";
        String logMsg = "Solicitud para borrar producto con ID: " + id + ".";
        logger.info(logMsgRequest);
        if(service.eliminar(id)){
            logger.info(logMsg + " => encontrado y borrado.");
            return ResponseEntity.noContent().build();
        }
        logger.info(logMsg + " => no encontrado.");
        return ResponseEntity.notFound().build();
    }
}
