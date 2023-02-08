package com.yang.guseokgi.dto.trade;

import com.yang.guseokgi.domain.Trade;
import com.yang.guseokgi.domain.category.TradeStatus;
import com.yang.guseokgi.dto.chat.ChatBasic;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TradeManager {

    private Long id;

    private Long myAccountId;

    private String myAccountUid;

    private String myAccountNickname;

    private Long myPostId;

    private String myPostTitle;

    private Long otherAccountId;

    private String otherAccountUid;

    private String otherAccountNickname;

    private Long otherPostId;

    private String otherPostTitle;

    private TradeStatus tradeStatus;

    private LocalDateTime startTime;

    private boolean myPostCancel;

    private boolean otherPostCancel;

    private boolean myPostComplete;

    private boolean otherPostComplete;

    private List<ChatBasic> chatList;

    public TradeManager(Trade trade) {
        this.id = trade.getId();

        this.myAccountId = trade.getMyPost().getAccount().getId();
        this.myAccountUid = trade.getMyPost().getAccount().getUid();
        this.myAccountNickname = trade.getMyPost().getAccount().getNickname();
        this.myPostId = trade.getMyPost().getId();
        this.myPostTitle = trade.getMyPost().getTitle();

        this.otherAccountId = trade.getOtherPost().getAccount().getId();
        this.otherAccountUid = trade.getOtherPost().getAccount().getUid();
        this.otherAccountNickname = trade.getOtherPost().getAccount().getNickname();
        this.otherPostId = trade.getOtherPost().getId();
        this.otherPostTitle = trade.getOtherPost().getTitle();

        this.tradeStatus = trade.getTradeStatus();
        this.startTime = trade.getStartTime();

        this.myPostCancel = trade.isMyPostCancel();
        this.otherPostCancel = trade.isOtherPostCancel();
        this.myPostComplete = trade.isMyPostComplete();
        this.otherPostCancel = trade.isOtherPostCancel();
    }

}
