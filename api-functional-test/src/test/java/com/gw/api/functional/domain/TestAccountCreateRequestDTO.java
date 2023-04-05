package com.gw.api.functional.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonDeserialize
public record TestAccountCreateRequestDTO(String userName,
                                          String password,
                                          String firstName,
                                          String lastName,
                                          String dateOfBirth, //Format: dd/mm/yyyy
                                          TestGender gender,
                                          String homeCountry) {}
