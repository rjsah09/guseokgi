package com.yang.guseokgi.domain;

import com.yang.guseokgi.domain.category.TradeStatus;
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
public class Trade {

    //기본 컬럼//

    @Id
    @GeneratedValue
    @Column(name = "trade_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "my_post_id")
    private Post myPost;  //내 물품

    @ManyToOne
    @JoinColumn(name = "other_post_id")
    private Post otherPost;  //상대방의 물품

    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean myPostCancel;

    private boolean otherPostCancel;

    private boolean myPostComplete;

    private boolean otherPostComplete;

    //추가 연관관계 설정

    @OneToMany(mappedBy = "trade")
    private List<Chat> chatHistories;

    public Trade(Post myPost, Post otherPost, TradeStatus tradeStatus) {
        this.myPost = myPost;
        this.otherPost = otherPost;
        this.tradeStatus = tradeStatus;
        this.startTime = LocalDateTime.now();
        this.myPostCancel = false;
        this.myPostComplete = false;
        this.otherPostCancel = false;
        this.otherPostComplete = false;
    }

}