package com.yang.guseokgi.dto.notification;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationBasic {

    private Long id;

    private LocalDateTime localDateTime;

    private String link;

    private String text;

    public NotificationBasic(Long id, LocalDateTime localDateTime, String link, String text) {
        this.id = id;
        this.localDateTime = localDateTime;
        this.link = link;
        this.text = text;
    }

}
