package com.microservicio.microserviciopedidos.controller;

import com.microservicio.microserviciopedidos.dto.ClienteDTO;
import com.microservicio.microserviciopedidos.dto.ProductoDTO;
import com.microservicio.microserviciopedidos.entidad.DetallePedido;
import com.microservicio.microserviciopedidos.entidad.Pedido;
import com.microservicio.microserviciopedidos.service.ClienteService;
import com.microservicio.microserviciopedidos.service.PedidoService;
import com.microservicio.microserviciopedidos.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
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
                                @RequestParam Long clienteId,
                                @RequestParam(required = false) String detalle) {

        System.out.println("=== PEDIDO ===");
        System.out.println("Detalle raw: [" + detalle + "]");
        System.out.println("==============");

        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDate.parse(fechaPedido));
        pedido.setEstado(estado);
        pedido.setClienteId(clienteId);

        if (detalle != null && !detalle.isBlank()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> items =
                        mapper.readValue(detalle, new TypeReference<List<Map<String, Object>>>() {});

                System.out.println("Items parseados: " + items.size());

                for (Map<String, Object> item : items) {
                    DetallePedido d = new DetallePedido();

                    // Busca productoId, id_producto o id en ese orden
                    Object idObj = item.get("productoId");
                    if (idObj == null) idObj = item.get("id_producto");
                    if (idObj == null) idObj = item.get("id");

                    System.out.println("ID encontrado: " + idObj);

                    d.setProductoId(Long.valueOf(idObj.toString()));
                    d.setCantidad(Integer.valueOf(item.get("cantidad").toString()));
                    d.setPrecio(Double.valueOf(item.get("precio").toString()));
                    pedido.addDetalle(d);
                }

            } catch (Exception e) {
                System.out.println("❌ ERROR al parsear detalle: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ Detalle llegó vacío o null");
        }

        System.out.println("Detalles en pedido antes de guardar: " + pedido.getDetalles().size());
        System.out.println("DETALLES EN CONTROLLER: " + pedido.getDetalles().size());
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

    @GetMapping("/detalles/listar-productos")
    @ResponseBody
    public List<ProductoDTO> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/listar-clientes")
    @ResponseBody
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarClientes();
    }

    @PostMapping
    public Pedido guardar(@RequestBody Pedido pedido) {
        return pedidoService.guardarPedido(pedido);
    }

    @GetMapping("/detalle/{id}")
    public String detallePedido(@PathVariable Long id, Model model) {
        Pedido pedido = pedidoService.obtenerPedidoConDetalles(id);
        if (pedido == null) return "redirect:/pedidos";
        model.addAttribute("pedido", pedido);
        return "detalle_pedido";
    }

}