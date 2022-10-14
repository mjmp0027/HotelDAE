package es.ujaen.dae.hotel.entidades;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class Hotel {

    private int id;

    @NotBlank
    private String nombre;

    @NotBlank
    private Direccion direccion;

    @Positive
    private int numSimp;

    @Positive
    private int numDobl;

    private Reserva[] reservasActuales;

    private Reserva[] reservasPasadas;

    public Hotel(int id, String nombre, Direccion direccion, int numDobl, int numSimp) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.numSimp = numSimp;
        this.numDobl = numDobl;
    }

    //TODO
    public Reserva[] verTodasReservas(LocalDateTime fechaIni, LocalDateTime fechaFin) {
        return reservasActuales;
    }

}
