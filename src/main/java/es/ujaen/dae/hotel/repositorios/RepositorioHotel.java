package es.ujaen.dae.hotel.repositorios;

import es.ujaen.dae.hotel.entidades.Direccion;
import es.ujaen.dae.hotel.entidades.Hotel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RepositorioHotel {

    private final Logger logger = Logger.getLogger(Hotel.class.getName());
    @PersistenceContext
    EntityManager em;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Hotel> buscarHotelPorId(int id) {
        return Optional.ofNullable(em.find(Hotel.class, id));
    }

    public void guardarHotel(Hotel hotel) {
        em.persist(hotel);
    }

    public List<Hotel> buscarHotelesPorDireccion(String ciudad) {
        List<Hotel> hoteles = new ArrayList<>();
        try {
            Query q = em.createQuery("Select h from Hotel h where h.direccion.ciudad=:ciudad", Hotel.class);
            q.setParameter("ciudad", ciudad);
            hoteles = q.getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return hoteles;
    }

    public void actualizarHotel(Hotel hotel){
        em.merge(hotel);
    }

    public List<Hotel> hoteles(){
        List<Hotel> hoteles;
        hoteles = em.createQuery("SELECT h FROM Hotel h", Hotel.class).getResultList();
        return hoteles;
    }

    public void cambioReservas(Hotel hotel){
        hotel.cambioReservas();
    }
}
