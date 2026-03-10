package com.microservicio.microserviciopedidos.entidad;

import jakarta.persistence.*;
import java.time.LocalDate;
<<<<<<< HEAD
=======
import java.util.ArrayList;
import java.util.List;
>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String estado;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;

<<<<<<< HEAD
    @Column(name = "cliente_id", nullable = false)  // ← agrega esto
    private Long clienteId;
    // 🔹 Constructor vacío (OBLIGATORIO para JPA)
    public Pedido() {
    }


    // Constructor vacío

    // Constructor con campos
    public Pedido(String estado, LocalDate fechaPedido, Long clienteId) {
        this.estado = estado;
        this.fechaPedido = fechaPedido;
        this.clienteId = clienteId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    // 🔹 Getters y Setters
=======
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    // RELACIÓN CON DETALLE
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>(); // Inicializar la lista
    @Transient
    public Double getTotal() {
        return detalles.stream()
                .mapToDouble(d -> d.getPrecio() * d.getCantidad())
                .sum();
    }

    //MÉTODO HELPER: Agrega un detalle y establece la relación bidireccional
    public void addDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this); // ¡ESTA LÍNEA ES CRÍTICA!
    }

    // MÉTODO HELPER: Elimina un detalle
    public void removeDetalle(DetallePedido detalle) {
        detalles.remove(detalle);
        detalle.setPedido(null);
    }
    // 🔹 Constructor vacío (OBLIGATORIO)
    public Pedido() {}

    // 🔹 Getters y Setters

>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133
    public Long getId() {
        return id;
    }

<<<<<<< HEAD
    public void setId(Long id) {
        this.id = id;
    }

=======
>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

<<<<<<< HEAD

}
=======
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
}
>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133
