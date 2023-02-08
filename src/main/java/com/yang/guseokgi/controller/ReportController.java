package com.yang.guseokgi.controller;

import com.yang.guseokgi.dto.account.AccountSession;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import com.yang.guseokgi.dto.post.PostBasic;
import com.yang.guseokgi.dto.post.PostNicknameAndTitle;
import com.yang.guseokgi.dto.report.ReportSend;
import com.yang.guseokgi.service.NotificationService;
import com.yang.guseokgi.service.PostService;
import com.yang.guseokgi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final PostService postService;
    private final NotificationService notificationService;

    @GetMapping("/report/send/{postId}")
    public String sendReportGet(@PathVariable("postId")String id, Model model, HttpServletRequest request) {
        Long postId = Long.parseLong(id);
        Optional<PostNicknameAndTitle> postNicknameAndTitleOpt = postService.findNicknameAndTitleById(postId);
        if(postNicknameAndTitleOpt.isEmpty()) {
            model.addAttribute("msg", "잘못된 접근입니다");
            model.addAttribute("url", "/");
            return "alertPage";
        }

        PostNicknameAndTitle postNicknameAndTitle = postNicknameAndTitleOpt.get();
        if (postNicknameAndTitle.isDeleted()) {
            model.addAttribute("msg", "삭제된 글입니다");
            String referer = request.getHeader("Referer");
            if(referer == null) {
                model.addAttribute("url", "/");
            } else {
                model.addAttribute("url", referer);
            }
            return "alertPage";
        }

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long accountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        model.addAttribute("postNicknameAndTitle", postNicknameAndTitle);
        model.addAttribute("reportSend", new ReportSend());

        return "report/reportPage";
    }

    @PostMapping("/report/send/{postId}")
    public String sendReportPost(ReportSend reportSend, @PathVariable("postId")String id, HttpServletRequest request) {
        Long postId = Long.parseLong(id);
        HttpSession session = request.getSession();
        AccountSession accountSession = (AccountSession) session.getAttribute("accountSession");

        reportService.save(reportSend, postId, accountSession.getId());

        return "redirect:/post/view/{postId}";
    }

}
