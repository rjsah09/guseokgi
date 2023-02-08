package com.yang.guseokgi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name ="account_id")
    private Account account;

    private String question;

    private LocalDateTime questionTime;

    private String answer;

    private LocalDateTime answerTime;

    private boolean completed;

    public Inquiry(Account account, String question) {
        this.account = account;
        this.question = question;
        this.questionTime = LocalDateTime.now();
        this.completed = false;
    }

}
