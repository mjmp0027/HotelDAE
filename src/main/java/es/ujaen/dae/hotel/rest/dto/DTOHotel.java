package es.ujaen.dae.hotel.rest.dto;

import es.ujaen.dae.hotel.entidades.Direccion;
import es.ujaen.dae.hotel.entidades.Hotel;
/**
 * DTO para recopilación de datos de hotel

 */

public record DTOHotel(
        int id,
        String nombre,
        Direccion direccion,
        int numSimp,
        int numDobl) {

    public DTOHotel(Hotel hotel) {
        this(hotel.getId(),
                hotel.getNombre(),
                hotel.getDireccion(),
                hotel.getNumSimp(),
                hotel.getNumDobl());
    }

    public Hotel aHotel() {
        return new Hotel(id, nombre, direccion, numSimp, numDobl);
    }
}