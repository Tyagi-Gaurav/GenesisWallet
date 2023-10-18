package com.gw.user.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.user.resource.domain.LoginRequestDTO;
import com.gw.user.resource.domain.UserCreateRequestDTO;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class DtoBuilder {
    final static ObjectMapper mapper = new ObjectMapper();

    public static String asJsonString(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UserCreateRequestDTO testAccountCreateRequestDTO() {
        return new UserCreateRequestDTO(
                randomAlphabetic(4), randomAlphabetic(6)
                , randomAlphabetic(7), randomAlphabetic(7),
                "10/10/2010");
    }

    public static LoginRequestDTO testLoginRequestDTO() {
        return new LoginRequestDTO(
                randomAlphabetic(4), randomAlphabetic(6));
    }
}
