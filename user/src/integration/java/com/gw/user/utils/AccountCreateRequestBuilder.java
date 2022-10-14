package com.gw.user.utils;

import com.gw.common.domain.Gender;
import com.gw.user.resource.domain.AccountCreateRequestDTO;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class AccountCreateRequestBuilder {
    private String userName = randomAlphabetic(4);
    private final String password = randomAlphabetic(6);
    private final String firstName = randomAlphabetic(7);
    private final String lastName = randomAlphabetic(7);
    private String dateOfBirth = "10/10/2010";
    private final Gender gender = Gender.FEMALE;
    private String homeCountry = "AUS";

    private AccountCreateRequestBuilder() {}

    public static AccountCreateRequestBuilder accountCreateRequest() {
        return new AccountCreateRequestBuilder();
    }

    public AccountCreateRequestDTO build() {
        return new AccountCreateRequestDTO(
                userName, password, firstName, lastName,
                dateOfBirth, gender, homeCountry);
    }

    public AccountCreateRequestBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public AccountCreateRequestBuilder withDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public AccountCreateRequestBuilder withHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
        return this;
    }
}
