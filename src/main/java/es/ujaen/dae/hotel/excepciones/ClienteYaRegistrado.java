package es.ujaen.dae.hotel.excepciones;

public class ClienteYaRegistrado extends RuntimeException{
    public ClienteYaRegistrado(){
        System.out.println("Este usuario ya existe.");
    }
}
