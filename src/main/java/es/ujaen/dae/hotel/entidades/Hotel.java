package es.ujaen.dae.hotel.entidades;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Hotel {

    private int id;

    @NotNull
    private String nombre;

    @NotNull
    private Direccion direccion;

    @PositiveOrZero
    private int numSimp;

    @PositiveOrZero
    private int numDobl;

    private Reserva[] reservasActuales;

    private Reserva[] reservasPasadas;

    public Reserva[] verTodasReservas(LocalDateTime fechaIni, LocalDateTime fechaFin){
        return reservasActuales;
    }

}
