package com.gw.user.testutils;

import com.gw.common.domain.Gender;
import com.gw.common.domain.User;
import com.gw.user.grpc.UserCreateGrpcRequestDTO;

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

    public static UserCreateGrpcRequestDTO userCreateGrpcRequestDTOBuilder(User user) {
        return UserCreateGrpcRequestDTO.newBuilder()
                .setUserName(user.username())
                .setFirstName(user.firstName())
                .setLastName(user.lastName())
                .setDateOfBirth(user.dateOfBirth())
                .setGender(toGrpcGender(user.gender()))
                .setPassword(user.password())
                .setHomeCountry(user.homeCountry())
                .build();
    }

    public static com.gw.user.grpc.Gender toGrpcGender(Gender gender) {
        return switch(gender) {
            case MALE -> com.gw.user.grpc.Gender.GENDER_MALE;
            case FEMALE -> com.gw.user.grpc.Gender.GENDER_FEMALE;
            case UNSPECIFIED -> com.gw.user.grpc.Gender.GENDER_UNSPECIFIED;
        };
    }
}
