package com.gw.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class AlphanumericValidator implements ConstraintValidator<AlphaNumeric, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isAlphanumeric(value);
    }
}
