package es.ujaen.dae.hotel.servicios;

import es.ujaen.dae.hotel.entidades.*;
import es.ujaen.dae.hotel.excepciones.*;
import es.ujaen.dae.hotel.repositorios.RepositorioAdministrador;
import es.ujaen.dae.hotel.repositorios.RepositorioCliente;
import es.ujaen.dae.hotel.repositorios.RepositorioHotel;
import es.ujaen.dae.hotel.repositorios.RepositorioReserva;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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

    @Autowired
    RepositorioReserva repositorioReserva;

    //Damos de alta el cliente en el sistema
    public Cliente altaCliente(@NotNull @Valid Cliente cliente) throws ClienteNoRegistrado {
        log.info("Cliente con datos: " + cliente + " registrandose");
        if (repositorioCliente.buscarPorUserName(cliente.getDni()).isPresent()) {
            throw new ClienteYaRegistrado();
        } else {
            repositorioCliente.guardarCliente(cliente);
            log.info("Cliente con datos: " + cliente + " registrado");
            return cliente;
        }
    }
    //Damos de alta el hotel en el sistema
    public Hotel altaHotel(@NotNull @Valid Hotel hotel, @Valid @NotNull Administrador administrador) throws AdministradorNoValido {
        if (repositorioAdministrador.buscarAdminPorUserName(administrador.getUserName()).isPresent()) {
            log.info("Hotel con datos: " + hotel + " registrandose");
            if (repositorioHotel.buscarHotelPorId(hotel.getId()).isPresent()) {
                throw new HotelYaExiste();
            } else {
                repositorioHotel.guardarHotel(hotel);
                log.info("Hotel con datos: " + hotel + " registrado");
                return hotel;
            }
        }
        throw new AdministradorNoValido();
    }
    //Damos de alta el administrador en el sistema
    public Administrador altaAdministrador(@NotNull @Valid Administrador administrador) throws AdministradorYaExiste {
        if (repositorioAdministrador.buscarAdminPorUserName(administrador.getUserName()).isPresent()) {
            throw new AdministradorYaExiste();
        } else {
            repositorioAdministrador.guardarAdministrador(administrador);
            return administrador;
        }
    }
    //Hacemos el login del cliente
    @Transactional
    public Optional<Cliente> loginCliente(@NotNull String userName, @NotNull String clave) {
        Optional<Cliente> clienteLogin = repositorioCliente.buscarPorUserName(userName)
                .filter((cliente) -> cliente.claveValida(clave));
        return clienteLogin;
    }
    //Buscamos el hotel mediante direccion, fechas y tipo
    public List<Hotel> buscarHoteles(Direccion direccion, LocalDate fechaIni, LocalDate fechaFin, int numDoble, int numSimple) {
        List<Hotel> listaHoteles = repositorioHotel.buscarHotelesPorDireccion(direccion);
        List<Hotel> listaHotelesDisp = new ArrayList<>();
        for (Hotel hotel : listaHoteles) {
            if (hotel.comprobarReserva(fechaIni, fechaFin, numDoble, numSimple))
                listaHotelesDisp.add(hotel);
        }
        if (listaHoteles.isEmpty() || listaHotelesDisp.isEmpty())
            throw new ReservaNoDisponible();
        return listaHotelesDisp;
    }

    //TODO revisar
    //Realización de la reserva
    @Transactional
    public boolean hacerReserva(@NotNull @Valid Cliente cliente, LocalDate fechaIni, LocalDate fechaFin, int numDoble, int numSimple, Hotel hotel) {

        if (repositorioCliente.buscarPorUserName(cliente.getUserName()).isPresent()) {
                Reserva reserva = new Reserva(fechaIni, fechaFin, numSimple, numDoble, cliente);
                repositorioReserva.guardarReserva(reserva);
                hotel.addReserva(reserva);
                repositorioHotel.actualizarHotel(hotel);

            return true;
        }
        return false;
    }

    @Scheduled(cron = "0 0 3 * * * ?")
    public void cambioReserva(Hotel hotel) {
        hotel.cambioReservas();
        repositorioHotel.actualizarHotel(hotel);
    }
}