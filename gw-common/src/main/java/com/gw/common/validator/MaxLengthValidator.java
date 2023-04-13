package com.gw.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class MaxLengthValidator implements ConstraintValidator<MaxLength, String> {

    private int maxLength;

    @Override
    public void initialize(MaxLength constraintAnnotation) {
        maxLength = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNotEmpty(value) && value.length() <= maxLength;
    }
}
