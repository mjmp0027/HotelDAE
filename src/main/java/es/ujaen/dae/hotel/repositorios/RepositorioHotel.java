package es.ujaen.dae.hotel.repositorios;

import es.ujaen.dae.hotel.entidades.Direccion;
import es.ujaen.dae.hotel.entidades.Hotel;
import es.ujaen.dae.hotel.entidades.Reserva;
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
    public Optional<Hotel> buscar(int id) {
        return Optional.ofNullable(em.find(Hotel.class, id));
    }


    public void guardar(Hotel hotel) {
        em.persist(hotel);
    }

    public List<Hotel> buscarHoteles(Direccion direccion, int numDoble, int numSimple){
        List<Hotel> hoteles = new ArrayList<>();
        try{
            Query q = em.createQuery("Select h from Hotel h where h.direccion=:direccion " +
                                    "and h.numDoble>:numDoble and h.numSimple>:numSimple", Hotel.class);
            q.setParameter("numSimple", numSimple);
            q.setParameter("numDoble", numDoble);
            q.setParameter("direccion", direccion);
            hoteles = q.getResultList();
        }catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return hoteles;
    }

    public void nuevaReserva(Hotel hotel, Reserva reserva /*Cliente cliente*/){
        em.persist(reserva);
//        cliente = em.merge(cliente);
        hotel = em.merge(hotel);
//        cliente.addReserva(reserva);
        hotel.addReserva(reserva);
    }

}
