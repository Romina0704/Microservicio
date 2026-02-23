package com.microservicio.microserviciopedidos.repository;

import com.microservicio.microserviciopedidos.entidad.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}