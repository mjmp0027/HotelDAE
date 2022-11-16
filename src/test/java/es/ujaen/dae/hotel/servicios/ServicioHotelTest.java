package es.ujaen.dae.hotel.servicios;

import es.ujaen.dae.hotel.entidades.Administrador;
import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Direccion;
import es.ujaen.dae.hotel.entidades.Hotel;
import es.ujaen.dae.hotel.excepciones.AdministradorYaExiste;
import es.ujaen.dae.hotel.excepciones.ClienteNoRegistrado;
import es.ujaen.dae.hotel.excepciones.ReservaNoDisponible;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

/**
 * Test de integración de la aplicación
 */
@SpringBootTest(classes = es.ujaen.dae.hotel.HotelDaeApp.class)
public class ServicioHotelTest {

    @Autowired
    ServicioHotel servicioHotel;

    //Accedemos al sistema
    @Test
    public void testAccesoServicioHotel() {
        Assertions.assertThat(servicioHotel).isNotNull();
    }


    //Identificación de errores
    @Test
    public void testAltaClienteInvalido() {
        // Cliente con e-mail incorrecto!!!
        String clave = "manuel82";
        Direccion direccion = new Direccion(

                "España",
                "Jaen",
                "SanJuan",
                19);

        //Registramos cliente
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

    //Damos de alta hotel y su dirección
    @Test
    public void testAltaHotel() throws AdministradorYaExiste {
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

        //Creamos administrador
        Administrador administrador = new Administrador("mjmp", "clave1");
        Administrador administrador1 = servicioHotel.altaAdministrador(administrador);
        Hotel hotel1 = servicioHotel.altaHotel(hotel, administrador1);
        Assertions.assertThat(hotel1).isNotNull();
    }

    //Damos de alta al cliente y queda registrado en el sistema
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


    // Buscamos el hotel deseado en las fechas deseadas y comprobamos disponibilidad
    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testBuscarHoteles() throws AdministradorYaExiste {


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


        LocalDate fechaInicioBuscar = LocalDate.of(2022, 10, 1);
        LocalDate fechaFinBuscar = LocalDate.of(2022, 10, 9);


        Administrador administrador = new Administrador("cgr", "clave2");
        servicioHotel.altaAdministrador(administrador);
        servicioHotel.altaHotel(hotel1, administrador);
        servicioHotel.altaHotel(hotel2, administrador);

        List<Hotel> listaHoteles = servicioHotel.buscarHoteles(direccion1, fechaInicioBuscar, fechaFinBuscar, 1, 2);
        Assertions.assertThat(listaHoteles).hasSize(1);

    }

    //Test para la comprobacion de que falla si no hay habitaciones o no hay ningun hotel disponible en esa direccion
    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testNoHayHotelesDisp() throws AdministradorYaExiste {
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
                21);

        Hotel hotel1 = new Hotel(
                "hotel1",
                direccion1,
                10,
                15);

        LocalDate fechaInicioBuscar = LocalDate.of(2022, 10, 1);
        LocalDate fechaFinBuscar = LocalDate.of(2022, 10, 9);


        Administrador administrador = new Administrador("cgr", "clave2");
        servicioHotel.altaAdministrador(administrador);
        servicioHotel.altaHotel(hotel1, administrador);
        Cliente altaCliente = servicioHotel.altaCliente(cliente);
        Cliente loginCliente = servicioHotel.loginCliente(altaCliente.getUserName(), clave).orElseThrow(() -> new ClienteNoRegistrado());

        List<Hotel> listaHoteles1 = servicioHotel.buscarHoteles(direccion1, fechaInicioBuscar, fechaFinBuscar, 8, 12);
        Assertions.assertThat(listaHoteles1).hasSize(1);
        //Realizamos una reserva donde quitamos casi todas las habitaciones para facilitar
        servicioHotel.hacerReserva(loginCliente, fechaInicioBuscar, fechaFinBuscar, 8, 12, listaHoteles1.get(0));
//        List<Hotel> listaHoteles2 = servicioHotel.buscarHoteles(direccion1, fechaInicioBuscar, fechaFinBuscar, 1, 1);
//        Assertions.assertThat(listaHoteles2).hasSize(1);
        //Intentamos buscar hoteles pero esta vez no habrá habitaciones disponibles
        Assertions.assertThatExceptionOfType(ReservaNoDisponible.class).isThrownBy(() -> servicioHotel.buscarHoteles(direccion1, fechaInicioBuscar, fechaFinBuscar, 5, 5));
        //Intentamos buscar hoteles en una direccion donde no hay ningún hotel, aunque si quedan habitaciones
        Assertions.assertThatExceptionOfType(ReservaNoDisponible.class).isThrownBy(() -> servicioHotel.buscarHoteles(direccion2, fechaInicioBuscar, fechaFinBuscar, 1, 2));

    }

    //Realizamos la reserva del cliente
    @Test
    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    public void testHacerReserva() throws Exception, AdministradorYaExiste {
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

        //Fechas de inicio y fin de la reserva
        LocalDate fechaInicioReserva = LocalDate.of(2022, 10, 1);
        LocalDate fechaFinReserva = LocalDate.of(2022, 10, 9);


        Cliente altaCliente = servicioHotel.altaCliente(cliente);
        Cliente loginCliente = servicioHotel.loginCliente(altaCliente.getUserName(), "manuel82")
                .orElseThrow(() -> new Exception("Cliente vacio"));
        Administrador administrador1 = new Administrador("cgr00064", "clave1");
        Administrador administrador10 = servicioHotel.altaAdministrador(administrador1);
        servicioHotel.altaHotel(hotel, administrador10);
        List<Hotel> listaHoteles = servicioHotel.buscarHoteles(direccionHotel, fechaInicioReserva, fechaFinReserva, 1, 2);
        boolean reservaRealizada = servicioHotel.hacerReserva(loginCliente, fechaInicioReserva, fechaFinReserva, 1, 2, listaHoteles.get(0));
        Assertions.assertThat(reservaRealizada).isTrue();

    }
}