package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.Inform;
import com.yang.guseokgi.dto.Inform.InformBasic;
import com.yang.guseokgi.dto.Inform.InformWrite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InformRepository extends JpaRepository<Inform , Long> {

    @Query("select i from Inform i order by i.localDateTime desc")
    Page<Inform> findPageAll(Pageable pageable);

    @Query("select new com.yang.guseokgi.dto.Inform.InformWrite(i.title, i.article) from Inform i where i.id = :informId")
    Optional<InformWrite> findInformWriteById(@Param("informId")Long informId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Inform i where i.id = :informId")
    void deleteById(Long informId);

    @Query("select i from Inform i order by  i.localDateTime desc")
    Page<Inform> findInformPreviewAll(Pageable pageable);

    @Query("select new com.yang.guseokgi.dto.Inform.InformBasic(i) from Inform i where i.id = :informId")
    Optional<InformBasic> findInformBasicById(@Param("informId") Long informId);
}
