package com.gw.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class MinLengthValidator implements ConstraintValidator<MinLength, String> {

    private int minLength;

    @Override
    public void initialize(MinLength constraintAnnotation) {
        minLength = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNotEmpty(value) && value.length() >= minLength;
    }
}
