package com.yang.guseokgi.dto.report;

import com.yang.guseokgi.domain.Report;
import com.yang.guseokgi.domain.category.ReportCategory;
import com.yang.guseokgi.domain.category.ReportStatus;
import com.yang.guseokgi.dto.post.PostBasic;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportBasic {

    private Long id;

    private Long postId;

    private PostBasic postBasic;

    private Long accountId;

    private String uid;

    private String nickname;

    private ReportCategory reportCategory;

    private ReportStatus reportStatus;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public ReportBasic(Report report) {
        this.id = report.getId();
        this.postId = report.getPost().getId();
        this.accountId = report.getAccount().getId();
        this.uid = report.getAccount().getUid();
        this.nickname = report.getAccount().getNickname();
        this.reportCategory = report.getReportCategory();
        this.reportStatus = report.getReportStatus();
        this.startTime = report.getStartTime();
        this.endTime = report.getEndTime();
    }
}
