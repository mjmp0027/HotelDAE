package es.ujaen.dae.hotel.entidades;

import es.ujaen.dae.hotel.utils.ExprReg;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@RequiredArgsConstructor
@Entity
public class Cliente implements Serializable {

    @NotBlank
    @Size(min=9, max=9)
    @Pattern(regexp = ExprReg.DNI)
    private final String dni;

    @NotBlank
    private final String nombre;

    @Id
    @NotBlank
    private final String userName;

    @NotBlank
    private final String contraseña;

    @NotNull
    @Embedded
    private final Direccion direccion;

    @Pattern(regexp = ExprReg.TLF)
    private final String tlf;

    @Email
    private final String email;

    public boolean claveValida(String clave) {
        return contraseña.equals(clave);
    }

}