package com.gw.user.resource;

import com.gw.user.resource.domain2.UserDTO;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class UserCreateResourceV2 {

    @QueryMapping
    public Flux<UserDTO> allUsers() {
        return Flux.just(
                new UserDTO("user1"),
                new UserDTO("user2")
        );
    }
}
