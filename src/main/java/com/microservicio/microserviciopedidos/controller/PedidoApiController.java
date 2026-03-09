package com.microservicio.microserviciopedidos.controller;

import com.microservicio.microserviciopedidos.entidad.DetallePedido;
import com.microservicio.microserviciopedidos.entidad.Pedido;
import com.microservicio.microserviciopedidos.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoApiController {

    private final PedidoService pedidoService;

    public PedidoApiController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // GET http://localhost:8085/api/pedidos
    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarPedidos();
    }

    // GET http://localhost:8085/api/pedidos/{id}
    @GetMapping("/{id}")
    public Map<String, Object> detallePedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.obtenerPedidoConDetalles(id);
        if (pedido == null) return Map.of("error", "Pedido no encontrado");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", pedido.getId());
        response.put("fechaPedido", pedido.getFechaPedido());
        response.put("estado", pedido.getEstado());
        response.put("clienteId", pedido.getClienteId());

        List<Map<String, Object>> detalles = new ArrayList<>();
        for (DetallePedido d : pedido.getDetalles()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", d.getId());
            item.put("productoId", d.getProductoId());
            item.put("cantidad", d.getCantidad());
            item.put("precio", d.getPrecio());
            item.put("subtotal", d.getPrecio() * d.getCantidad());
            detalles.add(item);
        }

        response.put("detalles", detalles);
        response.put("total", pedido.getDetalles().stream()
                .mapToDouble(d -> d.getPrecio() * d.getCantidad()).sum());

        return response;
    }
}