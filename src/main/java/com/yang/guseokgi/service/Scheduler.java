package com.yang.guseokgi.service;

import com.yang.guseokgi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class Scheduler {

    private final PostRepository postRepository;

    @Scheduled(cron = "0 59 23 * * MON-SUN")
    public void dailyViewReset() {
        log.info("오늘 뷰 초기화");
        postRepository.dailyViewReset();
    }


}
