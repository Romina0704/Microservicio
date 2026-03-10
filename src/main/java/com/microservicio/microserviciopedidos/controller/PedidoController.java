package com.microservicio.microserviciopedidos.controller;

import com.microservicio.microserviciopedidos.dto.ProductoDTO;
import com.microservicio.microserviciopedidos.entidad.DetallePedido;
import com.microservicio.microserviciopedidos.entidad.Pedido;
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


    public PedidoController(PedidoService pedidoService,
                            ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;

    }

    @GetMapping
    public String vistaPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarPedidos());
        return "pedidos";
    }

    @PostMapping("/agregar")
    public String agregarPedido(@RequestParam String fechaPedido,
                                @RequestParam String estado,
                                @RequestParam Long proveedorId,
                                @RequestParam(required = false) String detalle) {

        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDate.parse(fechaPedido));
        pedido.setEstado(estado);
        pedido.setProveedorId(proveedorId);

        if (detalle != null && !detalle.isBlank()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> items =
                        mapper.readValue(detalle, new TypeReference<List<Map<String, Object>>>() {});

                for (Map<String, Object> item : items) {
                    DetallePedido d = new DetallePedido();

                    Object idObj = item.get("productoId");
                    if (idObj == null) idObj = item.get("id_producto");
                    if (idObj == null) idObj = item.get("id");

                    d.setProductoId(Long.valueOf(idObj.toString()));
                    d.setCantidad(Integer.valueOf(item.get("cantidad").toString()));
                    d.setPrecio(Double.valueOf(item.get("precio").toString()));

                    Object ppObj = item.get("precioProveedor");
                    if (ppObj != null) d.setPrecioProveedor(Double.valueOf(ppObj.toString()));

                    Object pvObj = item.get("precioVenta");
                    if (pvObj != null) d.setPrecioVenta(Double.valueOf(pvObj.toString()));

                    System.out.println("✅ Detalle → productoId=" + d.getProductoId()
                            + " precio=" + d.getPrecio()
                            + " precioProveedor=" + d.getPrecioProveedor()
                            + " precioVenta=" + d.getPrecioVenta());

                    pedido.addDetalle(d);
                }

            } catch (Exception e) {
                System.out.println("❌ ERROR al parsear detalle: " + e.getMessage());
                e.printStackTrace();
            }
        }

        pedidoService.crearPedido(pedido);

        // ✅ Aumentar stock con precioVenta
        try {
            List<Map<String, Object>> stockItems = new ArrayList<>();
            for (DetallePedido d : pedido.getDetalles()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("idProducto", d.getProductoId());
                item.put("cantidad", d.getCantidad());
                item.put("precioVenta", d.getPrecioVenta());
                stockItems.add(item);
            }
            System.out.println("📦 Stock items enviados: " + stockItems);
            productoService.aumentarStock(stockItems);
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo aumentar stock: " + e.getMessage());
        }

        return "redirect:/pedidos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.obtenerPedidoConDetalles(id);
            if (pedido != null && !pedido.getDetalles().isEmpty()) {
                devolverStock(pedido);
            }
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo reducir stock: " + e.getMessage());
        }
        pedidoService.eliminarPedido(id);
        return "redirect:/pedidos";
    }

    @PostMapping("/editar")
    public String editarPedido(@RequestParam Long id,
                               @RequestParam String fechaPedido,
                               @RequestParam String estado) {

        Pedido pedido = pedidoService.obtenerPorId(id);
        if (pedido != null) {
            // Si cambia a CANCELADO, devolver stock
            if ("CANCELADO".equals(estado) && !"CANCELADO".equals(pedido.getEstado())) {
                try {
                    Pedido conDetalles = pedidoService.obtenerPedidoConDetalles(id);
                    if (conDetalles != null && !conDetalles.getDetalles().isEmpty()) {
                        devolverStock(conDetalles);
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ No se pudo aumentar stock al cancelar: " + e.getMessage());
                }
            }
            pedido.setFechaPedido(LocalDate.parse(fechaPedido));
            pedido.setEstado(estado);
            pedidoService.crearPedido(pedido);
            // Después de pedidoService.crearPedido(pedido);
            try {
                List<Map<String, Object>> stockItems = new ArrayList<>();
                for (DetallePedido d : pedido.getDetalles()) {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("idProducto", d.getProductoId());
                    item.put("cantidad", d.getCantidad());
                    stockItems.add(item);
                }
                productoService.aumentarStock(stockItems);
            } catch (Exception e) {
                System.out.println("⚠️ No se pudo reducir stock: " + e.getMessage());
            }

            return "redirect:/pedidos";
        }

        return "redirect:/pedidos";
    }

    // Método reutilizable para devolver stock
    private void devolverStock(Pedido pedido) {
        List<Map<String, Object>> items = new ArrayList<>();
        for (DetallePedido d : pedido.getDetalles()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("idProducto", d.getProductoId());
            item.put("cantidad", d.getCantidad());
            items.add(item);
        }
        productoService.aumentarStock(items);
    }
    @GetMapping("/detalles/listar-productos")
    @ResponseBody
    public List<ProductoDTO> listarProductos() {
        return productoService.listarProductos();
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

        Map<Long, String> nombresProductos = new LinkedHashMap<>();
        try {
            List<ProductoDTO> productos = productoService.listarProductos();
            for (ProductoDTO p : productos) {
                if (p.getIdProducto() != null) {
                    nombresProductos.put(p.getIdProducto(), p.getNombre());
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo cargar productos: " + e.getMessage());
        }

        model.addAttribute("nombresProductos", nombresProductos);
        return "detalle_pedido";
    }

}