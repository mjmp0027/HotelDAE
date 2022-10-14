package es.ujaen.dae.hotel.entidades;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class Cliente {

    private int id;

    @NotBlank
    private String dni;

    @NotBlank
    private String nombre;

    @NotBlank
    private String userName;

    @NotBlank
    private String contraseña;

    private Direccion direccion;

    private int tlf;

    @Email
    @NotBlank
    private String email;

    private Reserva[] reservas;

    public Cliente(int id, String dni, String nombre, String userName, String contraseña, Direccion direccion, int tlf, String email){
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.userName = userName;
        this.contraseña = contraseña;
        this.direccion = direccion;
        this.tlf = tlf;
        this.email = email;
    }

    public Reserva[] verReservas(){
        return reservas;
    }

    public Reserva verReserva(int idReserva){
        return reservas[idReserva];
    }

    public boolean claveValida(String clave){
        if(contraseña.equals(clave))
            return true;
        return false;
    }

}
