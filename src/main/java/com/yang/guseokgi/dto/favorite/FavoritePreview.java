package com.yang.guseokgi.dto.favorite;

import com.yang.guseokgi.domain.Favorite;
import com.yang.guseokgi.domain.category.PostCategory;
import com.yang.guseokgi.domain.category.PostStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FavoritePreview {

    private Long id;

    private Long postId;

    private Long accountId;

    private String nickname;

    private LocalDateTime postTime;

    private String title;

    private Long views;

    private PostCategory postCategory;

    private PostStatus postStatus;

    private String thumbnail;

    public FavoritePreview(Favorite favorite) {
        this.id = favorite.getId();
        this.postId = favorite.getPost().getId();
        this.accountId = favorite.getPost().getAccount().getId();
        this.nickname = favorite.getPost().getAccount().getNickname();
        this.postTime = favorite.getPost().getPostTime();
        this.title = favorite.getPost().getTitle();
        this.views = favorite.getPost().getViews();
        this.postCategory = favorite.getPost().getPostCategory();
        this.postStatus = favorite.getPost().getPostStatus();
    }

}
