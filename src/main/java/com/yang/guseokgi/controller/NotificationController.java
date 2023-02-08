package com.yang.guseokgi.controller;

import com.yang.guseokgi.dto.notification.NotificationDeleteId;
import com.yang.guseokgi.dto.trade.TradeAJAX;
import com.yang.guseokgi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @RequestMapping("/notification/redirect/{notificationId}")
    public String notificationRedirect(@PathVariable("notificationId") String notificationId, HttpServletRequest request) {
        return notificationService.notificationChecked(Long.parseLong(notificationId));
    }

    @RequestMapping("notification/check")
    public Map<String, String> notificationCheck(Long notificationId) {
        return new HashMap<>();
    }

    @ResponseBody
    @RequestMapping("/notification/chatChecked")
    public void tradeChatNotificationChecked(@RequestBody TradeAJAX tradeAJAX) {
        Long accountId = Long.parseLong(tradeAJAX.getAccountId());
        Long tradeId = Long.parseLong(tradeAJAX.getTradeId());
        notificationService.setChatChecked(tradeId, accountId);
    }

    @ResponseBody
    @RequestMapping("notification/deleteNotification")
    public void deleteNotification(@RequestBody NotificationDeleteId id) {
        Long notificationId = Long.parseLong(id.getId());
        notificationService.setNotificationChecked(notificationId);
    }

}
