package com.yang.guseokgi.dto.Inform;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class InformWrite {

    @NotEmpty(message = "제목을 입력해주세요")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요")
    private String article;

    public InformWrite(String title, String article) {
        this.title = title;
        this.article = article;
    }

}
