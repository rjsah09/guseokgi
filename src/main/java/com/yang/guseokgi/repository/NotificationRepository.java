package com.yang.guseokgi.repository;

import com.yang.guseokgi.domain.notification.Notification;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findById(Long notificationId);

    @Query("select new com.yang.guseokgi.dto.notification.NotificationBasic(n.id, n.localDateTime, n.link, n.text) " +
            "from Notification n where n.account.id = :accountId and n.checked = false " +
            "order by n.localDateTime asc")
    List<NotificationBasic> findByAccountId(@Param("accountId")Long accountId);

    @Modifying(clearAutomatically = true)
    @Query("update TradeChatNotification n set n.checked = true where n.trade.id = :tradeId and n.account.id = :accountId")
    void setChatChecked(@Param("tradeId") Long tradeId, @Param("accountId") Long accountId);

    @Modifying(clearAutomatically = true)
    @Query("update Notification  n set n.checked = true where n.id = :notificationId")
    void setNoticationCheckedById(@Param("notificationId")Long notificationId);

}
