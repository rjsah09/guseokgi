package com.yang.guseokgi.domain.notification;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.domain.Trade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorColumn(name = "T")
@NoArgsConstructor
@Getter
@Setter
public class TradeNotification extends Notification{

    @ManyToOne
    @JoinColumn(name = "trade_id")
    private Trade trade;

    public TradeNotification(Account account,String link, String text, Trade trade) {
        super(account, link, text);
        this.trade = trade;
    }

}
