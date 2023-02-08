package com.yang.guseokgi.dto.inquiry;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class InquirySend {

    @NotEmpty(message = "문의 내용을 입력해주세요")
    private String text;

}
