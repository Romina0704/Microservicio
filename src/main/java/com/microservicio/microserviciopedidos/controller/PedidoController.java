package com.microservicio.microserviciopedidos.controller;

import com.microservicio.microserviciopedidos.client.ClienteClient;
import com.microservicio.microserviciopedidos.dto.ClienteDTO;
import com.microservicio.microserviciopedidos.entidad.Pedido;
import com.microservicio.microserviciopedidos.repository.PedidoRepository;
import com.microservicio.microserviciopedidos.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteClient clienteClient;
    private final PedidoRepository pedidoRepository;

    public PedidoController(PedidoService pedidoService , ClienteClient clienteClient,  PedidoRepository pedidoRepository) {
        this.pedidoService = pedidoService;
        this.clienteClient = clienteClient;
        this.pedidoRepository =pedidoRepository;
    }

    @GetMapping
    public String vistaPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarPedidos());
        return "pedidos";
    }
    // Devuelve todos los clientes (para el selector del formulario)
    @GetMapping("/clientes")
    @ResponseBody
    public List<ClienteDTO> obtenerClientes() {
        return clienteClient.listarClientes();
    }

    @PostMapping("/agregar")
    public String agregarPedido(@ModelAttribute Pedido pedido) {
        pedidoRepository.save(pedido);
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