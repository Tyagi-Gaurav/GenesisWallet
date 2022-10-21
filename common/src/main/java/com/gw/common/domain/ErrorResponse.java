package com.gw.common.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
public record ErrorResponse(int statusCode, String message) {}
