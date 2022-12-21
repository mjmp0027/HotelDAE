package es.ujaen.dae.hotel.rest;


import es.ujaen.dae.hotel.entidades.Administrador;
import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Direccion;
import es.ujaen.dae.hotel.entidades.Hotel;
import es.ujaen.dae.hotel.excepciones.AdministradorYaExiste;
import es.ujaen.dae.hotel.excepciones.ClienteNoRegistrado;
import es.ujaen.dae.hotel.rest.dto.DTOCliente;
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
import java.util.List;

@SpringBootTest(classes = es.ujaen.dae.hotel.HotelDaeApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"test"})
public class ControladorRESTTest {

    @LocalServerPort
    int localPort;

    @Autowired
    MappingJackson2HttpMessageConverter springBootJacksonConverter;

    TestRestTemplate restTemplate;

    @PostConstruct
    void crearRestTemplateBuilder() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + localPort + "/hoteldae")
                .additionalMessageConverters(List.of(springBootJacksonConverter));

        restTemplate = new TestRestTemplate(restTemplateBuilder);
    }

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

    @Test
    public void testAltaYLoginCliente() {
        String clave = "manuel82";
        Direccion direccion = new Direccion(

                "España",
                "Jaen",
                "SanJuan",
                19);
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

}
