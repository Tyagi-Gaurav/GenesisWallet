package com.gw.user.resource.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gw.common.domain.Gender;

import java.util.UUID;

@JsonSerialize
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
