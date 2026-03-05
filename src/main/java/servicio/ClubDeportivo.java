package servicio;

import jakarta.persistence.*;
import modelo.*;

import java.time.LocalTime;
import java.util.List;

public class ClubDeportivo {

    private EntityManagerFactory emf;

    public ClubDeportivo() {
        emf = Persistence.createEntityManagerFactory("clubPU");
    }

    // ===================== SOCIOS =====================

    public List<Socio> getSocios() {
        EntityManager em = emf.createEntityManager();
        List<Socio> lista = em.createQuery("from Socio", Socio.class).getResultList();
        em.close();
        return lista;
    }

    public void altaSocio(Socio socio) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(socio);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void eliminarSocio(String idSocio) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Socio socio = em.find(Socio.class, idSocio);
            if (socio == null) {
                throw new RuntimeException("El socio no existe");
            }

            // 🔴 No permitir borrar socio con reservas
            Long totalReservas = em.createQuery(
                            "SELECT COUNT(r) FROM Reserva r WHERE r.socio.idSocio = :id",
                            Long.class)
                    .setParameter("id", idSocio)
                    .getSingleResult();

            if (totalReservas > 0) {
                throw new RuntimeException("No se puede eliminar un socio con reservas");
            }

            em.remove(socio);
            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ===================== PISTAS =====================

    public List<Pista> getPistas() {
        EntityManager em = emf.createEntityManager();
        List<Pista> lista = em.createQuery("from Pista", Pista.class).getResultList();
        em.close();
        return lista;
    }

    public void altaPista(Pista pista) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(pista);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void cambiarDisponibilidadPista(String idPista, boolean estado) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Pista pista = em.find(Pista.class, idPista);
            if (pista == null) {
                throw new RuntimeException("La pista no existe");
            }
            pista.setDisponible(estado);
            em.merge(pista);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ===================== RESERVAS =====================

    // 🔥 AHORA CON JOIN FETCH (carga socio y pista en la misma consulta)
    public List<Reserva> getReservas() {

        EntityManager em = emf.createEntityManager();

        List<Reserva> lista = em.createQuery(
                        "SELECT r FROM Reserva r " +
                                "JOIN FETCH r.socio " +
                                "JOIN FETCH r.pista",
                        Reserva.class)
                .getResultList();

        em.close();
        return lista;
    }

    public void crearReserva(Reserva reserva) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 1️⃣ Comprobar que la pista existe y está disponible
            Pista pista = em.find(Pista.class, reserva.getPista().getIdPista());
            if (pista == null) {
                throw new RuntimeException("La pista no existe");
            }

            if (!pista.isDisponible()) {
                throw new RuntimeException("La pista no está disponible");
            }

            // 2️⃣ Comprobar solape
            List<Reserva> reservasMismaPista = em.createQuery(
                            "SELECT r FROM Reserva r WHERE r.pista.idPista = :idPista AND r.fecha = :fecha",
                            Reserva.class)
                    .setParameter("idPista", reserva.getPista().getIdPista())
                    .setParameter("fecha", reserva.getFecha())
                    .getResultList();

            for (Reserva r : reservasMismaPista) {

                LocalTime inicioExistente = r.getHoraInicio();
                LocalTime finExistente = inicioExistente.plusMinutes(r.getDuracionMin());

                LocalTime inicioNueva = reserva.getHoraInicio();
                LocalTime finNueva = inicioNueva.plusMinutes(reserva.getDuracionMin());

                boolean solape =
                        inicioNueva.isBefore(finExistente) &&
                                finNueva.isAfter(inicioExistente);

                if (solape) {
                    throw new RuntimeException("Existe solape de reservas en esa pista");
                }
            }

            // 3️⃣ Calcular precio automáticamente (6€ cada 30 min)
            double precioCalculado =
                    Math.ceil(reserva.getDuracionMin() / 30.0) * 6.0;
            reserva.setPrecio(precioCalculado);

            em.persist(reserva);
            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void cancelarReserva(String idReserva) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Reserva reserva = em.find(Reserva.class, idReserva);
            if (reserva == null) {
                throw new RuntimeException("La reserva no existe");
            }
            em.remove(reserva);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // ===================== CIERRE =====================

    public void cerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}