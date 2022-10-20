package es.ujaen.dae.hotel.servicios;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;



public class ServicioHotelTest {

    @Autowired
    ServicioHotel servicioHotel;

    @Test
    public void testAccesoServicioHotel(){
        Assertions.assertThat(servicioHotel).isNotNull();
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testAltaClienteInvalido(){
        // Cliente con e-mail incorrecto!!!
        String clave = "manuel82";
        Direccion direccion = new Direccion(
                "España",
                "Jaen",
                "SanJuan",
                19);
        Cliente cliente = new Cliente(
                3,
                "12345678Q",
                "Manuel Jesus",
                "mjmp0027",
                clave,
                direccion,
                123456789,
                "mjmp0027.es"
        );

        Assertions.assertThatThrownBy(()->{
            servicioHotel.altaCliente(cliente);})
                .isInstanceOf(ConstraintViolationException.class);
    }


}
