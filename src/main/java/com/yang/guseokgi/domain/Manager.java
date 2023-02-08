package com.yang.guseokgi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Manager {

    @Id
    private Long id;

    private String uid;

    private String password;

}
