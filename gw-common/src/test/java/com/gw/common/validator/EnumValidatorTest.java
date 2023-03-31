package com.gw.common.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EnumValidatorTest extends AbstractValidatorTest {
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"abc", "admin", "user", "aDmIn", "uSeR"})
    void shouldNotValidateAndThrowExceptionWhenNonEnumValuesProvided(String value) {
        TestValidEnumAnnotation underTest = new TestValidEnumAnnotation(value);
        Set<ConstraintViolation<TestValidEnumAnnotation>> violations =
                validator.validate(underTest);
        assertThat(violations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "USER"})
    void shouldValidateSuccessWhenEnumValuesProvided(String value) {
        TestValidEnumAnnotation underTest = new TestValidEnumAnnotation(value);
        Set<ConstraintViolation<TestValidEnumAnnotation>> violations =
                validator.validate(underTest);
        assertThat(violations).isEmpty();
    }

    private record TestValidEnumAnnotation(@ValidEnum(TestRole.class) String field) { }

    private enum TestRole {
        ADMIN,
        USER
    }
}