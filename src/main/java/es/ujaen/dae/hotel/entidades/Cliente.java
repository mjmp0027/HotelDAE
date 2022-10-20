package es.ujaen.dae.hotel.entidades;

import es.ujaen.dae.hotel.utils.ExprReg;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cliente {

    private int id;

    @NotBlank
    @Pattern(regexp = ExprReg.DNI)
    private String dni;

    @NotBlank
    private String nombre;

    @NotBlank
    private String userName;

    @NotBlank
    private String contraseña;

    private Direccion direccion;

    @Pattern(regexp = ExprReg.TLF)
    private String tlf;

    @Email
    private String email;

    private List<Reserva> reservas;

    public Cliente(int id, String dni, String nombre, String userName, String contraseña, Direccion direccion, String tlf, String email){
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.userName = userName;
        this.contraseña = contraseña;
        this.direccion = direccion;
        this.tlf = tlf;
        this.email = email;
        reservas = new ArrayList<>();
    }

    public List<Reserva> verReservas(){
        return reservas;
    }

    public Reserva verReserva(int idReserva){
        return reservas.get(idReserva);
    }

    public boolean claveValida(String clave){
        return contraseña.equals(clave);
    }

    public void addReserva(Reserva reserva){
        reservas.add(reserva);
    }


}
