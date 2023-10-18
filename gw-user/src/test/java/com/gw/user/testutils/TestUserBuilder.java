package com.gw.user.testutils;

import com.gw.user.domain.User;
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
    private String authority = "USER";

    private TestUserBuilder() {
    }

    public static TestUserBuilder aUser() {
        return new TestUserBuilder();
    }

    public static TestUserBuilder copyOf(User currentUser) {
        TestUserBuilder testUserBuilder = new TestUserBuilder();

        testUserBuilder.id = currentUser.userId();
        testUserBuilder.username = currentUser.userName();
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
                .withRole(this.authority)
                .build();
    }

    public static UserCreateGrpcRequestDTO userCreateGrpcRequestDTOBuilder(User user) {
        return UserCreateGrpcRequestDTO.newBuilder()
                .setUserName(user.userName())
                .setFirstName(user.firstName())
                .setLastName(user.lastName())
                .setPassword(user.password())
                .build();
    }
}
