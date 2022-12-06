package es.ujaen.dae.hotel.seguridad;

import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.servicios.ServicioHotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ServicioDatosCliente implements UserDetailsService {
    @Autowired
    ServicioHotel servicioHotel;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Cliente cliente = servicioHotel.verCliente(userName)
                .orElseThrow(() -> new UsernameNotFoundException(""));

        return User.withUsername(cliente.getUserName())
                .roles("CLIENTE").password(cliente.getContraseña())
                .build();
    }
}
