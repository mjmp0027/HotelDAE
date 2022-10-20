package es.ujaen.dae.hotel.servicios;

import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Direccion;
import es.ujaen.dae.hotel.entidades.Hotel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import javax.validation.ConstraintViolationException;


@SpringBootTest(classes = es.ujaen.dae.hotel.HotelDaeApp.class)
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

        Assertions.assertThatThrownBy(()-> servicioHotel.altaCliente(cliente))
                .isInstanceOf(ConstraintViolationException.class);
    }
    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testAltaHotel() {
        Direccion direccion = new Direccion(
                "España",
                "Jaen",
                "SanJuan",
                19);
        Hotel hotel = new Hotel(
                2,
                "hotel",
                direccion,
                20,
                30
        );
        Assertions.assertThatThrownBy(() ->
        {
            servicioHotel.altaHotel(hotel);
        });
    }
    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testAltaYLoginCliente(){
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

        Cliente cliente= servicioHotel.altaCliente(cliente.getUserName(),cliente.getClave());
        Optional<Cliente>  clienteLogin = servicioHotel.loginCliente(cliente.getUserName(), clave);

        Assertions.assertThat(clienteLogin.isPresent()).isTrue();
        Assertions.assertThat(clienteLogin.get()).isEqualTo(cliente);



    }

}
