package com.gw.api.functional.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonDeserialize
public record TestUserDetailsFetchResponseDTO(String firstName,
                                              String lastName,
                                              String dateOfBirth) {}
