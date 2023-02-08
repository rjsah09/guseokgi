package com.yang.guseokgi.dto.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ChangePassword {

    @NotEmpty(message = "현재 비밀번호를 입력해주세요")
    private String password;

    @NotEmpty(message = "새 비밀번호를 입력해주세요")
    private String newPassword;

    @NotEmpty(message = "새 비밀번호를 다시 한번 입력해주세요")
    private String newPasswordConfirm;
}
