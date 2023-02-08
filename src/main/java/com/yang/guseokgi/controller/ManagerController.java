package com.yang.guseokgi.controller;

import com.yang.guseokgi.dto.Inform.InformAjax;
import com.yang.guseokgi.dto.Inform.InformBasic;
import com.yang.guseokgi.dto.Inform.InformWrite;
import com.yang.guseokgi.dto.account.AccountManager;
import com.yang.guseokgi.dto.inquiry.InquiryAjax;
import com.yang.guseokgi.dto.inquiry.InquiryBasic;
import com.yang.guseokgi.dto.manager.ManagerLogin;
import com.yang.guseokgi.dto.manager.ManagerSession;
import com.yang.guseokgi.dto.post.PostBasicManager;
import com.yang.guseokgi.dto.report.ReportAjax;
import com.yang.guseokgi.dto.report.ReportBasic;
import com.yang.guseokgi.dto.trade.TradeManager;
import com.yang.guseokgi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ManagerController {

    private final ManagerService managerService;
    private final AccountService accountService;
    private final PostService postService;
    private final TradeService tradeService;
    private final InquiryService inquiryService;
    private final ReportService reportService;
    private final NotificationService notificationService;
    private final InformService informService;

    //관리자 로그인
    @GetMapping("/managerLogin")
    public String managerLoginGet(Model model, HttpServletRequest request) {
        request.getSession().invalidate();
        ManagerLogin managerLogin = new ManagerLogin();

        model.addAttribute("managerLogin", managerLogin);

        return "manager/managerLogin";
    }

    @PostMapping("/managerLogin")
    public String managerLoginPost(@Validated ManagerLogin managerLogin, BindingResult bindingResult, HttpServletRequest request) {
        HttpSession session = request.getSession();

        if(bindingResult.hasErrors()) {
            return "manager/managerLogin";
        }

        //아이디, 비밀번호 확인
        Optional<ManagerSession> managerSession = managerService.findByIdAndPassword(managerLogin.getUid(), managerLogin.getPassword());
        if(managerSession.isEmpty()) {
            bindingResult.addError(new ObjectError("AccountLogin", "아이디 혹은 비밀번호가 일치하지 않습니다"));
            return "manager/managerLogin";
        }

        //성공시
        session.setAttribute("managerSession", managerSession.get());
        return "redirect:/manager/account";
    }

    //회원 조회(기본페이지)
    @GetMapping("/manager/account")
    public String managerAccount(Model model,
                                 @PageableDefault(page=0, size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(value="keyword", required = false) String keyword,
                                 HttpServletRequest request)
    {
        Page<AccountManager> list;

        if(keyword == null) {
            list = accountService.findAllManager(pageable);
        } else {
            list = accountService.findAllByKeywordManager(pageable, keyword);
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

        return "manager/managerAccount";
    }

    //글 조회
    @GetMapping("/manager/post")
    public String managerPost(Model model,
                              @PageableDefault(page=0, size=10, sort="postTime", direction= Sort.Direction.DESC) Pageable pageable,
                              @RequestParam(value="keyword", required = false) String keyword,
                              HttpServletRequest request)
    {
        Page<PostBasicManager> list;

        if(keyword == null) {
            list = postService.findAllManager(pageable);
        } else {
            list = postService.findAllByKeywordManager(pageable, keyword);
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

        return "manager/managerPost";
    }

    //거래 조회
    @GetMapping("/manager/trade")
    public String managerTrade(Model model,
                               @PageableDefault(page=0, size=10, sort="startTime", direction= Sort.Direction.DESC) Pageable pageable,
                               @RequestParam(value="keyword", required = false) String keyword,
                               HttpServletRequest request)
    {
        Page<TradeManager> list;

        if(keyword == null) {
            list = tradeService.findAllManager(pageable);
        } else {
            list = tradeService.findAllByKeywordManager(pageable, keyword);
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

        return "manager/managerTrade";
    }

    //문의 조회
    @GetMapping("/manager/inquiry")
    public String managerInquiry(Model model,
                                 @PageableDefault(page=0, size=10, sort="questionTime", direction= Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(value="keyword", required = false) String keyword,
                                 HttpServletRequest request)
    {
        Page<InquiryBasic> list;

        if(keyword == null) {
            list = inquiryService.findAllManager(pageable);
        } else {
            list = inquiryService.findAllByKeywordManager(pageable, keyword);
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

        return "manager/managerInquiry";
    }

    //신고 조회
    @GetMapping("/manager/report")
    public String managerReport(Model model,@PageableDefault(page=0, size=10, sort="startTime", direction= Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(value="keyword", required = false) String keyword,
                                 HttpServletRequest request)
    {
        Page<ReportBasic> list;

        if(keyword == null) {
            list = reportService.findAllManager(pageable);
        } else {
            list = reportService.findAllByKeywordManager(pageable, keyword);
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

        return "manager/managerReport";
    }

    //공지 조회
    @GetMapping("/manager/inform")
    public String managerInform(Model model,
                                @PageableDefault(page=0, size=10, sort="localDateTime", direction= Sort.Direction.DESC) Pageable pageable,
                                HttpServletRequest request)
    {

        Page<InformBasic> list = informService.findPageAll(pageable);

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

        return "manager/managerInform";
    }

    //공지 작성 Get
    @GetMapping("/manager/inform/write")
    public String writeInformGet(Model model, HttpServletRequest request) {
        model.addAttribute("informWrite", new InformWrite());

        return "manager/managerInformWrite";
    }

    //공지 작성 Post
    @PostMapping("/manager/inform/write")
    public String writeInformPost(@Validated InformWrite informWrite, BindingResult bindingResult, HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            return "manager/managerInformWrite";
        }

        informService.save(informWrite.getTitle(), informWrite.getArticle());
        return "redirect:/manager/inform";
    }

    //공지 수정 Get
    @GetMapping("/manager/inform/update/{informId}")
    public String updateInformGet(@PathVariable("informId") String id, Model model, HttpServletRequest request) {

        Long informId = Long.parseLong(id);
        InformWrite informWrite = informService.findInformWriteById(informId);

        model.addAttribute("informWrite", informWrite);

        return "manager/managerInformUpdate";
    }

    //공지 수정 Post
    @PostMapping("/manager/inform/update/{informId}")
    public String updateInformPost(@Validated InformWrite informWrite, BindingResult bindingResult,
                                   @PathVariable("informId") String id, HttpServletRequest request)
    {

        if(bindingResult.hasErrors()) {
            return "manager/managerInformUpdate";
        }

        Long informId = Long.parseLong(id);
        informService.update(informId, informWrite.getTitle(), informWrite.getArticle());
        return "redirect:/manager/inform";
    }

    //공지 삭제
    @ResponseBody
    @RequestMapping(value = "/manager/inform/deleteInform", method = RequestMethod.POST)
    public void deleteInform(@RequestBody InformAjax informAjax) {
        Long informId = Long.parseLong(informAjax.getId());
        informService.deleteById(informId);
    }

    //문의 답변
    @ResponseBody
    @RequestMapping(value = "/manager/inquiryReply", method = RequestMethod.POST)
    public void inquiryReply(@RequestBody InquiryAjax inquiryAjax) {
        Long inquiryId = Long.parseLong(inquiryAjax.getId());
        String text = inquiryAjax.getText();
        Long accountId = inquiryService.inquiryReply(inquiryId, text);
        if(accountId != -1L) {
            notificationService.inquiryRepliedNotification(accountId);
        }
    }

    //글 삭제
    @ResponseBody
    @RequestMapping(value = "/manager/deletePost", method = RequestMethod.POST)
    public void deletePost(@RequestBody ReportAjax reportAjax) {
        Long postId = Long.parseLong(reportAjax.getId());
        Long tradeId = tradeService.cancelTradeByPostId(postId);
        if(tradeId != null) {
            notificationService.tradeRejectNotification(tradeId);
        }
        postService.deletePost(postId);
        notificationService.postDeleteNotification(postId);
    }

    //신고 처리
    @ResponseBody
    @RequestMapping(value = "/manager/acceptReport", method = RequestMethod.POST)
    public void acceptReport(@RequestBody ReportAjax reportAjax) {
        Long reportId = Long.parseLong(reportAjax.getId());

        Long postId = reportService.acceptReport(reportId);
        postService.deletePost(postId);
        notificationService.postDeleteNotification(postId);
    }

    //신고거절
    @ResponseBody
    @RequestMapping(value = "/manager/rejectReport", method = RequestMethod.POST)
    public void rejectReport(@RequestBody ReportAjax reportAjax) {
        Long reportId = Long.parseLong(reportAjax.getId());

        Long postId = reportService.rejectReport(reportId);
    }

    //매니저 거래 해제
    @GetMapping("/manager/tradeReject/{id}")
    public String managerTradeReject(@PathVariable("id")String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long tradeId = Long.parseLong(id);
        tradeService.tradeReject(tradeId);
        notificationService.tradeRejectNotification(tradeId);

        return "redirect:" + request.getHeader("referer");
    }

}
