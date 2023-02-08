package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.Chat;
import com.yang.guseokgi.dto.chat.ChatBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select new com.yang.guseokgi.dto.chat.ChatBasic(c.trade.id, c.account.id, c.chatText, c.chatCategory) from Chat c where c.trade.id = :tradeId order by c.chatTime asc")
    List<ChatBasic> findByTradeId(long tradeId);

    @Query("select new com.yang.guseokgi.dto.chat.ChatBasic(c.trade.id, c.account.id, c.chatText, c.chatCategory) from Chat c where c.trade.id = :id order by c.chatTime asc")
    List<ChatBasic> findListByPostId(@Param("id")Long id);
}
