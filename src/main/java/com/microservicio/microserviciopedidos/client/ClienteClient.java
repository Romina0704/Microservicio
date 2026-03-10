package com.microservicio.microserviciopedidos.client;
<<<<<<< HEAD

import com.microservicio.microserviciopedidos.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ClienteClient {

    @Value("${cliente.service.url}")
    private String clienteServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<ClienteDTO> listarClientes() {
        ClienteDTO[] clientes = restTemplate.getForObject(
                clienteServiceUrl + "/clientes",
                ClienteDTO[].class
        );
        return clientes != null ? Arrays.asList(clientes) : Collections.emptyList();
    }
=======
import com.microservicio.microserviciopedidos.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "cliente-service", url = "http://4.206.202.17:8081")
public interface ClienteClient {

    @GetMapping("/api/clientes")
    List<ClienteDTO> listarClientes();
>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133
}