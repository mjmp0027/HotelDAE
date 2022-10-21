package es.ujaen.dae.hotel.servicios;

import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Direccion;
import es.ujaen.dae.hotel.entidades.Hotel;
import es.ujaen.dae.hotel.entidades.Reserva;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@SpringBootTest(classes = es.ujaen.dae.hotel.HotelDaeApp.class)
public class ServicioHotelTest {

    @Autowired
    ServicioHotel servicioHotel;

    @Test
    public void testAccesoServicioHotel() {
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
                "657550655",
                "mjmp0027gmail.com"
        );

        Assertions.assertThatThrownBy(() -> servicioHotel.altaCliente(cliente))
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
        Hotel hotel1 = servicioHotel.altaHotel(hotel);
        Assertions.assertThat(hotel1).isNotNull();
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testAltaYLoginCliente() {
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
                "657550655",
                "mjmp@0027.es"
        );

        Cliente cliente1 = servicioHotel.altaCliente(cliente);
        Cliente clienteLogin = servicioHotel.loginCliente(cliente.getUserName(), "manuel82");

        Assertions.assertThat(clienteLogin).isNotNull();
        Assertions.assertThat(clienteLogin).isEqualTo(cliente1);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testBuscarHoteles() {
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
        LocalDateTime fechaInicioReserva = LocalDateTime.of(2022, 10, 10, 10, 10, 10, 10);
        LocalDateTime fechaFinReserva = LocalDateTime.of(2022, 11, 11, 11, 11, 11, 11);
        LocalDateTime fechaInicioBuscar = LocalDateTime.of(2022, 10, 1, 10, 10, 10, 10);
        LocalDateTime fechaFinBuscar = LocalDateTime.of(2022, 10, 9, 11, 11, 11, 11);

        Reserva reserva = new Reserva(
                1,
                direccion,
                fechaInicioReserva,
                fechaFinReserva,
                1,
                2);
        Hotel hotel1 = servicioHotel.altaHotel(hotel);
        hotel1.addReserva(reserva);
        List<Hotel> listaHoteles = servicioHotel.buscarHoteles(direccion, fechaInicioBuscar, fechaFinBuscar);

        Assertions.assertThat(listaHoteles).hasSize(1);
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testHacerReserva() {
        Direccion direccionHotel = new Direccion(
                "España",
                "Jaen",
                "SanJuan",
                19);
        Hotel hotel = new Hotel(
                2,
                "hotel",
                direccionHotel,
                20,
                30
        );

        String clave = "manuel82";
        Direccion direccionCliente = new Direccion(
                "España",
                "Malaga",
                "SanJuan",
                19);

        Cliente cliente = new Cliente(
                3,
                "12345678Q",
                "Manuel Jesus",
                "mjmp0027",
                clave,
                direccionCliente,
                "657550655",
                "mjmp0027@ujaen.es"
        );

        LocalDateTime fechaInicioReserva = LocalDateTime.of(2022, 10, 10, 10, 10, 10, 10);
        LocalDateTime fechaFinReserva = LocalDateTime.of(2022, 11, 11, 11, 11, 11, 11);
        LocalDateTime fechaInicioBuscar = LocalDateTime.of(2022, 10, 1, 10, 10, 10, 10);
        LocalDateTime fechaFinBuscar = LocalDateTime.of(2022, 10, 9, 11, 11, 11, 11);
        Reserva reserva = new Reserva(
                1,
                direccionHotel,
                fechaInicioReserva,
                fechaFinReserva,
                1,
                2);
        Cliente altaCliente = servicioHotel.altaCliente(cliente);
        Cliente loginCliente = servicioHotel.loginCliente(altaCliente.getUserName(), "manuel82");
        Hotel hotel1 = servicioHotel.altaHotel(hotel);
        hotel1.addReserva(reserva);
        boolean reservaRealizada = servicioHotel.hacerReserva(loginCliente, direccionHotel, fechaInicioBuscar, fechaFinBuscar, 2, 1);

        Assertions.assertThat(reservaRealizada).isTrue();

    }


}
