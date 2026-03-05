package modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @Column(name = "id_reserva")
    private String idReserva;

    @ManyToOne
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio socio;

    @ManyToOne
    @JoinColumn(name = "id_pista", nullable = false)
    private Pista pista;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "duracion_min")
    private int duracionMin;

    @Column(name = "precio")
    private double precio;

    public Reserva() {
    }

    public Reserva(String idReserva, Socio socio, Pista pista,
                   LocalDate fecha, LocalTime horaInicio,
                   int duracionMin, double precio)
            throws IdObligatorioException {

        if (idReserva == null || idReserva.isBlank())
            throw new IdObligatorioException("idReserva obligatorio");
        if (socio == null)
            throw new IdObligatorioException("socio obligatorio");
        if (pista == null)
            throw new IdObligatorioException("pista obligatoria");
        if (fecha == null)
            throw new IdObligatorioException("fecha obligatoria");
        if (horaInicio == null)
            throw new IdObligatorioException("horaInicio obligatoria");
        if (duracionMin <= 0)
            throw new IdObligatorioException("duración debe ser > 0");
        if (precio < 0)
            throw new IdObligatorioException("precio debe ser >= 0");

        this.idReserva = idReserva;
        this.socio = socio;
        this.pista = pista;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionMin = duracionMin;
        this.precio = precio;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Pista getPista() {
        return pista;
    }

    public void setPista(Pista pista) {
        this.pista = pista;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // 🔹 Para que el ComboBox muestre algo legible
    @Override
    public String toString() {
        return idReserva + " - " + pista.getIdPista() + " - " + fecha + " " + horaInicio;
    }
}