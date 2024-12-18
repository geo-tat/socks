package com.geotat.socks.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PercentageValidator.class)
public @interface Percentage {
    String message() default "Invalid percentage";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
