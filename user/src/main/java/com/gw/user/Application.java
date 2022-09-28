package com.gw.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ComponentScan({"com.gw.user"})
@EnableWebFlux
public class Application {
    public static void main(String[] args) {
        new GracefullyTerminatingApplication(Application.class, args).run();
    }
}
