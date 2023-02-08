package com.yang.guseokgi.dto.account;

import com.yang.guseokgi.domain.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountManager {

    private Long id;

    private String uid;

    private String nickname;

    public AccountManager(Account account) {
        this.id = account.getId();
        this.uid = account.getUid();
        this.nickname = account.getNickname();
    }

}
