package com.yang.guseokgi.domain.notification;

import com.yang.guseokgi.domain.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    @Column(name="notification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    private LocalDateTime localDateTime;

    private boolean checked;

    private String link;

    private String text;

    public Notification(Account account, String link, String text) {
        this.account = account;
        this.localDateTime = LocalDateTime.now();
        this.checked = false;
        this.link = link;
        this.text = text;

    }

}
