package com.yang.guseokgi.dto.Inform;

import com.yang.guseokgi.domain.Inform;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InformPreview {

    private Long id;

    private String title;

    private LocalDateTime localDateTime;

    public InformPreview(Inform inform) {
        this.id = inform.getId();
        this.title = inform.getTitle();
        this.localDateTime = inform.getLocalDateTime();
    }

}
