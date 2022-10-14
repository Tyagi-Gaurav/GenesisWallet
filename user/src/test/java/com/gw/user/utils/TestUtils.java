package com.gw.user.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.common.domain.Gender;
import com.gw.user.resource.domain.AccountCreateRequestDTO;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class TestUtils {
    final static ObjectMapper mapper = new ObjectMapper();

    public static String asJsonString(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static AccountCreateRequestDTO testAccountCreateRequestDTO() {
        return new AccountCreateRequestDTO(
                randomAlphabetic(4), randomAlphabetic(6)
                , randomAlphabetic(7), randomAlphabetic(7),
                "10/10/2010", Gender.FEMALE, "AUS");
    }
}
