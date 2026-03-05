package modelo;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pistas")
public class Pista {

    @Id
    @Column(name = "id_pista")
    private String idPista;

    @Column(name = "deporte", nullable = false)
    private String deporte;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "disponible")
    private boolean disponible;

    // 🔹 Relación con Reserva
    @OneToMany(mappedBy = "pista")
    private List<Reserva> reservas;

    public Pista() {
    }

    public Pista(String idPista, String deporte, String descripcion, boolean disponible)
            throws IdObligatorioException {

        if (idPista == null || idPista.isEmpty()) {
            throw new IdObligatorioException("El id de la pista no puede ser vacío");
        }

        this.idPista = idPista;
        this.deporte = deporte;
        this.descripcion = descripcion;
        this.disponible = disponible;
    }

    public String getIdPista() {
        return idPista;
    }

    public void setIdPista(String idPista) {
        this.idPista = idPista;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    // 🔹 Para que el ComboBox muestre algo legible
    @Override
    public String toString() {
        return idPista + " - " + deporte + (disponible ? " (Disponible)" : " (No disponible)");
    }
}