package com.example.libreriaMarketCatalogService.controller;

import com.example.libreriaMarketCatalogService.assemblers.ProveedorModelAssembler;
import com.example.libreriaMarketCatalogService.dto.ProveedorDTO;
import com.example.libreriaMarketCatalogService.service.ProveedorService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/proveedores")
public class ProveedorControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorControllerV2.class.getName());
    
    @Autowired
    private ProveedorService service;

    @Autowired
    private ProveedorModelAssembler assembler;

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado registros",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProveedorDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado el registros",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Lista todos", description = "Muestra todos los registros de proveedores")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ProveedorDTO.Response>>> listar() {
        String logMsgRequest = "Recibiendo solicitud para buscar listado de proveedores.";
        String logMsg = "Solicitud para buscar listado de proveedores";
        logger.info(logMsgRequest);
        List<EntityModel<ProveedorDTO.Response>> listadoDTOs = service.listar().stream().map(assembler::toModel).collect(Collectors.toList());

        if(!listadoDTOs.isEmpty()){
                logger.info(logMsg + "-> encontrado(s) y listado(s)");
                return ResponseEntity.ok(CollectionModel.of(listadoDTOs, 
                        linkTo(methodOn(ProveedorControllerV2.class).listar()).withSelfRel()));
        }
        logger.info(logMsg + "-> sin coincidencias");
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha encontrado el registro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado el registro de proveedor",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Busca por ID", description = "Encuentra proveedor por ID")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProveedorDTO.Response>> obtener(@PathVariable Long id) {
        String logMsgRequest = "Recibiendo solicitud para buscar proveedor por ID: " + id + ".";
        String logMsg = "Solicitud para buscar proveedor por ID: " + id + ".";
        logger.info(logMsgRequest);

        ProveedorDTO.Response dto = service.obtener(id);

        if(dto != null){
                logger.info(logMsg + "-> encontrado");
                return ResponseEntity.ok(assembler.toModel(dto));
        }

        logger.info(logMsg + "-> no encontrado");

        return ResponseEntity.notFound().build();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Se ha guardado el registro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto al guardar",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Crea registro proveedor", description = "Guarda nuevo proveedor")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProveedorDTO.Response>> crear(@Valid @RequestBody ProveedorDTO.Request dto) {
        String logMsgRequest = "Recibiendo solicitud para crear proveedor";
        String logMsg = "Solicitud para crear proveedor";
        logger.info(logMsgRequest);

        ProveedorDTO.Response created = service.guardar(dto);
        URI location = linkTo(methodOn(ProveedorControllerV2.class).obtener(created.getId())).toUri();

        logger.info(logMsg + "-> creado con ID Proveedor: {}, Nombre {}, Contacto {}", 
                                                                                created.getId(),
                                                                                created.getNombre(),
                                                                                created.getContacto());

        return ResponseEntity.created(location).body(assembler.toModel(created));
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha actualizado el registro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado registros",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Actualizar informacion", description = "Actualiza datos de proveedor encontrado por ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ProveedorDTO.Response>> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorDTO.Request dto) {
        String logMsgRequest = "Recibiendo solicitud para actualizar proveedor con ID: " + id + ".";
        String logMsg = "Solicitud para actualizar proveedor con ID: " + id + ".";
        logger.info(logMsgRequest);

        ProveedorDTO.Response updated = service.actualizar(id, dto);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updated.getId()).toUri();

        logger.info(logMsg + "-> actualizado");

        return ResponseEntity.status(200).location(location).body(assembler.toModel(updated));
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Se ha eliminado el registro de proveedor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProveedorDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado registro de proveedor",
                    content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    @Operation(summary = "Eliminar registro", description = "Borra registro de proveedor")
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        String logMsgRequest = "Recibiendo solicitud para borrar proveedor con ID: " + id + ".";
        String logMsg = "Solicitud para borrar proveedor con iD: " + id + ".";
        logger.info(logMsgRequest);

        if(service.eliminar(id)){
                logger.info(logMsg + "-> borrado");
                return ResponseEntity.noContent().build();
        }

        logger.info(logMsg + "-> no encontrado");
        return ResponseEntity.notFound().build();
    }
}
