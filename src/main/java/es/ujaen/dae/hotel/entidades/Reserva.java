package es.ujaen.dae.hotel.entidades;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Reserva {

    @NotBlank
    private String destino;

    @NotBlank
    @DateTimeFormat
    private LocalDateTime fechaInicio;

    @NotBlank
    @DateTimeFormat
    private LocalDateTime fechaFin;

    @Positive
    private int numHabitacionesSimp;

    @Positive
    private int numHabitacionesDobl;
}
