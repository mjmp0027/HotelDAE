package es.ujaen.dae.hotel.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class Reserva implements Serializable {

    @Id
    int id;

    @NotNull
    private final Direccion direccion;

    @DateTimeFormat
    private final LocalDateTime fechaInicio;

    @DateTimeFormat
    private final LocalDateTime fechaFin;

    @PositiveOrZero
    private final int numHabitacionesSimp;

    @PositiveOrZero
    private final int numHabitacionesDobl;
}