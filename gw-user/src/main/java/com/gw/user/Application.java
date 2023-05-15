package com.gw.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ComponentScan({
        "com.gw.common",
        "com.gw.common.config",
        "com.gw.grpc.common",
        "com.gw.security.config",
        "com.gw.user.config",
        "com.gw.user.cache",
        "com.gw.user.exception",
        "com.gw.user.repo",
        "com.gw.user.resource",
        "com.gw.user.security",
        "com.gw.user.service"})
@EnableWebFlux
public class Application {
    public static void main(String[] args) {
        new GracefullyTerminatingApplication(Application.class, args).run();
    }
}
