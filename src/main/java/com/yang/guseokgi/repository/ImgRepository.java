package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImgRepository extends JpaRepository<Img, Long> {

    //글로 이미지 리스트 반환
    @Query("select i.fileName from Img i where i.targetPost.id = :id")
    List<String> findListByPostId(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("delete from Img i where i.targetPost.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);
}
