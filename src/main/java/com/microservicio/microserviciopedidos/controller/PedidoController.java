package com.microservicio.microserviciopedidos.controller;

import com.microservicio.microserviciopedidos.entidad.Pedido;
import com.microservicio.microserviciopedidos.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String vistaPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarPedidos());
        return "pedidos";
    }

    @PostMapping("/agregar")
    public String agregarPedido(@RequestParam String fechaPedido,
                                @RequestParam String estado) {
        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDate.parse(fechaPedido));
        pedido.setEstado(estado);
        pedidoService.crearPedido(pedido);
        return "redirect:/pedidos";
    }

    @PostMapping("/editar")
    public String editarPedido(@RequestParam Long id,
                               @RequestParam String fechaPedido,
                               @RequestParam String estado) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        if (pedido != null) {
            pedido.setFechaPedido(LocalDate.parse(fechaPedido));
            pedido.setEstado(estado);
            pedidoService.crearPedido(pedido);
        }
        return "redirect:/pedidos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return "pedidos";
    }
}