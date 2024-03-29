package com.gw.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gw.common.domain.UserIdentity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize
@Document
public record User(UUID userId,
                   String firstName,
                   String lastName,
                   String userName,
                   String password,
                   String salt,
                   String dateOfBirth,
                   String role) implements UserIdentity {
    public User(UUID id,
                String firstName,
                String lastName,
                String username,
                String password,
                String homeCountry,
                String role) {
        this(id, firstName, lastName, username, password, null, homeCountry, role);
    }

    public String generateSalt(long randomVal) {
        return lastName + randomVal + firstName;
    }

    @Override
    public String id() {
        return userId.toString();
    }

    public static class UserBuilder {
        private UUID id;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private String salt;
        private String dateOfBirth;
        private String role;

        private UserBuilder() {}

        public static UserBuilder copyOf(User user) {
            return User.aUser()
                    .withId(user.userId())
                    .withFirstName(user.firstName())
                    .withLastName(user.lastName())
                    .withUserName(user.userName())
                    .withPassword(user.password())
                    .withSalt(user.salt())
                    .withDateOfBirth(user.dateOfBirth())
                    .withRole(user.role());
        }

        public UserBuilder withId(UUID id) {
            this.id = id;
            return this;
        }

        public UserBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder withUserName(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public UserBuilder withRole(String role) {
            this.role = role;
            return this;
        }

        public UserBuilder withSalt(String salt) {
            this.salt = salt;
            return this;
        }

        public User build() {
            return new User(id, firstName, lastName, username, password, salt, dateOfBirth, role);
        }
    }

    public static User.UserBuilder aUser() {
        return new UserBuilder();
    }
}
