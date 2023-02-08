package com.yang.guseokgi.dto.Inform;

import com.yang.guseokgi.domain.Inform;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InformBasic {

    private Long id;

    private String title;

    private String article;

    private LocalDateTime localDateTime;

    public InformBasic(Inform inform) {
        this.id = inform.getId();
        this.title = inform.getTitle();
        this.article = inform.getArticle();
        this.localDateTime = inform.getLocalDateTime();
    }

}
