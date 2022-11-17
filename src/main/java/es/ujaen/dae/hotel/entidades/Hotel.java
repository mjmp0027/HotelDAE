package es.ujaen.dae.hotel.entidades;

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


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id_reservas_actuales")
    private List<Reserva> reservasActuales;
    private int totalReservasActuales = 0;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id_reservas_pasadas")
    private Set<Reserva> reservasPasadas;
    private int totalReservasPasadas = 0;

    public Hotel(String nombre, Direccion direccion, int numDobl, int numSimp) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.numSimp = numSimp;
        this.numDobl = numDobl;
        reservasActuales = new ArrayList<>();
        reservasPasadas = new HashSet<>();
    }

    public void addReserva(Reserva reserva) {
        reservasActuales.add(reserva);
    }


    //Trabajo Voluntario
    public void cambioReservas() {
        for (Reserva reservasActuale : reservasActuales) {
            if (reservasActuale.getFechaFin().isBefore(LocalDate.now()))
                reservasPasadas.add(reservasActuale);
        }
    }

    private boolean comprobarReservaDia(LocalDate dia, int numDobl, int numSimp) {
        int totalS = 0;
        int totalD = 0;

        for (Reserva reserva: reservasActuales) {
            if (reserva.contieneDia(dia)) {
                totalS += reserva.getNumHabitacionesSimp();
                totalD += reserva.getNumHabitacionesDobl();
            }
        }

        return (this.numSimp - totalS >= numSimp && this.numDobl - totalD >= numDobl);
    }

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