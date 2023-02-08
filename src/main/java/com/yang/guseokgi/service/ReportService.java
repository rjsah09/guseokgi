package com.yang.guseokgi.service;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.domain.Report;
import com.yang.guseokgi.domain.category.ReportStatus;
import com.yang.guseokgi.dto.post.PostBasic;
import com.yang.guseokgi.dto.report.ReportBasic;
import com.yang.guseokgi.dto.report.ReportSend;
import com.yang.guseokgi.repository.AccountRepository;
import com.yang.guseokgi.repository.ImgRepository;
import com.yang.guseokgi.repository.PostRepository;
import com.yang.guseokgi.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final ImgRepository imgRepository;

    public void save(ReportSend reportSend, Long postId, Long accountId) {
        Post post = postRepository.findById(postId).get();
        Account account = accountRepository.findById(accountId).get();

        Report report = new Report(
                post,
                account,
                reportSend.getReportCategory(),
                ReportStatus.WAITING
        );

        reportRepository.save(report);
    }

    public Page<ReportBasic> findAllManager(Pageable pageable) {
        Page<ReportBasic> list = reportRepository.findAllManager(pageable).map(ReportBasic::new);
        for(ReportBasic rb : list) {
            PostBasic postBasic = postRepository.findById(rb.getPostId()).map(PostBasic::new).get();
            rb.setPostBasic(postBasic);
            List<String> imgList = imgRepository.findListByPostId(rb.getPostId());
            List<String> newImgList = new ArrayList<>();
            for(String eachImg : imgList) {
                eachImg = "/image/" + eachImg;
                newImgList.add(eachImg);
            }
            rb.getPostBasic().setImgList(newImgList);
        }
        return list;
    }

    public Page<ReportBasic> findAllByKeywordManager(Pageable pageable, String keyword) {
        return reportRepository.findAllByKeywordManager(pageable, keyword).map(ReportBasic::new);
    }

    public Long rejectReport(Long reportId) {
        Report report = reportRepository.findById(reportId).get();
        Long postId = report.getPost().getId();
        reportRepository.rejectReportByPostId(postId);
        return report.getPost().getId();
    }

    public Long acceptReport(Long reportId) {
        Report report = reportRepository.findById(reportId).get();
        Long postId = report.getPost().getId();
        reportRepository.acceptReportByPostId(postId);
        return report.getPost().getId();
    }
}
