package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.domain.Favorite;
import com.yang.guseokgi.dto.favorite.FavoritePreview;
import com.yang.guseokgi.dto.post.PostPreview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("select count(f) from Favorite f where f.account.id = :accountId")
    Long findCountByAccountId(@Param("accountId") Long accountId);

    @Query("delete from Favorite f where f.post.id = :postId and f.account.id = :accountId")
    @Modifying(clearAutomatically = true)
    void deleteByPostIdAndAccountId(@Param("postId")Long postId, @Param("accountId")Long accountId);

    @Query("select f.account from Favorite f where f.post.id = :postId")
    List<Account> findByNotificationPostId(@Param("postId")Long postId);

    @Query("select new com.yang.guseokgi.dto.favorite.FavoritePreview(f) from Favorite f where f.account.id = :accountId")
    List<FavoritePreview> findPostPreviewByAccountId(@Param("accountId")Long accountId);

    @Query("delete from Favorite f where f.id = :favoriteId")
    @Modifying(clearAutomatically = true)
    void deleteById(@Param("favoriteId")Long favoriteId);

    @Query("select count(f) from Favorite f where f.post.id = :postId and f.account.id = :accountId")
    Long findCountByPostIdAndAccountId(Long postId, Long accountId);

}
