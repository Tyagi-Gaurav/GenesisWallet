package com.gw.user.resource.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonDeserialize
public record UserDetailsFetchResponseDTO(
        String firstName,
        String lastName,
        String dateOfBirth) {
}
