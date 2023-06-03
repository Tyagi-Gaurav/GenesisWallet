package com.gw.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.EnumUtils;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Class<? extends Enum> enumClass;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        enumClass = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return EnumUtils.isValidEnum(enumClass, value);
    }
}
