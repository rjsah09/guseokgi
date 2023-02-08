package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.domain.category.PostCategory;
import com.yang.guseokgi.domain.category.PostStatus;
import com.yang.guseokgi.dto.post.PostIdAndTitle;
import com.yang.guseokgi.dto.post.PostNicknameAndTitle;
import com.yang.guseokgi.dto.post.PostPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    //글 ID로 검색
    @Query("select p from Post p where p.id = :id")
    Optional<Post> findById(@Param("id")Long id);

    @Query("select p from Post p where p.deleted = false order by p.postTime desc")
    Page<Post> findAll(Pageable pageable);

    @Query("select new com.yang.guseokgi.dto.post.PostIdAndTitle(p.id, p.title) from Post p " +
            "where p.account.id = :id " +
            "and p.postStatus = com.yang.guseokgi.domain.category.PostStatus.WAITING " +
            "and p.deleted = false " +
            "order by p.postTime desc")
    List<PostIdAndTitle> findByIdNotCompleted(@Param("id") Long id);

    @Query("select p from Post p where p.postCategory = :postCategory and p.deleted = false order by p.postTime desc")
    Page<Post> findByPostCategory(Pageable pageable, @Param("postCategory")PostCategory postCategory);

    @Query("select p from Post p where p.postCategory = :postCategory and p.title like %:keyword% and p.deleted = false order by p.postTime desc")
    Page<Post> findByPostCategoryAndKeyword(Pageable pageable, PostCategory postCategory, @Param("keyword") String keyword);

    @Query("select p from Post p where  p.title like %:keyword% and p.deleted = false order by p.postTime desc")
    Page<Post> findAllByKeyword(Pageable pageable, @Param("keyword")String keyword);

    @Query("select p from Post p where p.account.id = :accountId and p.deleted = false order by p.postTime desc")
    Page<Post> findAllByAccountId(Pageable pageable, @Param("accountId")Long accountId);

    @Query("select p from Post p where p.account.id = :accountId and p.deleted = false and p.postStatus = :postStatus order by  p.postTime desc")
    Page<Post> findAllByAccountIdAndPostStatus(Pageable pageable, long accountId, PostStatus postStatus);

    @Query("select p from Post p " +
            "where p.account.id = :accountId " +
            "and (p.postStatus = com.yang.guseokgi.domain.category.PostStatus.WAITING or  p.postStatus = com.yang.guseokgi.domain.category.PostStatus.TRADING)" +
            "and p.deleted = false order by p.postTime desc")
    Page<Post> findWriterPost(Pageable pageable, @Param("accountId")Long accountId);

    @Query("select p from Post p " +
            "where p.account.id = :accountId " +
            "and p.postStatus = com.yang.guseokgi.domain.category.PostStatus.WAITING " +
            "and p.deleted = false " +
            "order by p.postTime asc")
    List<PostIdAndTitle> findIdAndTitleNotCompleted(Long accountId);

    @Query("select p from Post p where p.deleted = false and p.deleted = false order by p.postTime desc")
    Page<Post> findAllManager(Pageable pageable);

    @Query("select p from Post p where p.account.uid = :keyword and p.deleted = false and p.deleted = false order by p.postTime desc")
    Page<Post> findAllByKeywordManager(Pageable pageable, @Param("keyword") String keyword);

    @Query("select new com.yang.guseokgi.dto.post.PostNicknameAndTitle(p.id, p.account.nickname, p.title, p.deleted) " +
            "from Post p where p.id = :postId and p.deleted = false")
    Optional<PostNicknameAndTitle> findNicknameAndTitleById(@Param("postId") Long postId);

    @Query("update Post p set p.dailyViews = 0")
    @Modifying(clearAutomatically = true)
    void dailyViewReset();

    @Query("select p from Post p " +
            "where p.postStatus = com.yang.guseokgi.domain.category.PostStatus.WAITING " +
            "and p.deleted = false")
    Page<Post> findToday(Pageable pageable);

    @Query("select count(p) from Post p " +
            "where p.postStatus = com.yang.guseokgi.domain.category.PostStatus.TRADED " +
            "and p.account.id = :accountId " +
            "and p.deleted= false")
    Long findTradedByAccountId(@Param("accountId")Long accountId);

    @Query("select count(p) from Post p " +
            "where (p.postStatus = com.yang.guseokgi.domain.category.PostStatus.WAITING or p.postStatus = com.yang.guseokgi.domain.category.PostStatus.TRADING) " +
            "and p.account.id = :accountId " +
            "and p.deleted = false")
    Long findTradingByAccountId(long accountId);
}
