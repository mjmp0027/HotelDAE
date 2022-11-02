package es.ujaen.dae.hotel.repositorios;

import es.ujaen.dae.hotel.entidades.Cliente;
import es.ujaen.dae.hotel.entidades.Reserva;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RepositorioCliente {
    @PersistenceContext
    EntityManager em;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Optional<Cliente> buscar(String id) {
        return Optional.ofNullable(em.find(Cliente.class, id));
    }

    public void guardar(Cliente cliente) {
        em.persist(cliente);
    }

    public void nuevaReserva(Cliente cliente, Reserva reserva) {
        em.persist(reserva);

        cliente = em.merge(cliente);
        cliente.addReserva(reserva);
    }

    public void actualizar(Cliente cliente) {
        em.merge(cliente);
    }
}
