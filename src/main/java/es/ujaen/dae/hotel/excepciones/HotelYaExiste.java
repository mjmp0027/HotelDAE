package es.ujaen.dae.hotel.excepciones;

public class HotelYaExiste extends RuntimeException{
    public HotelYaExiste(){
        System.out.println("Este hotel ya está dado de alta.");
    }
}
