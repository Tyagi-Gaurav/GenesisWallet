package com.gw.ping;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ComponentScan({
        "com.gw.ping.resource"})
@EnableWebFlux
public class Application {
    public static void main(String[] args) {
        new GracefullyTerminatingApplication(Application.class, args).run();
    }
}
