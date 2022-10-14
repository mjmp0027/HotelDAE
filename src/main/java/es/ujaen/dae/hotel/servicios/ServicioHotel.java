package es.ujaen.dae.hotel.servicios;

import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Direccion;
import es.ujaen.dae.hotel.entidades.Hotel;
import es.ujaen.dae.hotel.excepciones.ClienteYaRegistrado;
import es.ujaen.dae.hotel.excepciones.HotelYaExiste;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Service
@Slf4j
public class ServicioHotel {
    Map<String, Cliente> clientes;
    Map<String, Hotel> hoteles;
    private int numClientes;
    private int numHoteles;

    @PostConstruct
    private void init() {
        clientes = new TreeMap<>();
        hoteles = new TreeMap<>();
        numClientes = 1;
        numHoteles = 1;
    }

    public Cliente altaCliente(@NotNull @Valid Cliente cliente) {
        log.info("Cliente con datos: " + cliente + " registrandose");
        if (clientes.containsKey(cliente)) {
            throw new ClienteYaRegistrado();
        } else {
            cliente.setId(numClientes++);
            clientes.put(cliente.getDni(), cliente);
            log.info("Cliente con datos: " + cliente + " resgistrado");
            return cliente;
        }
    }

    public Hotel altaHotel(@NotNull @Valid Hotel hotel) {
        log.info("Hotel con datos: " + hotel + " registrandose");
        if (hoteles.containsKey(hotel)) {
            throw new HotelYaExiste();
        } else {
            hotel.setId(numHoteles++);
            hoteles.put(hotel.getNombre(), hotel);
            log.info("Hotel con datos: " + hotel + " registrado");
            return hotel;
        }
    }

    public Optional<Cliente> loginCliente(@NotNull String userName, @NotNull String clave) {
            return Optional
                    .ofNullable(clientes.get(userName))
                    .filter((cliente)
                    ->cliente.getContraseña().equals(clave));
    }

    //TODO
    public String buscarHotel(Direccion direccion, LocalDateTime fechaIni, LocalDateTime fechaFin) {
        return "";
    }

    //TODO
    boolean hacerReserva(Cliente cliente, LocalDateTime fechaIni, LocalDateTime fechaFin, int numDoble, int numSimple, Hotel hotel) {
        return false;
    }

}
