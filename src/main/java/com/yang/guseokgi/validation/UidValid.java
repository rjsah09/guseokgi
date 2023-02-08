package com.yang.guseokgi.validation;

import com.yang.guseokgi.validation.impl.UidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UidValidator.class)
public @interface UidValid {

    String message() default "중복된 아이디 입니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
