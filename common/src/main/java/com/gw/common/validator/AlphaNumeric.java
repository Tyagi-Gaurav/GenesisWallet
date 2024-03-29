package com.gw.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlphanumericValidator.class)
public @interface AlphaNumeric {
    String message() default "{com.gw.common.constraints.AlphaNumeric.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
