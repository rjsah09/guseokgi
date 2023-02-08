package com.yang.guseokgi.service;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.domain.Trade;
import com.yang.guseokgi.domain.category.PostStatus;
import com.yang.guseokgi.domain.category.TradeStatus;
import com.yang.guseokgi.dto.chat.ChatBasic;
import com.yang.guseokgi.dto.post.PostBasicManager;
import com.yang.guseokgi.dto.trade.TradeBasic;
import com.yang.guseokgi.dto.trade.TradeManager;
import com.yang.guseokgi.repository.ChatRepository;
import com.yang.guseokgi.repository.PostRepository;
import com.yang.guseokgi.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TradeService {

    private final PostRepository postRepository;
    private final TradeRepository tradeRepository;
    private final ChatRepository chatRepository;

    //거래신청저장
    public long saveWithPostId(Long myPostId, Long otherPostId) {
        Post myPost = postRepository.findById(myPostId).get();
        Post otherPost = postRepository.findById(otherPostId).get();

        myPost.setPostStatus(PostStatus.TRADING);
        otherPost.setPostStatus(PostStatus.TRADING);

        Trade trade = new Trade(myPost, otherPost, TradeStatus.TRADING);

        return tradeRepository.save(trade).getId();

    }

    public Optional<TradeBasic> findById(Long tradeId) {
        return tradeRepository.findById(tradeId).map(TradeBasic::new);
    }

    //진행중인 거래 목록
    public Page<TradeBasic> findTradingList(Long accountId, Pageable pageable) {
        return tradeRepository.findTradingList(accountId, pageable).map(TradeBasic::new);
    }

    //끝난 거래 목록(거래 성공, 실패)
    public Page<TradeBasic> findTradeHistory(Long accountId, Pageable pageable) {
        return tradeRepository.findTradeHistory(accountId, pageable).map(TradeBasic::new);
    }

    public Page<TradeBasic> findTradeHistoryByStatus(Long accountId, Pageable pageable, TradeStatus tradeStatus) {
        return tradeRepository.findTradeHistoryByStatus(accountId, pageable, tradeStatus).map(TradeBasic::new);
    }

    //거래 취소 요청
    public boolean itemReject(String tradeId, String accountId) {
        Trade trade = tradeRepository.findById(Long.parseLong(tradeId)).get();

        //거래 취소 요청
        if(trade.getMyPost().getAccount().getId() == Long.parseLong(accountId)) {
            trade.setMyPostCancel(true);
        } else {
            trade.setOtherPostCancel(true);
        }

        //두 명 모두 거래 취소 요청을 한 경우 거래 취소
        if(trade.isMyPostCancel() && trade.isOtherPostCancel()) {
            tradeReject(Long.parseLong(tradeId));
            return true;
        }

        return false;
    }

    //거래 취소 철회
    public void itemRejectWithdraw(String tradeId, String accountId) {
        Trade trade = tradeRepository.findById(Long.parseLong(tradeId)).get();

        if(trade.getMyPost().getAccount().getId() == Long.parseLong(accountId)) {
            trade.setMyPostCancel(false);
        } else {
            trade.setOtherPostCancel(false);
        }

    }

    //거래 취소
    public void tradeReject(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).get();
        if(trade.getTradeStatus() == TradeStatus.TRADING) {
            trade.setTradeStatus(TradeStatus.CANCEL);
            trade.getMyPost().setPostStatus(PostStatus.WAITING);
            trade.getOtherPost().setPostStatus(PostStatus.WAITING);
            trade.setEndTime(LocalDateTime.now());
        }
    }

    //물품 수령
    public boolean itemArrived(String tradeId, String accountId) {
        Trade trade = tradeRepository.findById(Long.parseLong(tradeId)).get();

        //물품 수령
        if(trade.getMyPost().getAccount().getId() == Long.parseLong(accountId)) {
            trade.setMyPostComplete(true);
        } else {
            trade.setOtherPostComplete(true);
        }

        //두 명 모두 거래 취소 요청을 한 경우 거래 취소
        if(trade.isMyPostComplete() && trade.isOtherPostComplete()) {
            tradeComplete(Long.parseLong(tradeId));
            return true;
        }

        return false;

    }

    //거래 성공
    public void tradeComplete(Long tradeId) {
        Trade trade = tradeRepository.findById(tradeId).get();
        trade.setTradeStatus(TradeStatus.SUCCEED);
        trade.getMyPost().setPostStatus(PostStatus.TRADED);
        trade.getOtherPost().setPostStatus(PostStatus.TRADED);
        trade.setEndTime(LocalDateTime.now());
    }

    //trade의 반대편 id 반환
    public Long getOtherSideId(Long tradeId, Long accountId) {
        Trade trade = tradeRepository.findById(tradeId).get();
        Account target;
        if(accountId == trade.getMyPost().getAccount().getId()) {
            target = trade.getOtherPost().getAccount();
        } else {
            target = trade.getMyPost().getAccount();
        }

        return target.getId();
    }

    //관리자 거래 조회
    public Page<TradeManager> findAllManager(Pageable pageable) {
        Page<TradeManager> list =  tradeRepository.findAllManager(pageable).map(TradeManager::new);

        for(TradeManager tm : list) {
            List<ChatBasic> chatList = chatRepository.findListByPostId(tm.getId());
            tm.setChatList(chatList);
        }

        return list;
    }

    //관리자 거래 조회
    public Page<TradeManager> findAllByKeywordManager(Pageable pageable, String keyword) {
        Page<TradeManager> list =  tradeRepository.findAllByKeywordManager(pageable, keyword).map(TradeManager::new);

        for(TradeManager tm : list) {
            List<ChatBasic> chatList = chatRepository.findListByPostId(tm.getId());
            tm.setChatList(chatList);
        }

        return list;
    }

    //관리자 거래 해제
    public Long cancelTradeByPostId(Long postId) {
        Optional<Trade> tradeOpt = tradeRepository.findTradingByPostId(postId);
        if(!tradeOpt.isEmpty()) {
            Trade trade = tradeOpt.get();
            tradeReject(trade.getId());
            return trade.getId();
        }

        return null;
    }
}


