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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public CollectionModel<EntityModel<ProductoResponseDTO>> listar() {

        List<EntityModel<ProductoResponseDTO>> listadoDTOs = service.listar()
                                                                    .stream()
                                                                    .map(assembler::toModel)
                                                                    .collect(Collectors.toList());

        return CollectionModel.of(listadoDTOs, 
            linkTo(methodOn(ProductoControllerV2.class).listar()).withSelfRel());
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
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtener(@PathVariable Long id) {
        String logMsgRequest = "Recibiendo solicitud para buscar producto por ID: " + id + ".";
        String logMsg = "Solicitud para buscar producto por ID: " + id + ".";
        logger.info(logMsgRequest);
        ProductoResponseDTO dto = service.obtener(id);
        if (dto != null){
            logger.info(logMsg + "=> encontrado.");
            return ResponseEntity.ok(dto);
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
    @GetMapping("/exists-by-id/{id}")
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
    @PostMapping
    public ProductoResponseDTO crear(@Valid @RequestBody ProductoInputDTO dto) {
        return service.guardar(dto);
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
    @PutMapping("/{id}")
    public ProductoResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProductoInputDTO dto) {
        return service.actualizar(
                        id,
                        ProductoMapper.toEntity(dto),
                        dto.getProveedorId()
                );
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
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Producto eliminado";
    }
}
