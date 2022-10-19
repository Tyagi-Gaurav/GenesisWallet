package com.gw.user.e2e.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gw.common.domain.Gender;

import java.util.UUID;

@JsonDeserialize
public record UserDetailsResponseDTO(String userName,
                                     String firstName,
                                     String lastName,
                                     String role,
                                     UUID id,
                                     String dateOfBirth,
                                     Gender gender,
                                     String homeCountry) {

}
