package com.yang.guseokgi.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostNicknameAndTitle {

    private Long id;

    private String nickname;

    private String title;

    private boolean deleted;

    public PostNicknameAndTitle(Long id, String nickname, String title, boolean deleted) {
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.deleted = deleted;
    }

}
