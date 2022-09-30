package com.gw.user.resource;

import com.gw.user.resource.domain.AccountCreateRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class AccountCreateResource {

    @PostMapping(consumes = "application/vnd+user.create.v1+json",
            produces = "application/vnd+user.create.v1+json",
            path = "/user/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<Void> createAccount(@Valid @RequestBody AccountCreateRequestDTO accountCreateRequestDTO) {
        return Mono.empty();
    }
}
