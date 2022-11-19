package com.gw.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize
public record User(UUID id,
                   String firstName,
                   String lastName,
                   String email,
                   String password,
                   String salt,
                   String dateOfBirth,
                   Gender gender,
                   String homeCountry,
                   String role) {


    public User(UUID id,
                String firstName,
                String lastName,
                String username,
                String password,
                String dateOfBirth,
                Gender gender,
                String homeCountry,
                String role) {
        this(id, firstName, lastName, username, password, null, dateOfBirth, gender, homeCountry, role);
    }

    public String generateSalt(long randomVal) {
        return lastName + randomVal + firstName;
    }

    public static class UserBuilder {
        private UUID id;
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private String salt;
        private String dateOfBirth;
        private Gender gender;
        private String homeCountry;
        private String role;

        public UserBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setEmail(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public UserBuilder setGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public UserBuilder setHomeCountry(String homeCountry) {
            this.homeCountry = homeCountry;
            return this;
        }

        public UserBuilder setRole(String role) {
            this.role = role;
            return this;
        }

        public UserBuilder setSalt(String salt) {
            this.salt = salt;
            return this;
        }

        public User createUser() {
            return new User(id, firstName, lastName, username, password, salt, dateOfBirth, gender, homeCountry, role);
        }
    }
}
