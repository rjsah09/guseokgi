package com.yang.guseokgi.service;

import com.yang.guseokgi.domain.*;
import com.yang.guseokgi.domain.category.ChatCategory;
import com.yang.guseokgi.dto.chat.ChatBasic;
import com.yang.guseokgi.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final TradeRepository tradeRepository;
    private final AccountRepository accountRepository;
    private final ChatImgRepository chatImgRepository;

    public List<ChatBasic> findByTradeId(long tradeId) {
        return chatRepository.findByTradeId(tradeId);
    }

    public void save(ChatBasic chatBasic) {
        Trade trade = tradeRepository.findById(Long.parseLong(chatBasic.getRoomId())).get();
        Account account = accountRepository.findById(Long.parseLong(chatBasic.getWriter())).get();
        LocalDateTime localDateTime = LocalDateTime.now();
        String text = chatBasic.getMessage();
        Chat chat = new Chat(trade, account, localDateTime, text);
        if(chatBasic.getChatCategory().equals("TEXT")) {
            chat.setChatCategory(ChatCategory.TEXT);
        } else if (chatBasic.getChatCategory().equals("IMAGE")) {
            chat.setChatCategory(ChatCategory.IMAGE);
        } else if (chatBasic.getChatCategory().equals("SYSTEM")) {
            chat.setChatCategory(ChatCategory.SYSTEM);
        } else if (chatBasic.getChatCategory().equals("SUCCEED")) {
            chat.setChatCategory(ChatCategory.SUCCEED);
        } else {
            chat.setChatCategory(ChatCategory.CANCEL);
        }
        chatRepository.save(chat);
    }

    public ChatImg upload(MultipartFile multipartFile) throws IOException {

            ChatImg chatImg = new ChatImg();

            String sourceFileName = multipartFile.getOriginalFilename();
            String sourceFileNameExtension = "." + FilenameUtils.getExtension(sourceFileName).toLowerCase();

            File destinationFile;
            String destinationFileName = System.nanoTime() + sourceFileNameExtension;
            if(!(sourceFileNameExtension.equals(".png") || sourceFileNameExtension.equals(".jpg") || sourceFileNameExtension.equals(".gif"))) {
                return null;
            }
            String fileUrl = "C:/guseokgiChatImages/";

            do {
                destinationFile = new File(fileUrl + destinationFileName);
            } while(destinationFile.exists());

            destinationFile.getParentFile().mkdirs();
            multipartFile.transferTo(destinationFile);

            chatImg.setFileName(destinationFileName);
            chatImg.setFileOriName(sourceFileName);
            chatImg.setFileUrl(fileUrl);

            chatImgRepository.save(chatImg);

        return chatImg;
    }
}
