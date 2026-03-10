package com.microservicio.microserviciopedidos.client;

import com.microservicio.microserviciopedidos.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "producto-service", url = "http://20.221.105.223")
public interface ProductoClient {

    @GetMapping("/api/productos")
    List<ProductoDTO> listarProductos();
    // En ProductoClient.java
    @PutMapping("/api/productos/aumentar-stock")
    void aumentarStock(@RequestBody List<Map<String, Object>> items);
}
