package es.ujaen.dae.hotel.entidades;

import es.ujaen.dae.hotel.utils.ExprReg;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
public class Cliente implements Serializable {

    @NotBlank
    @Size(min=9, max=9)
    @Pattern(regexp = ExprReg.DNI)
    private String dni;

    @NotBlank
    private String nombre;

    @Id
    @NotBlank
    private String userName;

    @NotBlank
    private String contraseña;

    @NotNull
    @Embedded
    private Direccion direccion;

    @Pattern(regexp = ExprReg.TLF)
    private String tlf;

    @Email
    private String email;

    public Cliente(String dni, String nombre, String userName, String contraseña, Direccion direccion, String tlf, String email){
        this.dni = dni;
        this.nombre = nombre;
        this.userName = userName;
        this.contraseña = contraseña;
        this.direccion = direccion;
        this.tlf = tlf;
        this.email = email;
    }

    public boolean claveValida(String clave) {
        return contraseña.equals(clave);
    }

}