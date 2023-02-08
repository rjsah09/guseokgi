package com.yang.guseokgi.domain.category;


import java.util.Locale;

public enum PostCategory {
    STATIONERY("사무용품"),
    HOME_APPLIANCE("가전제품"),
    ELECTRONICS("전자기기"),
    MOBILE("모바일"),
    COMPUTER("컴퓨터"),
    CLOTHES("의류"),
    FURNITURE("가구"),
    KITCHEN("주방용품"),
    BABY("유아용품"),
    BOOK("도서"),
    FASHION_STUFF("패션잡화"),
    BEAUTY("뷰티"),
    SPORTS("스포츠"),
    PET("반려용품"),
    ELSE("기타");

    private String name;

    private String icon;

    private PostCategory(String name) {
        this.name = name;
        this.icon = "/categoryIcons/" + this.name().toLowerCase(Locale.ROOT) + ".png";
    }

    public String getName() {
        return this.name;
    }

    public String getIcon() { return this.icon; }

}

