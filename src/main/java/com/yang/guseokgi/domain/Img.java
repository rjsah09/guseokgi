package com.yang.guseokgi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Img {

    //기본 컬럼//

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private String fileOriName;

    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "target_post_id")
    private Post targetPost;

}