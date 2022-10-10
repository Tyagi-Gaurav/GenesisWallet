package com.gw.user.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gw.common.domain.Gender;
import com.gw.user.resource.domain.AccountCreateRequestDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;

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
                "10/10/2010", Gender.FEMALE, "AUS",
                "USER");
    }

    public static AccountCreateRequestDTO testAccountCreateRequestDTO(String userName) {
        return new AccountCreateRequestDTO(
                userName, randomAlphabetic(6)
                , randomAlphabetic(7), randomAlphabetic(7),
                "10/10/2010", Gender.FEMALE, "AUS",
                "USER");
    }

    public static void executeQueryUpdate(DataSource dataSource, String query) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(true);
            assertThat(preparedStatement.executeUpdate())
                    .describedAs("Could not run query. {}", query)
                    .isNotNegative();
        }
    }
}
