package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select r from Report r where r.reportStatus = com.yang.guseokgi.domain.category.ReportStatus.WAITING")
    Page<Report> findAllManager(Pageable pageable);

    @Query("select r from Report r where r.reportStatus = com.yang.guseokgi.domain.category.ReportStatus.WAITING and r.account.uid = :keyword")
    Page<Report> findAllByKeywordManager(Pageable pageable, @Param("keyword")String uid);

    @Query("update Report r set r.reportStatus = com.yang.guseokgi.domain.category.ReportStatus.DENIED where r.post.id = :postId")
    @Modifying(clearAutomatically = true)
    void rejectReportByPostId(@Param("postId")Long postId);

    @Query("update Report r set r.reportStatus = com.yang.guseokgi.domain.category.ReportStatus.ACCEPTED where r.post.id = :postId")
    @Modifying(clearAutomatically = true)
    void acceptReportByPostId(@Param("postId")Long postId);
}
