package com.geotat.socks.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PercentageValidator implements ConstraintValidator<Percentage, Integer> {
    @Override
    public void initialize(Percentage constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return value >= 0 && value <= 100;
    }
}
