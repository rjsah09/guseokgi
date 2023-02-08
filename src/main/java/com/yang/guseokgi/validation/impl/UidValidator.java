package com.yang.guseokgi.validation.impl;

import com.yang.guseokgi.service.AccountService;
import com.yang.guseokgi.validation.UidValid;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UidValidator implements ConstraintValidator<UidValid, String> {

    private final AccountService accountService;

    @Override
    public void initialize(UidValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(accountService.isDuplicatedUid(value))
            return false;
        return true;
    }

}
