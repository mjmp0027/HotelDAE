package es.ujaen.dae.hotel.repositorios;

import es.ujaen.dae.hotel.entidades.Hotel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
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
    /*      NO SE SI TIENE SENTIDO
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Hotel> buscaryBloquear(String id) {
        return Optional.ofNullable(em.find(Hotel.class, id, LockModeType.PESSIMISTIC_WRITE));
    }
    */

    public void guardar(Hotel hotel) {
        em.persist(hotel);
    }

    /*public List<Hotel> buscarHoteles(Direccion direccion, LocalDateTime fechaIni, LocalDateTime fechaFin, int numDoble, int numSimple){
        List<Hotel> hotels = null;
        try{
            Query q = em.createQuery("Select h from Hotel h where h.direccion=:direccion", Hotel.class);
            q.setParameter("direccion", direccion);
            hotels = (List<Hotel>) q.getResultList();
        }catch (Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return hotels;
    }*/
}
