package com.yang.guseokgi.domain.notification;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorColumn(name = "P")
@NoArgsConstructor
@Getter
@Setter
public class FavoriteNotification extends Notification {

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public FavoriteNotification(Account account, String link, String text, Post post) {
        super(account, link, text);
        this.post = post;
    }
}
