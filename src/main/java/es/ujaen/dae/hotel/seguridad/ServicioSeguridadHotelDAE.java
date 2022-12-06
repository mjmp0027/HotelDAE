package es.ujaen.dae.hotel.seguridad;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ServicioSeguridadHotelDAE {
    private HttpSecurity httpSecurity;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //FIXME
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Desactivar cualquier soporte de sesión
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.csrf().disable();

        //Activar seguridad HTTP Basic
        httpSecurity.httpBasic();

        // Definir protección por URL
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/hoteldae/clientes").permitAll();

        // Permitir el acceso de un cliente sólo a sus recursos asociados (datos personales, cuentas, tarjetas, etc.)
        httpSecurity.authorizeRequests().antMatchers("/hoteldae/clientes/{userName}/**").access("hasRole('CLIENTE') and #userName == principal.username");

        return httpSecurity.build();

    }

}
