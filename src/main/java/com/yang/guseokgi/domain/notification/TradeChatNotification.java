package com.yang.guseokgi.domain.notification;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.domain.Trade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorColumn(name = "C")
@NoArgsConstructor
@Getter
@Setter
public class TradeChatNotification extends Notification{

    @ManyToOne
    @JoinColumn(name="trade_id")
    public Trade trade;

    public TradeChatNotification(Account account, String link, String text, Trade trade) {
        super(account, link, text);
        this.trade = trade;
    }

}
