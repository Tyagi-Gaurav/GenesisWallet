package com.gw.user.resource.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.validation.annotation.Validated;


@JsonSerialize
@JsonDeserialize
@Validated
public record UserCreateResponseDTO(String userId) implements WithUserId {
}
