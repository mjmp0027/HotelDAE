package es.ujaen.dae.hotel.rest.dto;

import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Direccion;


public record DTOCliente(
        String dni,
        String nombre,
        String userName,
        String contraseña,
        Direccion direccion,
        String tlf,
        String email) {

    public DTOCliente(Cliente cliente) {
        this(cliente.getDni(),
                cliente.getNombre(),
                cliente.getUserName(),
                "", cliente.getDireccion(),
                cliente.getTlf(),
                cliente.getEmail());
    }

    public Cliente aCliente() {
        return new Cliente(dni, nombre, userName, contraseña, direccion, tlf, email);
    }
}