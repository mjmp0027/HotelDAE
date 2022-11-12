package es.ujaen.dae.hotel.servicios;

import es.ujaen.dae.hotel.entidades.*;
import es.ujaen.dae.hotel.excepciones.AdministradorYaExiste;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest(classes = es.ujaen.dae.hotel.HotelDaeApp.class)
public class ServicioHotelTest {

    @Autowired
    ServicioHotel servicioHotel;

    @Test
    public void testAccesoServicioHotel() {
        Assertions.assertThat(servicioHotel).isNotNull();
    }

    @Test
    public void testAltaClienteInvalido() {
        String clave = "manuel82";
        Direccion direccion = new Direccion(

                "España",
                "Jaen",
                "SanJuan",
                19);

        Cliente cliente = new Cliente(
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
    public void testAltaHotel() throws Exception, AdministradorYaExiste {
        Direccion direccion = new Direccion(

                "España",
                "Jaen",
                "SanJuan",
                19);
        Hotel hotel = new Hotel(
                "hotel",
                direccion,
                20,
                30
        );
        
        Administrador administrador = new Administrador("mjmp", "clave1");
        Administrador administrador1 = servicioHotel.altaAdministrador(administrador);
        Hotel hotel1 = servicioHotel.altaHotel(hotel, administrador1);
        Assertions.assertThat(hotel1).isNotNull();
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testAltaYLoginCliente() throws Exception {
        String clave = "manuel82";
        Direccion direccion = new Direccion(

                "España",
                "Jaen",
                "SanJuan",
                19);
        Cliente cliente = new Cliente(
                "12345678Q",
                "Manuel Jesus",
                "mjmp0027",
                clave,
                direccion,
                "657550655",
                "mjmp@0027.es"
        );

        Cliente cliente1 = servicioHotel.altaCliente(cliente);
        Cliente clienteLogin = servicioHotel.loginCliente(cliente.getUserName(), "manuel82")
                .orElseThrow(() -> new Exception("Cliente vacio"));

        Assertions.assertThat(clienteLogin).isNotNull();
        Assertions.assertThat(clienteLogin).isEqualTo(cliente1);

    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testBuscarHoteles() throws Exception, AdministradorYaExiste {

        String clave = "manuel82";
        Direccion direccionCliente = new Direccion(
                "España",
                "Malaga",
                "SanRafael",
                22);

        Cliente cliente = new Cliente(
                "12345678Q",
                "Manuel Jesus",
                "mjmp0027",
                clave,
                direccionCliente,
                "657550655",
                "mjmp0027@ujaen.es"
        );

        Direccion direccion1 = new Direccion(
                "España",
                "Jaen",
                "SanJuan",
                19);
        Direccion direccion2 = new Direccion(
                "España",
                "Jaen",
                "SanPablo",
                20);

        Hotel hotel1 = new Hotel(
                "hotel1",
                direccion1,
                20,
                30);
        Hotel hotel2 = new Hotel(
                "hotel2",
                direccion2,
                20,
                30);

        LocalDateTime fechaInicioReserva1 = LocalDateTime.of(2022, 10, 1, 10, 10, 10, 10);
        LocalDateTime fechaFinReserva1 = LocalDateTime.of(2022, 10, 10, 10 ,10, 10, 10);
        LocalDateTime fechaInicioReserva2 = LocalDateTime.of(2022, 10, 5, 10, 10 ,10 ,10);
        LocalDateTime fechaFinReserva2 = LocalDateTime.of(2022, 10, 15, 10, 10, 10, 10);
        LocalDateTime fechaInicioReserva3 = LocalDateTime.of(2022, 10, 3, 10, 10, 10, 10);
        LocalDateTime fechaFinReserva3 = LocalDateTime.of(2022, 10, 13, 10, 10, 10, 10);
        LocalDateTime fechaInicioReserva4 = LocalDateTime.of(2022, 10, 7, 10, 10, 10, 10);
        LocalDateTime fechaFinReserva4 = LocalDateTime.of(2022, 10, 17, 10, 10, 10, 10);

        LocalDateTime fechaInicioReservaPasada1 = LocalDateTime.of(2022, 1, 1, 10, 10, 10,0);
        LocalDateTime fechaFinReservaPasada1 = LocalDateTime.of(2022, 1, 2, 10, 10, 10, 10);
        LocalDateTime fechaInicioReservaPasada2 = LocalDateTime.of(2022, 1, 1, 10, 10, 10, 10);
        LocalDateTime fechaFinReservaPasada2 = LocalDateTime.of(2022, 1, 2, 10, 10, 10, 10);

        Reserva reserva1Hotel1 = new Reserva(fechaInicioReserva1, fechaFinReserva1, 1, 2, cliente);
        Reserva reserva2Hotel1 = new Reserva(fechaInicioReserva2, fechaFinReserva2, 1, 2, cliente);
        Reserva reserva3Hotel2 = new Reserva(fechaInicioReserva3, fechaFinReserva3, 1, 2, cliente);
        Reserva reserva4Hotel2 = new Reserva(fechaInicioReserva4, fechaFinReserva4, 1, 2, cliente);

        Reserva reservaPasada1 = new Reserva(fechaInicioReservaPasada1, fechaFinReservaPasada1, 1, 2, cliente);
        Reserva reservaPasada2 = new Reserva(fechaInicioReservaPasada2, fechaFinReservaPasada2, 1, 2, cliente);

        hotel1.addReserva(reserva1Hotel1);
        hotel1.addReserva(reserva2Hotel1);
        hotel2.addReserva(reserva3Hotel2);
        hotel2.addReserva(reserva4Hotel2);

        hotel1.addReservaPasada(reservaPasada1);
        hotel2.addReservaPasada(reservaPasada2);

        LocalDateTime fechaInicioReserva = LocalDateTime.of(2022, 10, 10, 10, 10, 10, 10);
        LocalDateTime fechaFinReserva = LocalDateTime.of(2022, 11, 11, 11, 11, 11, 11);
        LocalDateTime fechaInicioBuscar = LocalDateTime.of(2022, 10, 1, 10, 10, 10, 10);
        LocalDateTime fechaFinBuscar = LocalDateTime.of(2022, 10, 9, 11, 11, 11, 11);


        Administrador administrador = new Administrador("cgr", "clave2");
        servicioHotel.altaAdministrador(administrador);
        servicioHotel.altaHotel(hotel1, administrador);
        servicioHotel.altaHotel(hotel2, administrador);
        List<Hotel> listaHoteles = servicioHotel.buscarHoteles(direccion1, fechaInicioBuscar, fechaFinBuscar, 1, 2);
        Assertions.assertThat(listaHoteles).hasSize(1);
        servicioHotel.hacerReserva(cliente, fechaInicioReserva, fechaFinReserva, 1, 2, listaHoteles.get(0));

    }

    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testHacerReserva() throws Exception {
        Direccion direccionHotel = new Direccion(

                "España",
                "Jaen",
                "SanJuan",
                19);
        Hotel hotel = new Hotel(
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
                fechaInicioReserva,
                fechaFinReserva,
                1,
                2,
                cliente);

        Cliente altaCliente = servicioHotel.altaCliente(cliente);
        Cliente loginCliente = servicioHotel.loginCliente(altaCliente.getUserName(), "manuel82")
                .orElseThrow(() -> new Exception("Cliente vacio"));
        Administrador administrador = new Administrador("mhm", "clave3");
        Hotel hotel1 = servicioHotel.altaHotel(hotel, administrador);
        hotel1.addReserva(reserva);
        boolean reservaRealizada = servicioHotel.hacerReserva(loginCliente, fechaInicioBuscar, fechaFinBuscar, 2, 1, hotel1);
        Assertions.assertThat(reservaRealizada).isTrue();

    }
}