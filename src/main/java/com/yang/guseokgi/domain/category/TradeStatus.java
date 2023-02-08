package com.yang.guseokgi.domain.category;


public enum TradeStatus {
    TRADING("거래중"),
    SUCCEED("거래 완료"),
    CANCEL("거래 취소");

    private String name;

    private TradeStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}