package com.gw.user.resource.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gw.common.annotations.GenerateBuilder;

@JsonSerialize
@JsonDeserialize
@GenerateBuilder
public record LoginRequestDTO(String userName, String password) { }
