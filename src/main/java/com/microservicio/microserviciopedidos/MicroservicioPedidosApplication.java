package com.microservicio.microserviciopedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD

@SpringBootApplication
=======
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients
>>>>>>> f1292c2c3ce7b5b686491f4482c7d63d035d5133
public class MicroservicioPedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioPedidosApplication.class, args);
    }

}
