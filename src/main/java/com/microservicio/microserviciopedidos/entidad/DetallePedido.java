package com.microservicio.microserviciopedidos.entidad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precio;

    // ← NUEVO: nombre del producto (no se guarda en BD)
    @Transient
    private String nombreProducto;

    @Column(name = "precio_proveedor")
    private Double precioProveedor;

    @Column(name = "precio_venta")
    private Double precioVenta;

    // 🔥 RELACIÓN CON PEDIDO
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    // 🔹 Constructor vacío
    public DetallePedido() {}

    // 🔹 Getters y Setters
    public Long getId() { return id; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    // ← NUEVO: subtotal calculado
    @Transient
    public Double getSubtotal() { return precio * cantidad; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Double getPrecioProveedor() { return precioProveedor; }
    public void setPrecioProveedor(Double precioProveedor) { this.precioProveedor = precioProveedor; }

    public Double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(Double precioVenta) { this.precioVenta = precioVenta; }
}