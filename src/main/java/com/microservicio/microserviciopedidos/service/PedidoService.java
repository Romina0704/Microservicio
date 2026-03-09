package com.microservicio.microserviciopedidos.service;

<<<<<<< HEAD
import com.microservicio.microserviciopedidos.entidad.Pedido;
import com.microservicio.microserviciopedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;
=======
import com.microservicio.microserviciopedidos.entidad.DetallePedido;
import com.microservicio.microserviciopedidos.entidad.Pedido;
import com.microservicio.microserviciopedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

<<<<<<< HEAD
=======
    @Transactional
>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133
    public Pedido crearPedido(Pedido pedido) {
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDate.now());
        }
<<<<<<< HEAD
=======

        // ✅ Solo establece la relación, NO limpies ni reasignes
        for (DetallePedido detalle : pedido.getDetalles()) {
            detalle.setPedido(pedido);
        }

>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
<<<<<<< HEAD
}
// prueba jenkins 2026
=======

    public Pedido guardarPedido(Pedido pedido) {

        for (DetallePedido detalle : pedido.getDetalles()) {
            detalle.setPedido(pedido);
        }

        return pedidoRepository.save(pedido);
    }


    @Transactional(readOnly = true)
    public Pedido obtenerPedidoConDetalles(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido != null) {
            // Fuerza la carga de los detalles (evita LazyInitializationException)
            pedido.getDetalles().size();
        }
        return pedido;
    }
}
>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133
