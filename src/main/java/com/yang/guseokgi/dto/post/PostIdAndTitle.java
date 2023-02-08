package com.yang.guseokgi.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostIdAndTitle {

    private Long id;

    private String title;

    public PostIdAndTitle(Long id, String title) {
        this.id = id;
        this.title = title;
    }

}
