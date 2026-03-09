package com.microservicio.microserviciopedidos.repository;

import com.microservicio.microserviciopedidos.entidad.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}