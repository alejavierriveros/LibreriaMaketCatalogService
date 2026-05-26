package com.example.libreriaMarketCatalogService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "proveedores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(message = "Debe ingresar un nombre de proveedor de mínimo 2 caracteres y máximo 20.", min = 2 , max = 20)
    private String nombre;

    @NotBlank
    @Email(message = "Debe ingresar una dirección con formato de correo electrónico con @ + domino")
    private String contacto;

    @OneToMany(mappedBy = "proveedor")
    @JsonIgnore
    private List<Producto> productos;
}