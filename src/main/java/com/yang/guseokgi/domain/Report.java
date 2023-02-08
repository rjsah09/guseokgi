package com.yang.guseokgi.domain;

import com.yang.guseokgi.domain.category.ReportCategory;
import com.yang.guseokgi.domain.category.ReportStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private ReportCategory reportCategory;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public Report(Post post, Account account, ReportCategory reportCategory, ReportStatus reportStatus) {
        this.post = post;
        this.account = account;
        this.reportCategory = reportCategory;
        this.reportStatus = reportStatus;
        this.startTime = LocalDateTime.now();
    }

}
