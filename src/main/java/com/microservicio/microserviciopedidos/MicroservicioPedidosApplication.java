package com.microservicio.microserviciopedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
public class MicroservicioPedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioPedidosApplication.class, args);
    }

}
