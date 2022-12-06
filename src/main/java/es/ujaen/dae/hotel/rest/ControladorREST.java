package es.ujaen.dae.hotel.rest;


import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Hotel;
import es.ujaen.dae.hotel.excepciones.ClienteNoRegistrado;
import es.ujaen.dae.hotel.excepciones.ClienteYaRegistrado;
import es.ujaen.dae.hotel.rest.dto.DTOCliente;
import es.ujaen.dae.hotel.rest.dto.DTOHotel;
import es.ujaen.dae.hotel.servicios.ServicioHotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@RestController
@RequestMapping("/hoteldae")
public class ControladorREST {

    @Autowired
    ServicioHotel servicios;

    /**
     * Handler para excepciones de violación de restricciones
     *
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerViolationRestricciones(ConstraintViolationException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Handler para excepciones de accesos de usuarios no registrados
     */
    @ExceptionHandler(ClienteNoRegistrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlerClienteNoRegistrado(ClienteNoRegistrado e){
    }

    /**
     * Creación de clientes
     */
    //FIXME
    //En el codigo del profesor lo hace DTOCuenta pero me suena que no era necesario crear el DTOReserva
    /*@PostMapping("/clientes")
    ResponseEntity<DTOHotel> altaCliente(@RequestBody DTOCliente cliente){
        try {
            Hotel hotel = servicios.altaCliente(cliente.aCliente());
            return ResponseEntity.status(HttpStatus.CREATED).body(new DTOHotel(hotel));
        } catch (ClienteYaRegistrado e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }*/
    /**
     * Login de clientes (temporal hasta incluir autenticación mediante Spring
     * Security
     */
    @GetMapping("/clientes/{userName}")
    ResponseEntity<DTOCliente> verCliente(@PathVariable String userName) {
        Optional<Cliente> cliente = servicios.verCliente(userName);
        return cliente
                .map(c -> ResponseEntity.ok(new DTOCliente(c)))
                .orElse(ResponseEntity.notFound().build());
    }




}
