package com.yang.guseokgi.domain.category;

public enum ReportCategory {
    ADVERTISEMENT("부적절한 홍보"),
    SEXUAL("선정적인 내용"),
    CASH("현금거래 유도"),
    MISMATCH("제목과 관련없는 물품"),
    ELSE("기타");

    private String name;

    private ReportCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
