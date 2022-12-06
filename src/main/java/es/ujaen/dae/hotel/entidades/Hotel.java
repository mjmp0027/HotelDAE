package es.ujaen.dae.hotel.entidades;

import es.ujaen.dae.hotel.excepciones.ReservaNoDisponible;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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



    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Reserva> reservasActuales;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Reserva> reservasPasadas;

    public Hotel(String nombre, Direccion direccion, int numDobl, int numSimp) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.numSimp = numSimp;
        this.numDobl = numDobl;
        reservasActuales = new ArrayList<>();
        reservasPasadas = new HashSet<>();
    }



    public Hotel(int id, String nombre, Direccion direccion, int numSimp, int numDobl) {}

    public void addReserva(Reserva reserva) {
        if(comprobarReserva(reserva.getFechaInicio(), reserva.getFechaFin(), reserva.getNumHabitacionesDobl(), reserva.getNumHabitacionesSimp())) {
            reservasActuales.add(reserva);
        }else{
            throw new ReservaNoDisponible();
        }
    }


    //Trabajo Voluntario
    public void cambioReservas() {
        List<Reserva> reservasActu = new ArrayList<>();
        for (Reserva reservaActual : reservasActuales){
            if (reservaActual.getFechaFin().isBefore(LocalDate.now())) {
                reservasPasadas.add(reservaActual);
                reservasActu.add(reservaActual);
            }
        }
        reservasActuales.removeAll(reservasActu);
    }


    //Comprobamos la reserva por día
    private boolean comprobarReservaDia(LocalDate dia, int numDobl, int numSimp) {
        int totalS = 0;
        int totalD = 0;

        for (Reserva reserva : reservasActuales) {
            if (reserva.contieneDia(dia)) {
                totalS += reserva.getNumHabitacionesSimp();
                totalD += reserva.getNumHabitacionesDobl();
            }
        }

        return (this.numSimp - totalS >= numSimp && this.numDobl - totalD >= numDobl);
    }

    //Comprobamos reserva
    public boolean comprobarReserva(LocalDate fechaIni, LocalDate fechaFin, int numDobl, int numSimp) {
        LocalDate dia = fechaIni;
        boolean reservaDisponible = true;

        //Comprobar las habitaciones
        while (dia.isBefore(fechaFin)) {
            if (!comprobarReservaDia(dia, numDobl, numSimp)) {
                reservaDisponible = false;
                break;
            }
            dia = dia.plusDays(1);
        }
        //En el momento que alguna de las comprobaciones devuelva falso reservaDisponible se pone falso
        //y no se vuele a poner a true, por lo que el método devolvería falso.
        //Si el metodo devuelve true, reservaDisponible se mantiene en true
        return reservaDisponible;
    }
}