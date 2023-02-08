package com.yang.guseokgi.dto.post;

import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.domain.category.PostCategory;
import com.yang.guseokgi.domain.category.PostStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class PostBasic {

    private Long id;

    private Long accountId;

    private String nickname;

    private LocalDateTime postTime;

    private String title;

    private String article;

    private Long views;

    private PostCategory postCategory;

    private PostStatus postStatus;

    private List<String> imgList;

    private boolean deleted;

    public PostBasic(Post post) {
        this.id = post.getId();
        this.accountId = post.getAccount().getId();
        this.nickname = post.getAccount().getNickname();
        this.title = post.getTitle();
        this.postTime = post.getPostTime();
        this.article = post.getArticle();
        this.views = post.getViews();
        this.postCategory = post.getPostCategory();
        this.postStatus = post.getPostStatus();
        this.deleted = post.isDeleted();
    }

}
