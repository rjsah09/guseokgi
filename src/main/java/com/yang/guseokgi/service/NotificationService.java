package com.yang.guseokgi.service;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.domain.Trade;
import com.yang.guseokgi.domain.category.TradeStatus;
import com.yang.guseokgi.domain.notification.FavoriteNotification;
import com.yang.guseokgi.domain.notification.Notification;
import com.yang.guseokgi.domain.notification.TradeChatNotification;
import com.yang.guseokgi.domain.notification.TradeNotification;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import com.yang.guseokgi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TradeRepository tradeRepository;
    private final AccountRepository accountRepository;
    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;

    //거래 시작 알림 생성
    public void tradeStartNotification(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).get();

        TradeNotification tradeNotification1 = new TradeNotification(
                trade.getMyPost().getAccount(),
                "/chat/room/" + tradeId,
                "새로운 거래가 있습니다 : " + trade.getMyPost().getTitle() + ", " + trade.getOtherPost().getTitle(),
                trade
        );
        notificationRepository.save(tradeNotification1);

        TradeNotification tradeNotification2 = new TradeNotification(
                trade.getOtherPost().getAccount(),
                "/chat/room/" + tradeId,
                "새로운 거래가 있습니다 : " + trade.getMyPost().getTitle() + ", " + trade.getOtherPost().getTitle(),
                trade
        );
        notificationRepository.save(tradeNotification2);
    }

    //거래 완료 알림 생성
    public void tradeSucceedNotification(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).get();

        TradeNotification tradeNotification1 = new TradeNotification(
                trade.getMyPost().getAccount(),
                "/trade/history/",
                "거래가 완료되었습니다 : " + trade.getMyPost().getTitle() + ", " + trade.getOtherPost().getTitle(),
                trade
        );
        notificationRepository.save(tradeNotification1);

        TradeNotification tradeNotification2 = new TradeNotification(
                trade.getOtherPost().getAccount(),
                "/trade/history/",
                "거래가 완료되었습니다 : " + trade.getMyPost().getTitle() + ", " + trade.getOtherPost().getTitle(),
                trade
        );
        notificationRepository.save(tradeNotification2);
    }

    //거래 취소 알림 생성
    public void tradeCancelNotification(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).get();

        TradeNotification tradeNotification1 = new TradeNotification(
                trade.getMyPost().getAccount(),
                "/trade/history/",
                "거래가 취소되었습니다 : " + trade.getMyPost().getTitle() + ", " + trade.getOtherPost().getTitle(),
                trade
        );
        notificationRepository.save(tradeNotification1);

        TradeNotification tradeNotification2 = new TradeNotification(
                trade.getOtherPost().getAccount(),
                "/trade/history/",
                "거래가 취소되었습니다 : " + trade.getMyPost().getTitle() + ", " + trade.getOtherPost().getTitle(),
                trade
        );
        notificationRepository.save(tradeNotification2);
    }

    //새로운 채팅 -> 이전의 TradeChatNotification 읽음처리 후 새로운 알림 생성
    public void newChatNotification(Long tradeId, Long accountId, String text) {
        Trade trade = tradeRepository.findById(tradeId).get();
        Account account = accountRepository.findById(accountId).get();

        TradeChatNotification notification = new TradeChatNotification(account,
                "/chat/room/" + tradeId,
                trade.getMyPost().getTitle() + ", " + trade.getOtherPost().getTitle() + " : " + text,
                trade);

        notificationRepository.save(notification);

    }

    //알림 읽음처리(알림을 클릭한 경우, 삭제 버튼을 누른 경우)
    public String notificationChecked(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).get();
        notification.setChecked(true);
        return "redirect:" + notification.getLink();
    }

    //알림 읽음처리(채팅방에 존재하는 경우, 벌크 업데이트)
    public void setChatChecked(Long tradeId, Long accountId) {
        notificationRepository.setChatChecked(tradeId, accountId);
    }

    //즐겨찾기의 글 미거래 상태 알림(거래가 취소된 경우)
    public void favoritePostTradeAbleNotification(Long postId) {
        List<Account> accountList = favoriteRepository.findByNotificationPostId(postId);
        Post post = postRepository.findById(postId).get();
        for(Account account : accountList) {
            FavoriteNotification favoriteNotification = new FavoriteNotification(
                    account,
                    "/post/view/" + postId,
                    "즐겨찾기 '" + post.getTitle() + "'의 거래 신청이 가능합니다",
                    post
            );
            notificationRepository.save(favoriteNotification);
        }
    }

    //즐겨찾기의 글 거래완료 상태 알림(거래가 성사된 경우)
    public void favoritePostTradeUnableNotification(Long postId) {
        List<Account> accountList = favoriteRepository.findByNotificationPostId(postId);
        Post post = postRepository.findById(postId).get();
        for(Account account : accountList) {
            FavoriteNotification favoriteNotification = new FavoriteNotification(
                    account,
                    "/post/view/" + postId,
                    "즐겨찾기 '" + post.getTitle() + "'이(가) 판매되었습니다.",
                    post
            );
            notificationRepository.save(favoriteNotification);
        }
    }

    //문의 답변 알림(문의에 답변이 온 경우)
    public void inquiryRepliedNotification(Long accountId) {
        Account account = accountRepository.findById(accountId).get();

        Notification notification = new Notification(
                account,
                "/inquiry/myInquiry",
                "문의에 대한 답변이 있습니다"
        );
        notificationRepository.save(notification);
    }

    //관리자에 의한 거래 해제
    public void tradeRejectNotification(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).get();

        if(trade.getTradeStatus() == TradeStatus.TRADING) {

            TradeNotification tradeNotification1 = new TradeNotification(
                    trade.getMyPost().getAccount(),
                    "/chat/history/",
                    "관리자에 의해 거래가 취소되었습니다 : " + trade.getMyPost().getTitle() + ", " + trade.getOtherPost().getTitle(),
                    trade
            );
            notificationRepository.save(tradeNotification1);

            TradeNotification tradeNotification2 = new TradeNotification(
                    trade.getOtherPost().getAccount(),
                    "/chat/history/",
                    "관리자에 의해 거래가 취소되었습니다 : " + trade.getMyPost().getTitle() + ", " + trade.getOtherPost().getTitle(),
                    trade
            );
            notificationRepository.save(tradeNotification2);
        }
    }

    //관리자에 의한 글 삭제
    public void postDeleteNotification(Long postId) {
        Post post = postRepository.findById(postId).get();
        Account account = post.getAccount();
        if (!post.isDeleted()) {
            Notification notification = new Notification(
                    account,
                    "/post/myPost",
                    "관리자에 의해 '" + post.getTitle() + "' 이(가) 삭제되었습니다"
            );
            notificationRepository.save(notification);
        }
    }


    public List<NotificationBasic> findByIdNotChecked(Long accountId) {
        return notificationRepository.findByAccountId(accountId);
    }

    public void setNotificationChecked(Long notificationId) {
        notificationRepository.setNoticationCheckedById(notificationId);
    }

}
