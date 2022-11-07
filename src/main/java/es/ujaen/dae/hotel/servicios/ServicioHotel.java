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

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    public Cliente altaCliente(@NotNull @Valid Cliente cliente) throws ClienteNoRegistrado {
        log.info("Cliente con datos: " + cliente + " registrandose");
        if (repositorioCliente.buscar(cliente.getDni()).isPresent()) {
            throw new ClienteYaRegistrado();
        } else {
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
    }

    public List<Hotel> buscarHoteles(Direccion direccion, LocalDateTime fechaIni, LocalDateTime fechaFin, int numDoble, int numSimple) {
        List<Hotel> listaHoteles = repositorioHotel.buscarHoteles(direccion, numDoble, numSimple);
        List<Hotel> listaHotelesDisp = new ArrayList<>();
        for (Hotel hotel : listaHoteles) {
            for (int j = 0; j < hotel.getReservasActuales().size(); j++) {
                Reserva reserva = hotel.getReservasActuales().get(j);
                if (fechaIni.isBefore(reserva.getFechaInicio()) && fechaFin.isBefore(reserva.getFechaInicio())
                        || fechaIni.isAfter(reserva.getFechaFin())) {
                    listaHotelesDisp.add(hotel);
                } else {
                    throw new ReservaNoDisponible();
                }
            }
        }
        return listaHotelesDisp;
    }

    @Transactional
    boolean hacerReserva(@NotNull @Valid Cliente cliente, Direccion direccion, LocalDateTime fechaIni, LocalDateTime fechaFin, int numDoble, int numSimple, Hotel hotel) {

        if (repositorioCliente.buscar(cliente.getDni()).isPresent()) {
            Reserva reserva = new Reserva(direccion, fechaIni, fechaFin, numSimple, numDoble);
            repositorioHotel.nuevaReserva(hotel, reserva);
            repositorioCliente.nuevaReserva(cliente, reserva);
            hotel.setNumSimp(numSimple);
            hotel.setNumDobl(numDoble);
            return true;
        }
        return false;
    }
}