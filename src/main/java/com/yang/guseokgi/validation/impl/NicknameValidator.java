package com.yang.guseokgi.validation.impl;

import com.yang.guseokgi.service.AccountService;
import com.yang.guseokgi.validation.NicknameValid;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class NicknameValidator implements ConstraintValidator<NicknameValid, String> {

    private final AccountService accountService;

    @Override
    public void initialize(NicknameValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(accountService.isDuplicatedNickname(value))
            return false;
        return true;
    }

}
