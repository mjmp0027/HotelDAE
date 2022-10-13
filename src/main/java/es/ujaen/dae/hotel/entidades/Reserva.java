package es.ujaen.dae.hotel.entidades;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Reserva {

    @NotNull
    private String destino;

    @NotNull
    @DateTimeFormat
    private LocalDateTime fechaInicio;

    @NotNull
    @DateTimeFormat
    private LocalDateTime fechaFin;

    @NotNull
    private int numHabitacionesSimp;

    @NotNull
    private int numHabitacionesDobl;
}
