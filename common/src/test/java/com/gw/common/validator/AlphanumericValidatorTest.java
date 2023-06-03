package com.gw.common.validator;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AlphanumericValidatorTest extends AbstractValidatorTest {
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {".", " ", "abc$123"})
    void shouldThrowExceptionWhenValueIsAlphanumeric(String value) {
        TestAlphanumericAnnotation underTest = new TestAlphanumericAnnotation(value);
        Set<ConstraintViolation<TestAlphanumericAnnotation>> violations =
                validator.validate(underTest);
        assertThat(violations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "123", "abc123", "238abc"})
    void shouldValidateSuccessWhenValueIsAlphanumeric(String value) {
        TestAlphanumericAnnotation underTest = new TestAlphanumericAnnotation(value);
        Set<ConstraintViolation<TestAlphanumericAnnotation>> violations =
                validator.validate(underTest);
        assertThat(violations).isEmpty();
    }

    private record TestAlphanumericAnnotation(@AlphaNumeric String field) {}
}