package com.gw.common.validator;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MinLengthValidatorTest extends AbstractValidatorTest {
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "a", "ab"})
    void shouldThrowExceptionWhenMinLengthIsLessThanOrZero(String value) {
        TestMinLengthValidator underTest = new TestMinLengthValidator(value);
        Set<ConstraintViolation<TestMinLengthValidator>> violations =
                validator.validate(underTest);
        assertThat(violations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "123", "abc123", "238abc"})
    void shouldValidateSuccessWhenValueIsAlphanumeric(String value) {
        TestMinLengthValidator underTest = new TestMinLengthValidator(value);
        Set<ConstraintViolation<TestMinLengthValidator>> violations =
                validator.validate(underTest);
        assertThat(violations).isEmpty();
    }

    private record TestMinLengthValidator(@MinLength(3) String field) {}
}