package com.yang.guseokgi.service;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.dto.account.AccountManager;
import com.yang.guseokgi.dto.account.AccountRegister;
import com.yang.guseokgi.dto.account.AccountSession;
import com.yang.guseokgi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public void join(AccountRegister accountRegister) {

        Account account = new Account(
                accountRegister.getUid(),
                accountRegister.getPassword(),
                accountRegister.getNickname(),
                accountRegister.getName()
        );

        accountRepository.save(account);
    }

    public Optional<AccountSession> findByIdAndPassword(String uid, String password) {
        return accountRepository.findByUidAndPassword(uid, password).map(AccountSession::new);
    }

    public Optional<AccountSession> findAccountSessionById(Long id) {
        return accountRepository.findById(id).map(AccountSession::new);
    }

    public boolean isDuplicatedNickname(String value) {
        if(accountRepository.findCountByNickname(value) > 0)
            return true;
        return false;
    }

    public boolean isDuplicatedUid(String value) {
        if(accountRepository.findCountByUid(value) > 0)
            return true;
        return false;
    }

    public void editNickname(Long id, String nickname) {
        Account account = accountRepository.findById(id).get();
        account.setNickname(nickname);

    }

    public void editPassword(Long id, String newPassword) {
        Account account = accountRepository.findById(id).get();
        account.setPassword(newPassword);
    }

    public Page<AccountManager> findAllManager(Pageable pageable) {
        return accountRepository.findAllManager(pageable).map(AccountManager::new);
    }

    public Page<AccountManager> findAllByKeywordManager(Pageable pageable, String keyword) {
        return accountRepository.findAllByKeywordManager(pageable, keyword).map(AccountManager::new);
    }
}
