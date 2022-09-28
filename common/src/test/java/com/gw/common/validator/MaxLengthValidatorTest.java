package com.gw.common.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MaxLengthValidatorTest {
    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"wadhjashjdg", ""})
    void shouldThrowExceptionWhenMinLengthIsLessThanOrZero(String value) {
        TestMaxLengthValidator underTest = new TestMaxLengthValidator(value);
        Set<ConstraintViolation<TestMaxLengthValidator>> violations =
                validator.validate(underTest);
        assertThat(violations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "ds" , "123", "abc12", "238ab"})
    void shouldValidateSuccessWhenValueIsAlphanumeric(String value) {
        TestMaxLengthValidator underTest = new TestMaxLengthValidator(value);
        Set<ConstraintViolation<TestMaxLengthValidator>> violations =
                validator.validate(underTest);
        assertThat(violations).isEmpty();
    }

    private record TestMaxLengthValidator(@MaxLength(5) String field) {}
}