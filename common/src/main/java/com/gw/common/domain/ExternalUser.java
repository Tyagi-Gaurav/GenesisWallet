package com.gw.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize
public record ExternalUser(UUID id,
                           String userName,
                           String locale,
                           String pictureUrl,
                           String firstName,
                           String lastName,
                           String tokenValue,
                           String tokenType,
                           long tokenExpiryTime,
                           String externalSystem,
                           String dateOfBirth,
                           Gender gender,
                           String homeCountry) {
    public ExternalUser(UUID id, String email, String locale, String pictureUrl, String firstName, String lastName, String tokenValue, String tokenType, long tokenExpiryTime, String externalSystem) {
        this(id, email, locale, pictureUrl, firstName, lastName, tokenValue, tokenType, tokenExpiryTime, externalSystem, null, null, null);
    }
}
