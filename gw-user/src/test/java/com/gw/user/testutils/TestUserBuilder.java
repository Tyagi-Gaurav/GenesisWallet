package com.gw.user.testutils;

import com.gw.common.domain.ExternalUser;
import com.gw.common.domain.Gender;
import com.gw.user.domain.User;
import com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO;
import com.gw.user.grpc.UserCreateGrpcRequestDTO;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class TestUserBuilder {

    private UUID id = UUID.randomUUID();
    private String password = randomAlphabetic(10);
    private String username = randomAlphabetic(10);
    private String firstName = randomAlphabetic(10);
    private String lastName = randomAlphabetic(10);
    private String salt = randomAlphabetic(10);
    private String dateOfBirth = "10/10/2010";
    private Gender gender = Gender.FEMALE;
    private String homeCountry = "AUS";
    private String authority = "USER";

    private TestUserBuilder() {
    }

    public static TestUserBuilder aUser() {
        return new TestUserBuilder();
    }

    public static TestUserBuilder copyOf(User currentUser) {
        TestUserBuilder testUserBuilder = new TestUserBuilder();

        testUserBuilder.id = currentUser.userId();
        testUserBuilder.username = currentUser.name();
        testUserBuilder.firstName = currentUser.firstName();
        testUserBuilder.lastName = currentUser.lastName();
        testUserBuilder.password = currentUser.password();
        testUserBuilder.authority = currentUser.role();
        testUserBuilder.salt = currentUser.salt();

        return testUserBuilder;
    }

    public TestUserBuilder withUserName(String userName) {
        this.username = userName;
        return this;
    }

    public TestUserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public TestUserBuilder withSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public User build() {
        return User.aUser()
                .withId(this.id)
                .withFirstName(this.firstName)
                .withLastName(this.lastName)
                .withUserName(this.username)
                .withPassword(this.password)
                .withSalt(this.salt)
                .withDateOfBirth(this.dateOfBirth)
                .withGender(this.gender)
                .withHomeCountry(this.homeCountry)
                .withRole(this.authority)
                .build();
    }

    public static UserCreateGrpcRequestDTO userCreateGrpcRequestDTOBuilder(User user) {
        return UserCreateGrpcRequestDTO.newBuilder()
                .setUserName(user.name())
                .setFirstName(user.firstName())
                .setLastName(user.lastName())
                .setDateOfBirth(user.dateOfBirth())
                .setGender(toGrpcGender(user.gender()))
                .setPassword(user.password())
                .setHomeCountry(user.homeCountry())
                .build();
    }

    public static ExternalUserCreateGrpcRequestDTO externalUserCreateGrpcRequestDTOBuilder(ExternalUser externalUser) {
        return ExternalUserCreateGrpcRequestDTO.newBuilder()
                .setFirstName(externalUser.firstName())
                .setLastName(externalUser.lastName())
                .setGender(toGrpcGender(externalUser.gender()))
                .setEmail(externalUser.email())
                .setExternalSystem(externalUser.externalSystem())
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
