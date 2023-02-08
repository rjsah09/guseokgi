package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.domain.Trade;
import com.yang.guseokgi.domain.category.TradeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public  interface TradeRepository extends JpaRepository<Trade, Long> {

    //겹치는 TradeRequest 있는지 확인 -> Long 반환
    @Query("select count(t) from Trade t where t.myPost = :post or t.otherPost = :post")
    Long findDuplicatedTradeRequest(Post post);

    @Query("select t from Trade t " +
            "where (t.myPost.account.id = :accountId or t.otherPost.account.id = :accountId) " +
            "and t.tradeStatus = com.yang.guseokgi.domain.category.TradeStatus.TRADING " +
            "order by t.startTime desc")
    Page<Trade> findTradingList(Long accountId, Pageable pageable);

    @Query("select t from Trade t " +
            "where (t.myPost.account.id = :accountId or t.otherPost.account.id = :accountId) " +
            "and (t.tradeStatus = com.yang.guseokgi.domain.category.TradeStatus.SUCCEED or t.tradeStatus = com.yang.guseokgi.domain.category.TradeStatus.CANCEL) " +
            "order by t.endTime desc")
    Page<Trade> findTradeHistory(Long accountId, Pageable pageable);

    @Query("select t from Trade t " +
            "where (t.myPost.account.id = :accountId or t.otherPost.account.id = :accountId) " +
            "and t.tradeStatus = :tradeStatus " +
            "order by t.endTime desc")
    Page<Trade> findTradeHistoryByStatus(Long accountId, Pageable pageable, @Param("tradeStatus")TradeStatus tradeStatus);

    @Query("select t from Trade t " +
            "where t.tradeStatus = com.yang.guseokgi.domain.category.TradeStatus.TRADING " +
            "order by t.startTime desc")
    Page<Trade> findAllManager(Pageable pageable);

    @Query("select t from Trade t " +
            "where t.tradeStatus = com.yang.guseokgi.domain.category.TradeStatus.TRADING " +
            "and (t.myPost.account.uid = :keyword or t.otherPost.account.uid = :keyword) order by t.startTime desc")
    Page<Trade> findAllByKeywordManager(Pageable pageable, @Param("keyword")String uid);

    @Query("select t from Trade t " +
            "where t.tradeStatus = com.yang.guseokgi.domain.category.TradeStatus.TRADING " +
            "and (t.myPost.id = :postId or t.otherPost.id = :postId)")
    Optional<Trade> findTradingByPostId(@Param("postId")Long postId);
}