package com.yang.guseokgi.dto.account;

import com.yang.guseokgi.validation.NicknameValid;
import com.yang.guseokgi.validation.UidValid;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class AccountRegister {

    @NotEmpty(message = "아이디를 입력해주세요")
    @Pattern(regexp = "[a-zA-Z0-9]{5,15}|[a-zA-Z]{5,15}|^$", message = "아이디 형식이 올바르지 않습니다")
    @UidValid( message = "이미 존재하는 아이디 입니다")
    private String uid;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;

    @NotEmpty(message = "비밀번호를 다시 한번 입력해주세요")
    private String passwordConfirm;

    @NotEmpty(message = "닉네임을 입력해주세요")
    @Pattern(regexp = "[a-zA-Z1-9가-힣|$]{2,12}|^$", message = "닉네임 형식이 올바르지 않습니다")
    @NicknameValid(message = "이미 존재하는 닉네임 입니다")
    private String nickname;

    @NotEmpty(message = "이름을 입력해주세요")
    @Pattern(regexp = "[a-zA-Z가-힣]{2,}|^$", message = "올바른 이름이 아닙니다")
    private String name;

}
