package com.yang.guseokgi.controller;

import com.yang.guseokgi.domain.ChatImg;
import com.yang.guseokgi.domain.category.ChatCategory;
import com.yang.guseokgi.domain.category.TradeStatus;
import com.yang.guseokgi.dto.account.AccountSession;
import com.yang.guseokgi.dto.chat.ChatBasic;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import com.yang.guseokgi.dto.trade.TradeBasic;
import com.yang.guseokgi.service.ChatService;
import com.yang.guseokgi.service.NotificationService;
import com.yang.guseokgi.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final TradeService tradeService;
    private final ChatService chatService;
    private final NotificationService notificationService;

    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/chat/message")
    public void message(ChatBasic chat) {
        chatService.save(chat);

        Long tradeId = Long.parseLong(chat.getRoomId());
        Long accountId = tradeService.getOtherSideId(tradeId, Long.parseLong(chat.getWriter()));
        String text;
        if (chat.getChatCategory().equals("IMAGE")) {
            text = "(이미지)";
        } else {
            text = chat.getMessage();
        }

        notificationService.setChatChecked(tradeId, accountId);
        notificationService.newChatNotification(tradeId, accountId, text);

        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
    }

    @GetMapping("/chat/room/{tradeId}")
    public String chatting(HttpServletResponse response, @PathVariable("tradeId") String tradeId, Model model, HttpServletRequest request) throws IOException {
        Long id = Long.parseLong(tradeId);

        Optional<TradeBasic> tradeBasicOpt = tradeService.findById(id);
        if(tradeBasicOpt.isEmpty()) {
            model.addAttribute("msg", "잘못된 접근입니다");
            model.addAttribute("url", "/");
            return "alertPage";
        }

        TradeBasic tradeBasic  = tradeBasicOpt.get();

        List<ChatBasic> history = chatService.findByTradeId(Long.parseLong(tradeId));

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        Long accountId = accountSession.getId();
        Long myAccountId = tradeBasic.getMyAccountId();
        Long otherAccountId = tradeBasic.getOtherAccountId();

        if (myAccountId != accountId && otherAccountId != accountId) {
            model.addAttribute("msg", "잘못된 접근입니다");
            model.addAttribute("url", request.getHeader("Referer"));
            return "alertPage";
        } else if (tradeBasic.getTradeStatus() == TradeStatus.SUCCEED || tradeBasic.getTradeStatus() == TradeStatus.CANCEL) {
            model.addAttribute("msg", "종료된 거래입니다");
            model.addAttribute("url", request.getHeader("Referer"));
            return "alertPage";
        }

        model.addAttribute("room", tradeBasic);
        model.addAttribute("history", history);

        notificationService.setChatChecked(id, accountId);


        return "chat/chattingPage";
    }

    @GetMapping("chat/history/{tradeId}")
    public String chatHistory(HttpServletResponse response, @PathVariable("tradeId") String tradeId, Model model, HttpServletRequest request) throws IOException {
        Long id = Long.parseLong(tradeId);

        Optional<TradeBasic> tradeBasicOpt = tradeService.findById(id);
        if(tradeBasicOpt.isEmpty()) {
            model.addAttribute("msg", "잘못된 접근입니다");
            model.addAttribute("url", "/");
            return "alertPage";
        }

        TradeBasic tradeBasic  = tradeBasicOpt.get();

        List<ChatBasic> history = chatService.findByTradeId(Long.parseLong(tradeId));

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        Long accountId = accountSession.getId();
        Long myAccountId = tradeBasic.getMyAccountId();
        Long otherAccountId = tradeBasic.getOtherAccountId();

        if (myAccountId != accountId && otherAccountId != accountId) {
            model.addAttribute("msg", "잘못된 접근입니다");
            model.addAttribute("url", request.getHeader("Referer"));
            return "alertPage";
        } else if (tradeBasic.getTradeStatus() == TradeStatus.TRADING) {
            model.addAttribute("msg", "진행중인 거래입니다");
            model.addAttribute("url", request.getHeader("Referer"));
            return "alertPage";
        }

        if(accountSession != null) {
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        model.addAttribute("room", tradeBasic);
        model.addAttribute("history", history);

        return "chat/chatHistoryPage";
    }

    @RequestMapping(value = "/chat/imageUpload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> itemRejectWithdraw(@RequestParam("uploadfile") MultipartFile multipartFile) {
        Map<String, String> map = new HashMap<String, String>();

        try {
            ChatImg chatImg = chatService.upload(multipartFile);
            map.put("imageUrl", "/chatImage/" + chatImg.getFileName());
        } catch (IOException e) {

        } catch (NullPointerException e) {

        }

        return map;
    }
}
