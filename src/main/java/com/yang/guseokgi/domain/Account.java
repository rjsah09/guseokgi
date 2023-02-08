package com.yang.guseokgi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Account {

    //기본 컬럼//

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private String uid; //로그인시 아이디

    private String password;

    private String nickname;

    private String name;

    //private boolean deleted;

    //추가 연관관계 설정//

    @OneToMany(mappedBy = "account")
    List<Chat> chats;

    @OneToMany(mappedBy = "account")
    List<Post> posts;

    // 생성자 //
    public Account(String uid, String password, String nickname, String name) {
        this.uid = uid;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
    }

}