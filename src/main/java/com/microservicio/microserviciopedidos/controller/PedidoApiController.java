package com.microservicio.microserviciopedidos.controller;

import com.microservicio.microserviciopedidos.dto.ProductoDTO;
import com.microservicio.microserviciopedidos.entidad.DetallePedido;
import com.microservicio.microserviciopedidos.entidad.Pedido;
import com.microservicio.microserviciopedidos.service.PedidoService;
import com.microservicio.microserviciopedidos.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoApiController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;

    public PedidoApiController(PedidoService pedidoService, ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }

    @GetMapping
    public List<Map<String, Object>> listarTodos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Pedido p : pedidos) {
            Pedido conDetalles = pedidoService.obtenerPedidoConDetalles(p.getId());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", conDetalles.getId());
            item.put("estado", conDetalles.getEstado());
            item.put("fechaPedido", conDetalles.getFechaPedido());
            item.put("clienteId", conDetalles.getProveedorId());

            List<Map<String, Object>> detalles = new ArrayList<>();
            for (DetallePedido d : conDetalles.getDetalles()) {
                Map<String, Object> det = new LinkedHashMap<>();
                det.put("id", d.getId());
                det.put("productoId", d.getProductoId());
                det.put("cantidad", d.getCantidad());
                det.put("precio", d.getPrecio());
                det.put("subtotal", d.getPrecio() * d.getCantidad());
                detalles.add(det);
            }
            item.put("detalles", detalles);
            item.put("total", conDetalles.getDetalles().stream()
                    .mapToDouble(d -> d.getPrecio() * d.getCantidad()).sum());
            response.add(item);
        }
        return response;
    }

    @GetMapping("/detalle")
    public List<Map<String, Object>> todosLosDetalles() {
        List<Pedido> pedidos = pedidoService.listarPedidos();

        // Si el microservicio no responde, usamos lista vacía
        List<ProductoDTO> productos = new ArrayList<>();
        try {
            productos = productoService.listarProductos();
        } catch (Exception e) {
            System.out.println("⚠️ Microservicio productos no disponible: " + e.getMessage());
        }

        final List<ProductoDTO> productosFinal = productos;
        List<Map<String, Object>> response = new ArrayList<>();

        for (Pedido p : pedidos) {
            Pedido conDetalles = pedidoService.obtenerPedidoConDetalles(p.getId());

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("pedidoId", conDetalles.getId());
            item.put("fechaPedido", conDetalles.getFechaPedido());
            item.put("estado", conDetalles.getEstado());
            item.put("clienteId", conDetalles.getProveedorId());

            List<Map<String, Object>> detalles = new ArrayList<>();
            double total = 0;

            for (DetallePedido d : conDetalles.getDetalles()) {
                // ← usa getIdProducto() en lugar de getId()
                String nombreProducto = productosFinal.stream()
                        .filter(prod -> prod.getIdProducto() != null && prod.getIdProducto().equals(d.getProductoId()))
                        .map(ProductoDTO::getNombre)
                        .findFirst()
                        .orElse("Producto #" + d.getProductoId());

                Map<String, Object> det = new LinkedHashMap<>();
                det.put("id", d.getId());
                det.put("productoId", d.getProductoId());
                det.put("nombreProducto", nombreProducto);
                det.put("cantidad", d.getCantidad());
                det.put("precio", d.getPrecio());
                det.put("subtotal", d.getPrecio() * d.getCantidad());
                detalles.add(det);
                total += d.getPrecio() * d.getCantidad();
            }

            item.put("detalles", detalles);
            item.put("totalProductos", detalles.size());
            item.put("total", total);
            response.add(item);
        }

        return response;
    }
}