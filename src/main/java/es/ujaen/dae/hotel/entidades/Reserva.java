package es.ujaen.dae.hotel.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Reserva {

    int id;

    @NotNull
    private Direccion direccion;

    @DateTimeFormat
    private LocalDateTime fechaInicio;

    @DateTimeFormat
    private LocalDateTime fechaFin;

    @PositiveOrZero
    private int numHabitacionesSimp;

    @PositiveOrZero
    private int numHabitacionesDobl;
}
