package com.gw.user.testutils;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.Gender;
import com.gw.common.domain.User;
import com.gw.user.grpc.UserCreateGrpcRequestDTO;

import java.time.Instant;
import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class ExternalUserBuilder {

    private UUID id = UUID.randomUUID();
    private String email = randomAlphabetic(10);
    private String firstName = randomAlphabetic(10);
    private String lastName = randomAlphabetic(10);
    private String dateOfBirth = "10/10/2010";
    private Gender gender = Gender.FEMALE;
    private String homeCountry = "AUS";
    private String locale = "en";
    private String pictureUrl = randomAlphabetic(10);
    private String tokenValue = randomAlphabetic(10);
    private String tokenType = randomAlphabetic(10);
    private long tokenExpiryTime = Instant.now().plusSeconds(10).toEpochMilli();
    private String externalSystem = "google";

    private ExternalUserBuilder() {
    }

    public static ExternalUserBuilder aExternalUser() {
        return new ExternalUserBuilder();
    }

    public ExternalUser build() {
        return new ExternalUser(id,
                email,
                locale,
                pictureUrl,
                firstName,
                lastName,
                tokenValue,
                tokenType,
                tokenExpiryTime,
                externalSystem,
                dateOfBirth,
                gender,
                homeCountry);
    }
}
