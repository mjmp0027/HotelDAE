package es.ujaen.dae.hotel.rest;


import es.ujaen.dae.hotel.entidades.Administrador;
import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Direccion;
import es.ujaen.dae.hotel.entidades.Hotel;
import es.ujaen.dae.hotel.excepciones.AdministradorYaExiste;
import es.ujaen.dae.hotel.excepciones.ClienteNoRegistrado;
import es.ujaen.dae.hotel.rest.dto.DTOCliente;
import es.ujaen.dae.hotel.rest.dto.DTOHotel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
/**
 * Test para controlador REST de clientes
 *

 */

@SpringBootTest(classes = es.ujaen.dae.hotel.HotelDaeApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"test"})
public class ControladorRESTTest {

    @LocalServerPort
    int localPort;

    @Autowired
    MappingJackson2HttpMessageConverter springBootJacksonConverter;

    TestRestTemplate restTemplate;
    /**
     * Crear un TestRestTemplate para las pruebas
     */

    @PostConstruct
    void crearRestTemplateBuilder() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + localPort + "/hoteldae")
                .additionalMessageConverters(List.of(springBootJacksonConverter));

        restTemplate = new TestRestTemplate(restTemplateBuilder);
    }
    /**
     * Intento de creación de un cliente inválido
     */

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
        DTOCliente cliente = new DTOCliente(
                "12345678Q",
                "Manuel Jesus",
                "mjmp0027",
                clave,
                direccion,
                "657550655",
                "mjmp0027gmail.com"
        );
        ResponseEntity<DTOCliente> respuestaLogin = restTemplate
                .withBasicAuth(cliente.userName(), cliente.contraseña())
                .getForEntity(
                        "/clientes/{userName}",
                        DTOCliente.class,
                        cliente.userName()
                );
                Assertions.assertThat(respuestaLogin.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    /**
     * test de alta y login cliente
     */

    @Test
    public void testAltaYLoginCliente() {
        String clave = "manuel82";
        Direccion direccion = new Direccion(

                "España",
                "Jaen",
                "SanJuan",
                19);
        // Crear segundo cliente
        DTOCliente cliente = new DTOCliente(
                "12345678Q",
                "Manuel Jesus",
                "mjmp0027",
                clave,
                direccion,
                "657550655",
                "mjmp@0027.es"
        );
        ResponseEntity<DTOCliente> respuestaLogin = restTemplate
                .withBasicAuth(cliente.userName(), cliente.contraseña())
                .getForEntity(
                        "/clientes/{userName}",
                        DTOCliente.class,
                        cliente.userName()
                );
        Assertions.assertThat(respuestaLogin.getStatusCode()).isEqualTo(HttpStatus.OK);
        DTOCliente clienteLogin = respuestaLogin.getBody();
        Assertions.assertThat(clienteLogin.userName()).isEqualTo(cliente.userName());

    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testBuscarHoteles(){
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

        DTOHotel hotel1 = new DTOHotel(
                1,
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
        DTOHotel DTOHotel = restTemplate.postForEntity(
                "/hoteles",
                hotel1,
                es.ujaen.dae.hotel.rest.dto.DTOHotel.class).getBody();

        Assertions.assertThat(DTOHotel.nombre()).isEqualTo(hotel1.nombre());
    }

}
