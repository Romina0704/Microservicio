package com.microservicio.microserviciopedidos.service;

import com.microservicio.microserviciopedidos.client.ProductoClient;
import com.microservicio.microserviciopedidos.dto.ProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoClient productoClient;

    public ProductoService(ProductoClient productoClient) {
        this.productoClient = productoClient;
    }

    public List<ProductoDTO> listarProductos() {
        return productoClient.listarProductos();
    }

    public ProductoDTO obtenerProductoPorId(Long id) {
        return productoClient.listarProductos()
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
