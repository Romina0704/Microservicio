package com.microservicio.microserviciopedidos.service;

import com.microservicio.microserviciopedidos.entidad.DetallePedido;
import com.microservicio.microserviciopedidos.entidad.Pedido;
import com.microservicio.microserviciopedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDate.now());
        }

        // ✅ Solo establece la relación, NO limpies ni reasignes
        for (DetallePedido detalle : pedido.getDetalles()) {
            detalle.setPedido(pedido);
        }

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