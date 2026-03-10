package com.microservicio.microserviciopedidos.controller;

import com.microservicio.microserviciopedidos.dto.ProductoDTO;
import com.microservicio.microserviciopedidos.entidad.DetallePedido;
import com.microservicio.microserviciopedidos.entidad.Pedido;
import com.microservicio.microserviciopedidos.entidad.Proveedor;
import com.microservicio.microserviciopedidos.service.PedidoService;
import com.microservicio.microserviciopedidos.service.ProductoService;
import com.microservicio.microserviciopedidos.service.ProveedorService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PedidoApiController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final ProveedorService proveedorService;

    public PedidoApiController(PedidoService pedidoService,
                               ProductoService productoService,
                               ProveedorService proveedorService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;
        this.proveedorService = proveedorService;
    }

    // ─── GET http://localhost:8085/api/pedidos ───
    @GetMapping("/api/pedidos")
    public List<Map<String, Object>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        List<Proveedor> proveedores = proveedorService.listarProveedores();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Pedido p : pedidos) {
            Pedido conDetalles = pedidoService.obtenerPedidoConDetalles(p.getId());

            Proveedor proveedor = proveedores.stream()
                    .filter(pv -> pv.getId().equals(conDetalles.getProveedorId()))
                    .findFirst().orElse(null);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", conDetalles.getId());
            item.put("fechaPedido", conDetalles.getFechaPedido());
            item.put("estado", conDetalles.getEstado());
            item.put("proveedorId", conDetalles.getProveedorId());
            item.put("proveedor", proveedor != null ? proveedor.getNombre() : "Proveedor #" + conDetalles.getProveedorId());
            item.put("totalProductos", conDetalles.getDetalles().size());
            item.put("total", conDetalles.getDetalles().stream()
                    .mapToDouble(d -> d.getPrecio() * d.getCantidad()).sum());
            response.add(item);
        }
        return response;
    }

    // ─── GET http://localhost:8085/api/detalle ───
    @GetMapping("/api/detalle")
    public List<Map<String, Object>> listarDetalles() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        List<Proveedor> proveedores = proveedorService.listarProveedores();

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

            Proveedor proveedor = proveedores.stream()
                    .filter(pv -> pv.getId().equals(conDetalles.getProveedorId()))
                    .findFirst().orElse(null);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("pedidoId", conDetalles.getId());
            item.put("fechaPedido", conDetalles.getFechaPedido());
            item.put("estado", conDetalles.getEstado());
            item.put("proveedorId", conDetalles.getProveedorId());
            item.put("proveedor", proveedor != null ? proveedor.getNombre() : "Proveedor #" + conDetalles.getProveedorId());
            item.put("ruc", proveedor != null ? proveedor.getRuc() : "—");

            List<Map<String, Object>> detalles = new ArrayList<>();
            double total = 0;

            for (DetallePedido d : conDetalles.getDetalles()) {
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