package com.gw.user.resource.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gw.common.domain.Gender;

@JsonSerialize
@JsonDeserialize
public record UserDetailsFetchResponseDTO(
        String firstName,
        String lastName,
        String dateOfBirth,
        Gender gender,
        String homeCountry) {
}
