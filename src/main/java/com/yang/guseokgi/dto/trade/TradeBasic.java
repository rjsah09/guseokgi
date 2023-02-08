package com.yang.guseokgi.dto.trade;

import com.yang.guseokgi.domain.Trade;
import com.yang.guseokgi.domain.category.TradeStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class TradeBasic {

    private Set<WebSocketSession> sessions = new HashSet<>();

    private Long id;

    private Long myAccountId;

    private String myAccountNickname;

    private Long myPostId;

    private String myPostTitle;

    private boolean myPostDeleted;

    private Long otherAccountId;

    private String otherAccountNickname;

    private Long otherPostId;

    private String otherPostTitle;

    private boolean otherPostDeleted;

    private TradeStatus tradeStatus;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private boolean myPostCancel;

    private boolean otherPostCancel;

    private boolean myPostComplete;

    private boolean otherPostComplete;

    public TradeBasic(Trade trade) {
        this.id = trade.getId();

        this.myAccountId = trade.getMyPost().getAccount().getId();
        this.myAccountNickname = trade.getMyPost().getAccount().getNickname();
        this.myPostId = trade.getMyPost().getId();
        this.myPostTitle = trade.getMyPost().getTitle();
        this.myPostDeleted = trade.getMyPost().isDeleted();

        this.otherAccountId = trade.getOtherPost().getAccount().getId();
        this.otherAccountNickname = trade.getOtherPost().getAccount().getNickname();
        this.otherPostId = trade.getOtherPost().getId();
        this.otherPostTitle = trade.getOtherPost().getTitle();
        this.otherPostDeleted = trade.getOtherPost().isDeleted();

        this.tradeStatus = trade.getTradeStatus();
        this.startTime = trade.getStartTime();
        this.endTime = trade.getEndTime();

        this.myPostCancel = trade.isMyPostCancel();
        this.otherPostCancel = trade.isOtherPostCancel();
        this.myPostComplete = trade.isMyPostComplete();
        this.otherPostCancel = trade.isOtherPostCancel();
    }

}
