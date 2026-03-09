package com.microservicio.microserviciopedidos.service;

import com.microservicio.microserviciopedidos.entidad.Proveedor;
import com.microservicio.microserviciopedidos.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    public void guardarProveedor(Proveedor proveedor) {
        proveedorRepository.save(proveedor);
    }

    public Proveedor obtenerProveedor(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }
}