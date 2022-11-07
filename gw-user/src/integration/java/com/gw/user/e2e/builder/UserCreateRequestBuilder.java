package com.gw.user.e2e.builder;

import com.gw.common.domain.Gender;
import com.gw.user.resource.domain.UserCreateRequestDTO;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class UserCreateRequestBuilder {
    private String userName = randomAlphabetic(4);
    private final String password = randomAlphabetic(6);
    private final String firstName = randomAlphabetic(7);
    private final String lastName = randomAlphabetic(7);
    private String dateOfBirth = "10/10/2010";
    private final Gender gender = Gender.FEMALE;
    private String homeCountry = "AUS";

    private UserCreateRequestBuilder() {}

    public static UserCreateRequestBuilder userCreateRequest() {
        return new UserCreateRequestBuilder();
    }

    public UserCreateRequestDTO build() {
        return new UserCreateRequestDTO(
                userName, password, firstName, lastName,
                dateOfBirth, gender, homeCountry);
    }

    public UserCreateRequestBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserCreateRequestBuilder withDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserCreateRequestBuilder withHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
        return this;
    }
}
