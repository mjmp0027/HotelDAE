package es.ujaen.dae.hotel.repositorios;

import es.ujaen.dae.hotel.entidades.Hotel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RepositorioHotel {
    @PersistenceContext
    EntityManager em;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Hotel> buscar(int id) {
        return Optional.ofNullable(em.find(Hotel.class, id));
    }
    /*      NO SE SI TIENE SENTIDO
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Hotel> buscaryBloquear(String id) {
        return Optional.ofNullable(em.find(Hotel.class, id, LockModeType.PESSIMISTIC_WRITE));
    }
    */

    public void guardar(Hotel hotel) {
        em.persist(hotel);
    }
}
