package com.yang.guseokgi.dto.account;

import com.yang.guseokgi.domain.Account;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountSession {

    private Long id;

    private String nickname;

    public AccountSession(Account account) {
        this.id = account.getId();
        this.nickname = account.getNickname();
    }

}
