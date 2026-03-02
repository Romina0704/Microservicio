package com.microservicio.microserviciopedidos.controller;

import com.microservicio.microserviciopedidos.dto.ClienteDTO;
import com.microservicio.microserviciopedidos.dto.ProductoDTO;
import com.microservicio.microserviciopedidos.entidad.Pedido;
import com.microservicio.microserviciopedidos.service.ClienteService;
import com.microservicio.microserviciopedidos.service.PedidoService;
import com.microservicio.microserviciopedidos.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final ClienteService clienteService;

    public PedidoController(PedidoService pedidoService,
                            ProductoService productoService,
                            ClienteService clienteService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String vistaPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarPedidos());
        return "pedidos";
    }

    @PostMapping("/agregar")
    public String agregarPedido(@RequestParam String fechaPedido,
                                @RequestParam String estado,
                                @RequestParam Long clienteId) {

        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDate.parse(fechaPedido));
        pedido.setEstado(estado);
        pedido.setClienteId(clienteId);

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
        return "redirect:/pedidos";
    }

    // 🔹 PRODUCTOS
    @GetMapping("/detalles/listar-productos")
    @ResponseBody
    public List<ProductoDTO> listarProductos() {
        return productoService.listarProductos();
    }

    // 🔹 CLIENTES
    @GetMapping("/listar-clientes")
    @ResponseBody
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarClientes();
    }
}