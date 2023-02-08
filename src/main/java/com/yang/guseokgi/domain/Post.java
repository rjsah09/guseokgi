package com.yang.guseokgi.domain;

import com.yang.guseokgi.domain.category.PostCategory;
import com.yang.guseokgi.domain.category.PostStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {

    //기본 컬럼//

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account; //양방향 연관관계

    private LocalDateTime postTime;

    private String title;

    private String article;

    private Long views;

    private Long dailyViews;

    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    private boolean deleted;

    //추가 연관관계 설정//

    //NotificationHistory 단방향 연관관계

    //NotificationHistory 단방향 연관관계 2

    //Report 단방향 연관관계

    @OneToMany(mappedBy = "myPost")
    private List<Trade> sendRequest;

    @OneToMany(mappedBy = "otherPost")
    private List<Trade> receivedRequest;

    @OneToMany(mappedBy = "targetPost", cascade = CascadeType.ALL)
    private List<Img> imgList;

    public Post(Account account, String title, String article, PostCategory postCategory) {
        this.account = account;
        this.title = title;
        this.article = article;
        this.postCategory = postCategory;
        this.views = 0L;
        this.dailyViews = 0L;
        this.postTime = LocalDateTime.now();
        this.postStatus = PostStatus.WAITING;
        this.deleted = false;
    }

}
