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
public class ReservaPasada implements Serializable {

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

    @ManyToOne
    @JoinColumn(name = "cliente_idpasado")
    private Cliente cliente;

    public ReservaPasada(Reserva reservaPasada) {
        this.fechaInicio = reservaPasada.getFechaInicio();
        this.fechaFin = reservaPasada.getFechaFin();
        this.numHabitacionesSimp = reservaPasada.getNumHabitacionesSimp();
        this.numHabitacionesDobl = reservaPasada.getNumHabitacionesDobl();
        this.cliente = reservaPasada.getCliente();
    }

}