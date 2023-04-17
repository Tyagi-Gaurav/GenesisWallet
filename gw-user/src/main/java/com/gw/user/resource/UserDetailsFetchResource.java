package com.gw.user.resource;

import com.gw.user.resource.domain.UserDetailsFetchResponseDTO;
import com.gw.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;


@RestController
public class UserDetailsFetchResource {
    private final UserService userService;

    public UserDetailsFetchResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = "application/vnd+user.details.v1+json",
            path = "/user/details/{userId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<UserDetailsFetchResponseDTO> fetchUser(@PathVariable("userId") UUID userId) {
        return userService.findUserBy(userId)
                .map(user -> new UserDetailsFetchResponseDTO(
                        user.firstName(),
                        user.lastName(),
                        user.dateOfBirth(),
                        user.gender(),
                        user.homeCountry()
                ));
    }
}
