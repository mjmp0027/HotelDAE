package es.ujaen.dae.hotel.rest.dto;

import es.ujaen.dae.hotel.entidades.Administrador;
/**
 * DTO para recopilación de datos de administrador

 */

public record DTOAdministrador(
        String userName,
        String contraseña) {
    public DTOAdministrador(Administrador administrador) {
        this(administrador.getUserName(), administrador.getContraseña());
    }

    public Administrador aAdministrador() {
        return new Administrador(userName, contraseña);
    }
}