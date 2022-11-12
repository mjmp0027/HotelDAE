package es.ujaen.dae.hotel.entidades;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany
    @JoinColumn(name = "hotel_id_reservas_pasadas")
    private List<Reserva> reservasPasadas;
    private int totalReservasPasadas = 0;

    public Hotel(String nombre, Direccion direccion, int numDobl, int numSimp) {
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

    public void addReservaPasada(Reserva reserva){
        reservasPasadas.add(reserva);
    }

    //Trabajo Voluntario
    public void cambioReservar() {
        for (Reserva reservasActuale : reservasActuales) {
            if (reservasActuale.getFechaFin().isBefore(LocalDateTime.now()))
                reservasPasadas.add(reservasActuale);
        }
    }

    private boolean comprobarReservaDia(LocalDateTime dia, int numDobl, int numSimp) {
        int totalS = 0;
        int totalD = 0;
        //Compruebo las reservas que coinciden con ese día
        for (int i = 0; i < reservasActuales.size(); i++) {
            if (dia.isAfter(reservasActuales.get(i).getFechaInicio())
                    && dia.isBefore(reservasActuales.get(i).getFechaFin())
                    || dia.isEqual(reservasActuales.get(i).getFechaInicio())
                    || dia.isEqual(reservasActuales.get(i).getFechaFin())) {
                totalS += reservasActuales.get(i).getNumHabitacionesSimp();
                totalD += reservasActuales.get(i).getNumHabitacionesDobl();
            }
        }
        if (this.numSimp - totalS >= numSimp && this.numDobl - totalD >= numDobl)
            return true;
        return false;
    }

    public boolean comprobarReserva(LocalDateTime fechaIni, LocalDateTime fechaFin, int numDobl, int numSimp) {
        LocalDateTime dia = fechaIni;
        boolean reservaDisponible = true;

        //Comprobar las fechas
        for (int i = 0; i < reservasActuales.size(); i++) {
            if (!(fechaIni.isBefore(reservasActuales.get(i).getFechaInicio()) && fechaFin.isBefore(reservasActuales.get(i).getFechaInicio())
                    || fechaIni.isAfter(reservasActuales.get(i).getFechaFin()))) {
                    reservaDisponible = false;
                    break;
            }
        }
        //Comprobar las habitaciones
        while (dia.isBefore(fechaFin)) {
            if (!comprobarReservaDia(dia, numDobl, numSimp)) {
                    reservaDisponible = false;
                    break;
            }
            dia.plusDays(1);
        }
        //En el momento que alguna de las comprobaciones devuelva falso reservaDisponible se pone falso
        //y no se vuele a poner a true, por lo que el método devolvería falso.
        //Si los dos metodos devuelven true, reservaDisponible se mantiene en true
        return reservaDisponible;
    }
}