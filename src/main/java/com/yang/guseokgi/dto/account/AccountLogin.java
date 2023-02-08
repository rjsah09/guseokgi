package com.yang.guseokgi.dto.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class AccountLogin {

    @NotEmpty(message = "아이디를 입력하세요")
    private String uid;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password;

}
