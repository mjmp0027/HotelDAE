package es.ujaen.dae.hotel.entidades;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String nombre;

    @Embedded
    @NotNull
    private Direccion direccion;

    @PositiveOrZero
    private int numSimp;

    @PositiveOrZero
    private int numDobl;


    @OneToMany
    @JoinColumn(name = "hotel_id_reservas_actuales")
    private List<Reserva> reservasActuales;
    private int totalReservasActuales = 0;

    @JoinColumn(name = "hotel_id_reservas_pasadas")
    @OneToMany
    private List<Reserva> reservasPasadas;
    private int totalReservasPasadas = 0;

    public Hotel(int id, String nombre, Direccion direccion, int numDobl, int numSimp) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.numSimp = numSimp;
        this.numDobl = numDobl;
        reservasActuales = new ArrayList<>();
        reservasPasadas = new ArrayList<>();
    }

    public void addReserva(Reserva reserva) {
        reservasActuales.add(reserva);
    }


    public void setNumDobl(int numDobl) {
        this.numDobl -= numDobl;
    }

    public void setNumSimp(int numSimp) {
        this.numSimp -= numSimp;
    }


    //Trabajo Voluntario
    public void cambioReservar() {
        for (Reserva reservasActuale : reservasActuales) {
            if (reservasActuale.getFechaFin().isBefore(LocalDateTime.now()))
                reservasPasadas.add(reservasActuale);
        }
    }

    private boolean comprobarReservaDia(LocalDateTime fechaIni, LocalDateTime fechaFin, int numDoble, int numSimp) {
        LocalDateTime siguiente = fechaIni;
        while (siguiente.isBefore(fechaFin)) {
            if (this.numDobl > numDoble && this.numSimp > numSimp) {
                return true;
            }
            siguiente = fechaIni.plusDays(1);
        }
        return false;
    }

    public boolean comprobarReserva(LocalDateTime fechaIni, LocalDateTime fechaFin, int numDobl, int numSimp) {
        if (comprobarReservaDia(fechaIni, fechaFin, numDobl, numSimp)) {
            for (int i = 0; i < reservasActuales.size(); i++) {
                if (fechaIni.isBefore(reservasActuales.get(i).getFechaInicio()) && fechaFin.isBefore(reservasActuales.get(i).getFechaInicio())
                        || fechaIni.isAfter(reservasActuales.get(i).getFechaFin()))
                    return true;
            }
        }
        return false;
    }
}