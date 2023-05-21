package com.gw.ping.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PingResource {

    @GetMapping(consumes = "application/vnd+ping.v1+json",
            produces = "application/vnd+ping.v1+json",
            path = "/ping")
    @ResponseStatus(code = HttpStatus.OK)
    public Mono<String> ping() {
        return Mono.just("pong");
    }
}
