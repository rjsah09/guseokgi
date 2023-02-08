package com.yang.guseokgi.domain.category;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ReportStatus {
    WAITING("대기중"),
    ACCEPTED("처리완료"),
    DENIED("처리거절");

    private String name;

    private ReportStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
