package com.yang.guseokgi.validation;


import com.yang.guseokgi.validation.impl.NicknameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NicknameValidator.class)
public @interface NicknameValid {

    String message() default "중복된 닉네임 입니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
