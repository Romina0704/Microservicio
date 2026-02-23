package com.microservicio.microserviciopedidos.client;

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
}