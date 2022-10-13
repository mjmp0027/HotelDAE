package es.ujaen.dae.hotel.entidades;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class Hotel {

    private int id;

    @NotNull
    private String nombre;

    @NotNull
    private String direccion;
}
