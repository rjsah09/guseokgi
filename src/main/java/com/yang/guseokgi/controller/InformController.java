package com.yang.guseokgi.controller;

import com.yang.guseokgi.dto.Inform.InformBasic;
import com.yang.guseokgi.dto.Inform.InformPreview;
import com.yang.guseokgi.dto.account.AccountSession;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import com.yang.guseokgi.service.InformService;
import com.yang.guseokgi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InformController {

    private final InformService informService;
    private final NotificationService notificationService;

    @GetMapping("/inform")
    public String informList(Model model, HttpServletRequest request,
                             @PageableDefault(page=0, size=10, sort="localDateTime", direction= Sort.Direction.DESC) Pageable pageable)
    {
        Page<InformPreview> list = informService.findInformPreviewAll(pageable);

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long accountId = accountSession.getId();
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

        return "inform/informPage";
    }

    @GetMapping("/inform/view/{informId}")
    public String informView(@PathVariable("informId")String id, Model model, HttpServletRequest request) {
        Long informId = Long.parseLong(id);

        InformBasic informBasic = informService.findInformBasicById(informId);
        if(informBasic == null) {
            model.addAttribute("msg", "잘못된 접근입니다");
            model.addAttribute("url", "/");
            return "alertPage";
        }

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long accountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        model.addAttribute("informBasic", informBasic);

        return "inform/informViewPage";
    }

}
