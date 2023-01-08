package com.gw.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.gw.common",
        "com.gw.grpc.common",
        "com.gw.ui.config",
        "com.gw.ui.service",
        "com.gw.ui.resource"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
