package com.yang.guseokgi.domain.category;

public enum ChatCategory {
    IMAGE("이미지"),
    TEXT("텍스트"),
    SYSTEM("시스템"),
    SUCCEED("거래 완료"),
    CANCEL("거래 취소");

    private String name;

    private ChatCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
