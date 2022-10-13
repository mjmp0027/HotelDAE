package es.ujaen.dae.hotel.entidades;

import es.ujaen.dae.hotel.utils.CodificadorPassword;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Cliente {

    private int id;

    @NotNull
    private String dni;

    @NotNull
    private String nombre;

    @NotNull
    private String userName;

    @NotNull
    private String contraseña;

    private Direccion direccion;

    private int tlf;

    @Email
    private String email;

    private Reserva[] reservas;


    public Reserva[] verReservas(){
        return reservas;
    }
    public Reserva verReserva(int idReserva){
        return reservas[idReserva];
    }

    public Hotel[] buscarHoteles(Direccion direccion, LocalDateTime fechaIni, LocalDateTime fechaFin){
        Hotel[] hoteles = new Hotel[10];
        return hoteles;
    }

}
