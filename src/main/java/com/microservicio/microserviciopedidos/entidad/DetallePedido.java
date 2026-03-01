package com.microservicio.microserviciopedidos.entidad;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relación con Pedido
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // 🆔 Solo guardamos el ID del producto (viene de otra API)
    @Column(nullable = false)
    private Long productoId;

    // 📦 Cantidad del producto
    @Column(nullable = false)
    private Integer cantidad;

    // 💰 Opcional (recomendado si quieres congelar precio histórico)
    private Double precioUnitario;

    // ===== CONSTRUCTORES =====

    public DetallePedido() {
    }

    public DetallePedido(Pedido pedido, Long productoId, Integer cantidad, Double precioUnitario) {
        this.pedido = pedido;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // ===== GETTERS Y SETTERS =====

    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}