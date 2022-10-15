package com.gw.user.testutils;

import com.gw.common.domain.Gender;
import com.gw.common.domain.User;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class UserBuilder {

    private UUID id = UUID.randomUUID();
    private String password = randomAlphabetic(10);
    private String username = randomAlphabetic(10);
    private String firstName = randomAlphabetic(10);
    private String lastName = randomAlphabetic(10);
    private String dateOfBirth = "10/10/2010";
    private Gender gender = Gender.FEMALE;
    private String homeCountry = "AUS";
    private String authority = "USER";

    private UserBuilder() {
    }

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public static UserBuilder copyOf(User currentUser) {
        UserBuilder userBuilder = new UserBuilder();

        userBuilder.id = currentUser.id();
        userBuilder.username = currentUser.username();
        userBuilder.firstName = currentUser.firstName();
        userBuilder.lastName = currentUser.lastName();
        userBuilder.password = currentUser.password();
        userBuilder.authority = currentUser.role();

        return userBuilder;
    }

    public UserBuilder withUserName(String userName) {
        this.username = userName;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public User build() {
        return new User.UserBuilder().setId(this.id)
                .setFirstName(this.firstName)
                .setLastName(this.lastName)
                .setUsername(this.username)
                .setPassword(this.password)
                .setDateOfBirth(this.dateOfBirth)
                .setGender(this.gender)
                .setHomeCountry(this.homeCountry)
                .setRole(this.authority)
                .createUser();
    }
}
