package es.ujaen.dae.hotel.entidades;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Direccion {
    private String pais;
    private String ciudad;
    private String calle;
    private int num;
}
