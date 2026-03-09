package com.microservicio.microserviciopedidos.service;

import com.microservicio.microserviciopedidos.client.ClienteClient;
import com.microservicio.microserviciopedidos.dto.ClienteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteClient clienteClient;

    public ClienteService(ClienteClient clienteClient) {
        this.clienteClient = clienteClient;
    }

    // 🔹 Listar todos los clientes
    public List<ClienteDTO> listarClientes() {
        return clienteClient.listarClientes();
    }

    // 🔹 Buscar cliente por ID (validación profesional)
    public ClienteDTO obtenerClientePorId(Long id) {
        return clienteClient.listarClientes()
                .stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // 🔹 Validar que el cliente exista antes de guardar pedido
    public boolean existeCliente(Long id) {
        return obtenerClientePorId(id) != null;
    }
}