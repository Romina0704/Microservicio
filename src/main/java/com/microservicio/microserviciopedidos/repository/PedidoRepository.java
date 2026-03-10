
package com.microservicio.microserviciopedidos.repository;

import com.microservicio.microserviciopedidos.entidad.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
