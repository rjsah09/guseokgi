package com.yang.guseokgi.dto.post;

import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.domain.category.PostCategory;
import com.yang.guseokgi.domain.category.PostStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PostPreview {

    private Long id;

    private Long accountId;

    private String nickname;

    private LocalDateTime postTime;

    private String title;

    private Long views;

    private PostCategory postCategory;

    private PostStatus postStatus;

    private String thumbnail;

    public PostPreview(Post post) {
        this.id = post.getId();
        this.accountId = post.getAccount().getId();
        this.nickname = post.getAccount().getNickname();
        this.postTime = post.getPostTime();
        this.title = post.getTitle();
        this.views = post.getViews();
        this.postCategory = post.getPostCategory();
        this.postStatus = post.getPostStatus();
    }

}
