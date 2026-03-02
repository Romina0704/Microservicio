package com.microservicio.microserviciopedidos.client;
import com.microservicio.microserviciopedidos.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "cliente-service", url = "http://68.155.227.209")
public interface ClienteClient {

    @GetMapping("/api/clientes")
    List<ClienteDTO> listarClientes();
}