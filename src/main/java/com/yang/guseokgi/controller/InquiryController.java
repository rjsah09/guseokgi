package com.yang.guseokgi.controller;

import com.yang.guseokgi.dto.account.AccountSession;
import com.yang.guseokgi.dto.inquiry.InquiryBasic;
import com.yang.guseokgi.dto.inquiry.InquirySend;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import com.yang.guseokgi.service.InquiryService;
import com.yang.guseokgi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;
    private final NotificationService notificationService;

    @GetMapping("/inquiry/myInquiry")
    public String myInquiry(Model model,
                            @PageableDefault(page=0, size=10, sort="questionTime", direction= Sort.Direction.DESC) Pageable pageable,
                            HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        AccountSession accountSession = (AccountSession) session.getAttribute("accountSession");
        Long accountId = accountSession.getId();

        Page<InquiryBasic> list = inquiryService.findByAccoundId(pageable, accountSession.getId());

        if(accountSession != null) {
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        int totalPage = list.getTotalPages();
        int currentPage = list.getPageable().getPageNumber();
        int startPage = currentPage - currentPage % 10;
        int endPage = startPage + 9;
        if(endPage > totalPage) {
            endPage = totalPage - 1;
        }
        int startIndex = 0;
        int endIndex = totalPage - totalPage % 10;
        int prevIndex = startPage > 0 ? startPage - 10 : startPage;
        int nextIndex = startPage + 10;
        if(nextIndex > totalPage) {
            nextIndex = endIndex;
        }
        model.addAttribute("list", list);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevIndex", prevIndex);
        model.addAttribute("nextIndex", nextIndex);
        model.addAttribute("startIndex", startIndex);
        model.addAttribute("endIndex", endIndex);

        return "inquiry/myInquiryPage";
    }

    @GetMapping("/inquiry/send")
    public String inquirySendGet(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AccountSession accountSession = (AccountSession) session.getAttribute("accountSession");
        Long accountId = accountSession.getId();

        model.addAttribute("inquirySend", new InquirySend());

        if(accountSession != null) {
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "inquiry/inquirySendPage";
    }

    @PostMapping("/inquiry/send")
    public String inquirySendPost(@Validated InquirySend inquirySend, BindingResult bindingResult, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        AccountSession accountSession = (AccountSession) session.getAttribute("accountSession");
        Long accountId = accountSession.getId();

        if(bindingResult.hasErrors()) {
            return "inquiry/inquirySendPage";
        }

        inquiryService.save(inquirySend.getText(), accountId);

        if(accountSession != null) {
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "redirect:/inquiry/myInquiry";
    }

    @GetMapping("/inquiry/view/{inquiryId}")
    public String inquiryView(@PathVariable("inquiryId")String id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AccountSession accountSession = (AccountSession) session.getAttribute("accountSession");
        Long accountId = accountSession.getId();

        if(accountSession == null) {
            model.addAttribute("msg", "잘못된 접근입니다.");
            model.addAttribute("url", "/");
        } else {
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);

        }

        Long inquiryId = Long.parseLong(id);
        Optional<InquiryBasic> inquiryBasicOpt = inquiryService.findById(inquiryId);

        if(inquiryBasicOpt.isEmpty()) {
            model.addAttribute("msg", "잘못된 접근입니다.");
            model.addAttribute("url", "/");
        }

        model.addAttribute("inquiry", inquiryBasicOpt.get());

        return "inquiry/inquiryViewPage";
    }
}
