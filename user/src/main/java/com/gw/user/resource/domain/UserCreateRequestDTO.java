package com.gw.user.resource.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gw.common.domain.Gender;
import com.gw.common.validator.AlphaNumeric;
import com.gw.common.validator.MaxLength;
import com.gw.common.validator.MinLength;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@JsonSerialize
@JsonDeserialize
@Valid
public record UserCreateRequestDTO(
        @NotNull @MinLength(4) @MaxLength(20) String userName,
        @AlphaNumeric @MinLength(6) @MaxLength(15) String password,
        @MinLength(3) @MaxLength(30) String firstName,
        @MinLength(3) @MaxLength(30) String lastName,
        @NotNull @Pattern(regexp = "^(3[0-1]|[1-2]\\d|0[1-9])\\/((1[0-2])|0[1-9])\\/((19|20)\\d{2})$") String dateOfBirth,
        Gender gender,
        @NotNull @Pattern(regexp = "^[A-Z]{3}$") String homeCountry
) {
}
