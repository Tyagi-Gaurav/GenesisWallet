package com.gw.user.e2e.domain;

import com.gw.common.domain.Gender;
import com.gw.user.domain.User;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public record UserDetailsResponseDTO(@Id UUID id,
                                     String userName,
                                     String firstName,
                                     String lastName,
                                     String role,
                                     String dateOfBirth,
                                     Gender gender,
                                     String homeCountry) {
    public static UserDetailsResponseDTO fromUser(User user) {
        return new UserDetailsResponseDTO(user.userId(),
                user.name(),
                user.firstName(),
                user.lastName(),
                user.role(),
                user.dateOfBirth(),
                user.gender(),
                user.homeCountry());
    }
}
