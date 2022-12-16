package es.ujaen.dae.hotel.rest;


import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Hotel;
import es.ujaen.dae.hotel.excepciones.ClienteNoRegistrado;
import es.ujaen.dae.hotel.excepciones.ClienteYaRegistrado;
import es.ujaen.dae.hotel.excepciones.ReservaNoDisponible;
import es.ujaen.dae.hotel.rest.dto.DTOAdministrador;
import es.ujaen.dae.hotel.rest.dto.DTOCliente;
import es.ujaen.dae.hotel.rest.dto.DTOHotel;
import es.ujaen.dae.hotel.servicios.ServicioHotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hoteldae")
public class ControladorREST {

    @Autowired
    ServicioHotel servicio;

    /**
     * Handler para excepciones de violación de restricciones
     *
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerViolationRestricciones(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Handler para excepciones de accesos de usuarios no registrados
     */
    @ExceptionHandler(ClienteNoRegistrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlerClienteNoRegistrado(ClienteNoRegistrado e) {
    }

    /**
     * Creación de clientes
     */
    @PostMapping("/clientes")
    public ResponseEntity<DTOCliente> altaCliente(@RequestBody DTOCliente cliente) {
        try {
            Cliente cliente1 = servicio.altaCliente(cliente.aCliente());
            return ResponseEntity.status(HttpStatus.CREATED).body(new DTOCliente(cliente1));
        } catch (ClienteYaRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Login de clientes (temporal hasta incluir autenticación mediante Spring
     * Security
     */
    @GetMapping("/clientes/{userName}")
    public ResponseEntity<DTOCliente> verCliente(@PathVariable String userName) {
        Optional<Cliente> cliente = servicio.verCliente(userName);
        return cliente
                .map(c -> ResponseEntity.ok(new DTOCliente(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DTOHotel> altaHotel(@RequestBody DTOHotel hotel, @RequestBody DTOAdministrador administrador) {
        try {
            Hotel hotel1 = servicio.altaHotel(hotel.aHotel(), administrador.aAdministrador());
            return ResponseEntity.status(HttpStatus.CREATED).body(new DTOHotel(hotel1));
        } catch (ClienteYaRegistrado e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping
    ResponseEntity<List<DTOHotel>> buscarHoteles(String ciudad, LocalDate fechaIni, LocalDate fechaFin, int numDoble, int numSimple) {
        try {
            List<Hotel> listHoteles = servicio.buscarHoteles(ciudad, fechaIni, fechaFin, numDoble, numSimple);
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        } catch (ReservaNoDisponible e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/clientes/{dni}")
    ResponseEntity<Boolean> hacerReserva(@RequestBody DTOCliente cliente, LocalDate fechaIni, LocalDate fechaFin, int numDoble, int numSimple, @RequestBody DTOHotel hotel) {
        try {
            servicio.hacerReserva(cliente.aCliente(), fechaIni, fechaFin, numDoble, numSimple, hotel.aHotel());
        } catch (ReservaNoDisponible e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
