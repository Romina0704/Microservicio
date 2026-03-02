package com.microservicio.microserviciopedidos.client;

import com.microservicio.microserviciopedidos.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "producto-service", url = "http://40.82.168.11:8090")
public interface ProductoClient {

    @GetMapping("/api/productos")
    List<ProductoDTO> listarProductos();
}
