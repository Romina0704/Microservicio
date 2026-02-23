package com.microservicio.microserviciopedidos.entidad;

import jakarta.persistence.*;
import java.time.LocalDate;

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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


}
