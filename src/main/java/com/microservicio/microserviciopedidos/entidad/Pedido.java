package com.microservicio.microserviciopedidos.entidad;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "proveedor_id", nullable = false)
    private Long proveedorId;

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

    public Long getId() {
        return id;
    }

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

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
    public Long getProveedorId() { return proveedorId; }
    public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }
}