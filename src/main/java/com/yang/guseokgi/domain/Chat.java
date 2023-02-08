package com.yang.guseokgi.domain;

import com.yang.guseokgi.domain.category.ChatCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Chat {

    //기본 컬럼//

    @Id
    @GeneratedValue
    @Column(name = "chat_history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trade_request_id")
    private Trade trade;    //참조키

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;  //참조키

    private LocalDateTime chatTime;

    private String chatText;

    @Enumerated(EnumType.STRING)
    private ChatCategory chatCategory;

    //추가 연관관계 설정//

    //constructor//
    public Chat(Trade trade, Account account, LocalDateTime localDateTime, String text, ChatCategory chatCategory) {
        this.trade = trade;
        this.account = account;
        this.chatTime = localDateTime;
        this.chatText = text;
        this.chatCategory = chatCategory;
    }

    public Chat(Trade trade, Account account, LocalDateTime localDateTime, String text) {
        this.trade = trade;
        this.account = account;
        this.chatTime = localDateTime;
        this.chatText = text;
    }

}
