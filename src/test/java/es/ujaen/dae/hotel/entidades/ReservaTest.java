package es.ujaen.dae.hotel.entidades;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

public class ReservaTest {
    public ReservaTest() {

    }

    @Test
    void TestValidacionReserva() {
        String clave = "manuel82";
        Direccion direccion = new Direccion(

                "España",
                "Jaen",
                "SanJuan",
                19);

        Cliente cliente = new Cliente(
                "12345678Q",
                "Manuel Jesu ºs",
                "mjmp0027",
                clave,
                direccion,
                "657550655",
                "mjmp0027@ujaen.es"
        );

        LocalDate fechaInicio = LocalDate.of(2022, 10, 10);
        LocalDate fechaFin = LocalDate.of(2022, 11, 11);
        Reserva reserva = new Reserva(
                fechaInicio,
                fechaFin,
                1,
                2,
                cliente);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Reserva>> violations = validator.validate(reserva);

        Assertions.assertThat(violations).isEmpty();
    }
}