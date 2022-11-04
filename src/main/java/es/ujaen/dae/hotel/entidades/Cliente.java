package es.ujaen.dae.hotel.entidades;

import es.ujaen.dae.hotel.utils.ExprReg;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
public class Cliente implements Serializable {

    @Id
    @NotNull
    private int id;

    @NotBlank
    @Size(min=9, max=9)
    @Pattern(regexp = ExprReg.DNI)
    private final String dni;

    @NotBlank
    private final String nombre;

    @NotBlank
    private final String userName;

    @NotBlank
    private final String contraseña;

    @NotNull
    @OneToOne
    private final Direccion direccion;

    @Pattern(regexp = ExprReg.TLF)
    private final String tlf;

    @Email
    private final String email;

    @JoinColumn(name="id")
    @OneToMany
    private List<Reserva> reservas = new ArrayList<>();
    private int totalReservas = 0;


    public List<Reserva> verReservas() {
        return Collections.unmodifiableList(reservas);
    }

    public Reserva verReserva(int idReserva) {
        return reservas.get(idReserva);
    }

    public boolean claveValida(String clave) {
        return contraseña.equals(clave);
    }

    public void addReserva(Reserva reserva) {
        reserva.setId(totalReservas++);
        reservas.add(reserva);
    }
}