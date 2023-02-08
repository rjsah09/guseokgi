package com.yang.guseokgi.domain.category;

public enum PostStatus {
    WAITING("미거래"),
    TRADING("거래중"),
    TRADED("거래완료");

    private String name;

    private PostStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
