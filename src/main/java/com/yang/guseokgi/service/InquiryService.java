package com.yang.guseokgi.service;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.domain.Inquiry;
import com.yang.guseokgi.dto.inquiry.InquiryBasic;
import com.yang.guseokgi.repository.AccountRepository;
import com.yang.guseokgi.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final AccountRepository accountRepository;

    public Page<InquiryBasic> findByAccoundId(Pageable pageable, Long accountId) {
        return inquiryRepository.findAllById(pageable, accountId).map(InquiryBasic::new);
    }

    public void save(String text, Long accountId) {
        Account account = accountRepository.findById(accountId).get();
        Inquiry inquiry = new Inquiry(account, text);
        inquiryRepository.save(inquiry);
    }

    public Page<InquiryBasic> findAllManager(Pageable pageable) {
        return inquiryRepository.findAllManager(pageable).map(InquiryBasic::new);
    }

    public Page<InquiryBasic> findAllByKeywordManager(Pageable pageable, String keyword) {
        return inquiryRepository.findAllByKeywordManager(pageable, keyword);
    }

    public Long inquiryReply(Long inquiryId, String text) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId).get();

        if(!inquiry.isCompleted()) {
            inquiry.setAnswer(text);
            inquiry.setAnswerTime(LocalDateTime.now());
            inquiry.setCompleted(true);
        } else {
            return -1L;
        }

        return inquiry.getAccount().getId();
    }

    public Optional<InquiryBasic> findById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId).map(InquiryBasic::new);
    }
}
