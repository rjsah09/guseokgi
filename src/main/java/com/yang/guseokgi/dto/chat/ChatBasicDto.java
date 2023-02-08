package com.yang.guseokgi.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatBasicDto {

    private Long tradeId;    //참조키

    private Long accountId;  //참조키

    private LocalDateTime chatTime;

    private String chatText;

    public ChatBasicDto(ChatBasic chatBasic) {
        this.tradeId = Long.parseLong(chatBasic.getRoomId());
        this.accountId = Long.parseLong(chatBasic.getWriter());
        this.chatTime = LocalDateTime.now();
    }

}
