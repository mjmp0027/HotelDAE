package es.ujaen.dae.hotel.rest.dto;

import es.ujaen.dae.hotel.entidades.Administrador;

public record DTOAdministrador(
        String userName,
        String contraseña) {
    public  DTOAdministrador(Administrador administrador){
        this(administrador.getUserName(), administrador.getContraseña());
    }
}
