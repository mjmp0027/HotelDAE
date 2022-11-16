package es.ujaen.dae.hotel.servicios;

import es.ujaen.dae.hotel.entidades.*;
import es.ujaen.dae.hotel.excepciones.*;
import es.ujaen.dae.hotel.repositorios.RepositorioAdministrador;
import es.ujaen.dae.hotel.repositorios.RepositorioCliente;
import es.ujaen.dae.hotel.repositorios.RepositorioHotel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    public Hotel altaHotel(@NotNull @Valid Hotel hotel, @Valid @NotNull Administrador administrador) throws AdministradorNoValido {
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
        throw new AdministradorNoValido();
    }

    public Administrador altaAdministrador(@NotNull @Valid Administrador administrador) throws AdministradorYaExiste {
        if(repositorioAdministrador.buscar(administrador.getUserName()).isPresent()){
            throw new AdministradorYaExiste();
        }else {
            repositorioAdministrador.guardar(administrador);
            return administrador;
        }
    }
    @Transactional
    public Optional<Cliente> loginCliente(@NotNull String userName, @NotNull String clave) {
        Optional<Cliente> clienteLogin = repositorioCliente.buscarPorId(userName)
                .filter((cliente) -> cliente.claveValida(clave));
        return clienteLogin;
    }

    public List<Hotel> buscarHotelesPorLocalidad(Direccion direccion, LocalDateTime fechaIni, LocalDateTime fechaFin, int numDoble, int numSimple) {
        List<Hotel> listaHoteles = repositorioHotel.buscarHotelesPorLocalidad(direccion);
        List<Hotel> listaHotelesDisp = new ArrayList<>();
        for (Hotel hotel : listaHoteles) {
            if(hotel.comprobarReserva(fechaIni, fechaFin, numDoble, numSimple)){
                    listaHotelesDisp.add(hotel);
                } else {
                    throw new ReservaNoDisponible();
                }
            }
        return listaHotelesDisp;
    }

    //TODO revisar
    @Transactional
    public boolean hacerReserva(@NotNull @Valid Cliente cliente, LocalDateTime fechaIni, LocalDateTime fechaFin, int numDoble, int numSimple, Hotel hotel) {

        if (repositorioCliente.buscarPorId(cliente.getDni()).isPresent()) {
            Reserva reserva = new Reserva(fechaIni, fechaFin, numSimple, numDoble, cliente);
            repositorioHotel.nuevaReserva(hotel, reserva);
            return true;
        }
        return false;
    }
    @Scheduled(cron="0 0 3 * * * ?")
    public void cambioReserva(Hotel hotel){
        repositorioHotel.cambioReservas(hotel);
    }
}