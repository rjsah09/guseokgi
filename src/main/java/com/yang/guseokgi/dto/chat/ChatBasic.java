package com.yang.guseokgi.dto.chat;

import com.yang.guseokgi.domain.category.ChatCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ChatBasic {

    private String roomId;

    private String writer;     //작성자

    private String message;    //추후 수정 가능

    private String chatCategory;

    public ChatBasic(Long roomId, Long writer, String message, ChatCategory chatCategory) {
        this.roomId = roomId.toString();
        this.writer = writer.toString();
        this.message = message;
        this.chatCategory = chatCategory.name();
    }

}
