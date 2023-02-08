package com.yang.guseokgi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Inform {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String article;

    private LocalDateTime localDateTime;

    public Inform(String title, String article) {
        this.title = title;
        this.article = article;
        this.localDateTime = LocalDateTime.now();
    }


}
