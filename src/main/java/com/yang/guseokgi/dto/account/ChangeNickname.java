package com.yang.guseokgi.dto.account;

import com.yang.guseokgi.validation.NicknameValid;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class ChangeNickname {

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;

    @NotEmpty(message = "닉네임을 입력해주세요")
    @Pattern(regexp = "[a-zA-Z1-9가-힣|$]{2,12}|^$", message = "닉네임 형식이 올바르지 않습니다")
    @NicknameValid(message = "이미 존재하는 닉네임 입니다")
    private String nickname;

}
