package es.ujaen.dae.hotel.entidades;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

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

    private List<Reserva> reservas;

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

    public List<Reserva> verReservas(){
        return reservas;
    }

    public Reserva verReserva(int idReserva){
        return reservas.get(idReserva);
    }

    public boolean claveValida(String clave){
        if(contraseña.equals(clave))
            return true;
        return false;
    }

}
