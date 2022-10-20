package es.ujaen.dae.hotel.entidades;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Hotel {

    private int id;

    @NotBlank
    private String nombre;


    private Direccion direccion;

    @Positive
    private int numSimp;

    @Positive
    private int numDobl;

    private List<Reserva> reservasActuales;

    private List<Reserva> reservasPasadas;

    public Hotel(int id, String nombre, Direccion direccion, int numDobl, int numSimp) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.numSimp = numSimp;
        this.numDobl = numDobl;
    }


    public List<Reserva> verReservas(LocalDateTime fechaIni, LocalDateTime fechaFin) {
        List<Reserva> reservas = new ArrayList<>();
        for (Reserva reservasActuales : reservasActuales) {
            if (reservasActuales.getFechaInicio() == fechaIni && reservasActuales.getFechaFin() == fechaFin)
                reservas.add(reservasActuales);
        }
        return reservas;
    }
}
