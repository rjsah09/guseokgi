package com.yang.guseokgi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class ChatImg {

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private String fileOriName;

    private String fileUrl;

}
