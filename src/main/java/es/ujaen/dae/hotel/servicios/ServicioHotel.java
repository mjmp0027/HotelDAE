package es.ujaen.dae.hotel.servicios;

import es.ujaen.dae.hotel.entidades.*;
import es.ujaen.dae.hotel.excepciones.ClienteNoRegistrado;
import es.ujaen.dae.hotel.excepciones.ClienteYaRegistrado;
import es.ujaen.dae.hotel.excepciones.HotelYaExiste;
import es.ujaen.dae.hotel.excepciones.ReservaNoDisponible;
import es.ujaen.dae.hotel.repositorios.RepositorioAdministrador;
import es.ujaen.dae.hotel.repositorios.RepositorioCliente;
import es.ujaen.dae.hotel.repositorios.RepositorioHotel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Validated
public class ServicioHotel {
    @Autowired
    RepositorioCliente repositorioCliente;
    @Autowired
    RepositorioHotel repositorioHotel;
    @Autowired
    RepositorioAdministrador repositorioAdministrador;
    private int numClientes;
    private int numHoteles;

    @PostConstruct
    private void init() {
        numClientes = 0;
        numHoteles = 0;
        //Solo vamos a ser tres administradores
        Administrador manuel = new Administrador("mjmp", "clave1");
        Administrador carlos = new Administrador("cgr", "clave2");
        Administrador maria = new Administrador("mhm", "clave3");
        repositorioAdministrador.guardar(manuel);
        repositorioAdministrador.guardar(carlos);
        repositorioAdministrador.guardar(maria);
    }

    public Cliente altaCliente(@NotNull @Valid Cliente cliente) throws ClienteNoRegistrado {
        log.info("Cliente con datos: " + cliente + " registrandose");
        if (repositorioCliente.buscar(cliente.getDni()).isPresent()) {
            throw new ClienteYaRegistrado();
        } else {
            cliente.setId(numClientes++);
            repositorioCliente.guardar(cliente);
            log.info("Cliente con datos: " + cliente + " registrado");
            return cliente;
        }
    }

    public Hotel altaHotel(@NotNull @Valid Hotel hotel, @Valid @NotNull Administrador administrador) throws Exception {
        if (repositorioAdministrador.buscar(administrador.getUserName()).isPresent()) {
            log.info("Hotel con datos: " + hotel + " registrandose");
            if (repositorioHotel.buscar(hotel.getId()).isPresent()) {
                throw new HotelYaExiste();
            } else {
                hotel.setId(numHoteles++);
                repositorioHotel.guardar(hotel);
                log.info("Hotel con datos: " + hotel + " registrado");
                return hotel;
            }
        }
        throw new Exception("Administrador no valido");
    }

    @Transactional
    public Optional<Cliente> loginCliente(@NotNull String userName, @NotNull String clave) {
        Optional<Cliente> clienteLogin = repositorioCliente.buscar(userName)
                .filter((cliente) -> cliente.claveValida(clave));

        return clienteLogin;
//        return Optional
//                .ofNullable(clientes.get(userName))
//                .filter((cliente)
//                        -> cliente.claveValida(clave));
    }

    //TODO
    public List<Hotel> buscarHoteles(Direccion direccion, LocalDateTime fechaIni, LocalDateTime fechaFin, int numDoble, int numSimple) {
        List<Hotel> listaHoteles = new ArrayList<>();
//        for (Map.Entry<Integer, Hotel> hoteles : hoteles.entrySet()) {
//            for (int i = 0; i < hoteles.getValue().getReservasActuales().size(); i++) {
//                if (hoteles.getValue().getDireccion() == direccion) {
//                    if (fechaIni.isBefore(hoteles.getValue().getReservasActuales().get(i).getFechaInicio())
//                            && fechaFin.isBefore(hoteles.getValue().getReservasActuales().get(i).getFechaInicio())
//                            || fechaIni.isAfter(hoteles.getValue().getReservasActuales().get(i).getFechaFin())
//                            && hoteles.getValue().getNumDobl() > numDoble && hoteles.getValue().getNumSimp() > numSimple) {
//                        listaHoteles.add(hoteles.getValue());
//                    } else {
//                        throw new ReservaNoDisponible();
//                    }
//                } else {
//                    throw new ReservaNoDisponible();
//                }
//            }
//        }
        return listaHoteles;
    }

    boolean hacerReserva(@NotNull @Valid Cliente cliente, Direccion direccion, LocalDateTime fechaIni, LocalDateTime fechaFin, int numDoble, int numSimple, Hotel hotel) {

        if (buscarHoteles(direccion, fechaIni, fechaFin, numDoble, numSimple).contains(hotel))

            if (repositorioCliente.buscar(cliente.getDni()).isPresent())
                if (hotel.getNumDobl() >= numDoble && hotel.getNumSimp() >= numSimple) {
                    Reserva reserva = new Reserva(direccion, fechaIni, fechaFin, numSimple, numDoble);
                    repositorioCliente.nuevaReserva(cliente, reserva);
                    hotel.setNumSimp(numSimple);
                    hotel.setNumDobl(numDoble);
                    return true;
                }
        return false;
    }
}