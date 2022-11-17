package es.ujaen.dae.hotel.entidades;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class Reserva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @DateTimeFormat
    private LocalDate fechaInicio;

    @DateTimeFormat
    private LocalDate fechaFin;

    @PositiveOrZero
    private int numHabitacionesSimp;

    @PositiveOrZero
    private int numHabitacionesDobl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_ID")
    private Cliente cliente;

    public Reserva(LocalDate fechaInicio, LocalDate fechaFin, int numHabitacionesSimp, int numHabitacionesDobl, Cliente cliente) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numHabitacionesSimp = numHabitacionesSimp;
        this.numHabitacionesDobl = numHabitacionesDobl;
        this.cliente = cliente;
    }
    public boolean contieneDia(LocalDate dia) {
        return dia.isEqual(fechaInicio) || (dia.isAfter(fechaInicio) && dia.isBefore(fechaFin));
    }
}