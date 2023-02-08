package com.yang.guseokgi.controller;

import com.yang.guseokgi.domain.category.PostCategory;
import com.yang.guseokgi.dto.account.AccountSession;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import com.yang.guseokgi.dto.post.PostPreview;
import com.yang.guseokgi.service.NotificationService;
import com.yang.guseokgi.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final PostService postService;
    private final NotificationService notificationService;

    @RequestMapping("/")
    public String home(Model model,
                       @PageableDefault(page=0, size=10, sort="postTime", direction= Sort.Direction.DESC)Pageable pageable,
                       @RequestParam(value="keyword", required = false) String keyword,
                       HttpServletRequest request)
    {
        Page<PostPreview> list;

        if(keyword == null) {
            list = postService.findAll(pageable);
        } else {
            list = postService.findAllByKeyword(pageable, keyword);
        }

        //세션이 있는 경우
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
        model.addAttribute("elements", list.getNumberOfElements());
        model.addAttribute("noResultMessage", "물품이 없습니다");
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("prevIndex", prevIndex);
        model.addAttribute("nextIndex", nextIndex);
        model.addAttribute("startIndex", startIndex);
        model.addAttribute("endIndex", endIndex);

        return "home";

    }

    @GetMapping("/{postCategory}")
    public String categoryHome(Model model,
                               @PageableDefault(page=0, size=10, sort="postTime", direction= Sort.Direction.DESC)Pageable pageable,
                               @PathVariable(value = "postCategory") PostCategory postCategory,
                               @RequestParam(value="keyword", required = false) String keyword,
                               HttpServletRequest request)
    {
        Page<PostPreview> list;

        if(keyword == null) {
            list = postService.findByPostCategory(pageable, postCategory);
        } else {
            list = postService.findByPostCategoryAndKeyword(pageable, postCategory, keyword);
        }

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

        return "home";
    }

    @GetMapping("/today")
    public String today(Model model,
                        @PageableDefault(page=0, size=10, sort="dailyViews", direction= Sort.Direction.DESC)Pageable pageable,
                        HttpServletRequest request)
    {
        Page<PostPreview> list = postService.findToday(pageable);

        model.addAttribute("list", list);

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long accountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "today";
    }

}
