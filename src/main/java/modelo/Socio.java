package modelo;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "socios")
public class Socio {

    @Id
    @Column(name = "id_socio")
    private String idSocio;

    @Column(name = "dni", nullable = false)
    private String dni;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    // 🔹 Relación con Reserva
    @OneToMany(mappedBy = "socio")
    private List<Reserva> reservas;

    public Socio() {
    }

    public Socio(String idSocio, String dni, String nombre,
                 String apellidos, String telefono, String email)
            throws IdObligatorioException {

        if (idSocio == null || idSocio.isEmpty()) {
            throw new IdObligatorioException("El id del socio no puede ser vacío");
        }

        this.idSocio = idSocio;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
    }

    public String getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(String idSocio) {
        this.idSocio = idSocio;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return idSocio + " - " + nombre + " " + apellidos;
    }
}