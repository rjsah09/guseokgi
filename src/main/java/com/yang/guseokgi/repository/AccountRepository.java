package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    //PK를 통해 Account 반환, Optional
    Optional<Account> findById(Long id);

    //uid select - 개수 반환
    @Query("select count(a) from Account a where a.uid = :uid")
    Long findCountByUid(@Param("uid") String uid);

    //nickname select - 개수 반환
    @Query("select count(a) from Account a where a.nickname = :nickname")
    Long findCountByNickname(@Param("nickname") String nickname);

    Optional<Account> findByUidAndPassword(String uid, String password);

    @Query("select a from Account a order by a.id")
    Page<Account> findAllManager(Pageable pageable);

    @Query("select a from Account a where a.uid like %:keyword% order by a.id")
    Page<Account> findAllByKeywordManager(Pageable pageable, String keyword);
}
