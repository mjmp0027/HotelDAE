package es.ujaen.dae.hotel.servicios;

import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Direccion;
import es.ujaen.dae.hotel.entidades.Hotel;
import es.ujaen.dae.hotel.entidades.Reserva;
import es.ujaen.dae.hotel.excepciones.ClienteNoRegistrado;
import es.ujaen.dae.hotel.excepciones.ClienteYaRegistrado;
import es.ujaen.dae.hotel.excepciones.HotelYaExiste;
import es.ujaen.dae.hotel.excepciones.ReservaNoDisponible;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
@Validated
public class ServicioHotel {
    Map<String, Cliente> clientes;
    Map<String, Hotel> hoteles;
    private int numClientes;
    private int numHoteles;

    @PostConstruct
    private void init() {
        clientes = new TreeMap<>();
        hoteles = new TreeMap<>();
        numClientes = 0;
        numHoteles = 0;
    }

    public Cliente altaCliente(@NotNull @Valid Cliente cliente) throws ClienteNoRegistrado {
        log.info("Cliente con datos: " + cliente + " registrandose");
        if (clientes.containsKey(cliente.getDni())) {
            throw new ClienteYaRegistrado();
        } else {
            cliente.setId(numClientes++);
            clientes.put(cliente.getDni(), cliente);
            log.info("Cliente con datos: " + cliente + " registrado");
            return cliente;
        }
    }

    public Hotel altaHotel(@NotNull @Valid Hotel hotel) {
        log.info("Hotel con datos: " + hotel + " registrandose");
        if (hoteles.containsKey(hotel.getNombre())) {
            throw new HotelYaExiste();
        } else {
            hotel.setId(numHoteles++);
            hoteles.put(hotel.getNombre(), hotel);
            log.info("Hotel con datos: " + hotel + " registrado");
            return hotel;
        }
    }

    public Cliente loginCliente(@NotNull String userName, @NotNull String clave) {
        Cliente cliente = null;
        for (Map.Entry<String, Cliente> clientes : clientes.entrySet()) {
            if (clientes.getValue().getUserName().equals(userName) && clientes.getValue().getContraseña().equals(clave))
                cliente = clientes.getValue();
        }
        return cliente;
        /*return Optional
                .ofNullable(clientes.get(userName))
                .filter((cliente)
                        -> cliente.claveValida(clave));
                        */
    }


    public List<Hotel> buscarHoteles(Direccion direccion, LocalDateTime fechaIni, LocalDateTime fechaFin) {
        List<Hotel> listaHoteles = new ArrayList<>();
        for (Map.Entry<String, Hotel> hoteles : hoteles.entrySet()) {
            for (int i = 0; i < hoteles.getValue().getReservasActuales().size(); i++) {
                if (hoteles.getValue().getDireccion() == direccion) {
                    if (fechaIni.isBefore(hoteles.getValue().getReservasActuales().get(i).getFechaInicio())
                            && fechaFin.isBefore(hoteles.getValue().getReservasActuales().get(i).getFechaInicio())
                            || fechaIni.isAfter(hoteles.getValue().getReservasActuales().get(i).getFechaFin())) {
                        listaHoteles.add(hoteles.getValue());
                    } else {
                        throw new ReservaNoDisponible();
                    }
                } else {
                    throw new ReservaNoDisponible();
                }
            }
        }
        log.info("Lista hoteles: " + listaHoteles.get(0));
        return listaHoteles;
    }


    boolean hacerReserva(@NotNull @Valid Cliente cliente, Direccion direccion, LocalDateTime fechaIni, LocalDateTime fechaFin, int numDoble, int numSimple) {

        List<Hotel> listaHoteles = buscarHoteles(direccion, fechaIni, fechaFin);
        //Suponemos que se queda con el primer hotel que hay en esa direccion
        if (listaHoteles.get(0).getNumDobl() >= numDoble && listaHoteles.get(0).getNumSimp() >= numSimple) {
            Reserva reserva = new Reserva(1, direccion, fechaIni, fechaFin, numSimple, numDoble);
            cliente.addReserva(reserva);
            listaHoteles.get(0).setNumSimp(numSimple);
            listaHoteles.get(0).setNumDobl(numDoble);
            return true;
        }
        return false;
    }
}
