package com.yang.guseokgi.dto.inquiry;

import com.yang.guseokgi.domain.Inquiry;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InquiryBasic {

    private Long id;
    private Long accountId;
    private String uid;
    private String nickname;
    private String question;
    private LocalDateTime questionTime;
    private String answer;
    private LocalDateTime answerTime;
    private boolean completed;

    public InquiryBasic(Inquiry inquiry) {
        this.id = inquiry.getId();
        this.accountId = inquiry.getAccount().getId();
        this.uid = inquiry.getAccount().getUid();
        this.nickname = inquiry.getAccount().getNickname();
        this.question = inquiry.getQuestion();
        this.questionTime = inquiry.getQuestionTime();
        this.answer = inquiry.getAnswer();
        this.answerTime = inquiry.getAnswerTime();
        this.completed = inquiry.isCompleted();
    }

}
