package es.ujaen.dae.hotel.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Administrador {

    @NotBlank
    @Id
    private String userName;

    @NotBlank
    private String contraseña;
}