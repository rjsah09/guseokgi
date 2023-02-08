package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.Inquiry;
import com.yang.guseokgi.dto.inquiry.InquiryBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("select i from Inquiry i where i.account.id = :accountId")
    Page<Inquiry> findAllById(Pageable pageable, @Param("accountId")Long accountId);

    @Query("select i from Inquiry i where i.completed = false")
    Page<Inquiry> findAllManager(Pageable pageable);

    @Query("select i from Inquiry i where i.completed = false and i.account.uid = :keyword")
    Page<InquiryBasic> findAllByKeywordManager(Pageable pageable, @Param("keyword")String keyword);

}
