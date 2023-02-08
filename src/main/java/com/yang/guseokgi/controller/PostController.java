package com.yang.guseokgi.controller;

import com.yang.guseokgi.domain.category.PostStatus;
import com.yang.guseokgi.dto.account.AccountSession;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import com.yang.guseokgi.dto.post.PostBasic;
import com.yang.guseokgi.dto.post.PostPreview;
import com.yang.guseokgi.dto.post.PostUpdate;
import com.yang.guseokgi.dto.post.PostWrite;
import com.yang.guseokgi.service.AccountService;
import com.yang.guseokgi.service.FavoriteService;
import com.yang.guseokgi.service.NotificationService;
import com.yang.guseokgi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final NotificationService notificationService;
    private final AccountService accountService;
    private final FavoriteService favoriteService;

    //글 작성 Get
    @GetMapping("/post/write")
    public String writeRedirect(Model model, HttpServletRequest request) {
        model.addAttribute("postWrite", new PostWrite());

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long accountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "post/writePostPage";
    }

    //글 작성 post
    @PostMapping("/post/write")
    public String write(@Validated PostWrite postWrite, BindingResult bindingResult, MultipartFile files, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        AccountSession accountSession = (AccountSession) session.getAttribute("accountSession");

        if(accountSession == null) {
            return "redirect:/login";
        }

        if(bindingResult.hasErrors()) {
            return "post/writePostPage";
        }

        Long postId = postService.save(postWrite, accountSession.getId());

        return "redirect:/post/view/" + postId;

    }

    //글 열람
    @GetMapping("/post/view/{postId}")
    public String postViewRedirect(@PathVariable("postId") String postId, Model model, HttpServletRequest request) {
        Long id = Long.parseLong(postId);
        PostBasic postBasic = postService.findPostBasicById(id);

        if(postBasic == null) {
            model.addAttribute("msg", "잘못된 접근입니다");
            model.addAttribute("url", "/");
            return "alertPage";
        } else if (postBasic.isDeleted()) {
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

            Long favoriteCount = favoriteService.findCountByPostIdAndAccountId(postBasic.getId(), accountId);
            model.addAttribute("favoriteCount", favoriteCount);
        }

        postService.updateViews(id);

        model.addAttribute("postBasic", postBasic);
        model.addAttribute("tradedCount", postService.findTradedByPostId(Long.parseLong(postId)));

        return "post/postViewPage";
    }


    //작성자 글 검색//
    @GetMapping("/post/writer/{accountId}")
    public String writerPostList(@PathVariable("accountId") String id, Model model, HttpServletRequest request,
                                 @PageableDefault(page=0, size=10, sort="postTime", direction= Sort.Direction.DESC)Pageable pageable)
    {
        Long accountId = Long.parseLong(id);
        Optional<AccountSession> account = accountService.findAccountSessionById(accountId);
        if(account.isEmpty()) {
            model.addAttribute("msg", "잘못된 접근입니다");
            String referer = request.getHeader("Referer");
            if(referer == null) {
                model.addAttribute("url", "/");
            } else {
                model.addAttribute("url", referer);
            }
            return "alertPage";
        }

        Page<PostPreview> list = postService.findWriterPost(pageable, accountId);

        model.addAttribute("writer", postService.findById(accountId).get().getAccount().getNickname());
        model.addAttribute("tradedCount", postService.findTradedByAccountId(accountId));
        model.addAttribute("tradingCount", postService.findTradingByAccountId(accountId));

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

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long myAccountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(myAccountId);
            model.addAttribute("notification", notification);
        }

        return "post/accountSearchPage";
    }

    //나의 글 전체//
    @GetMapping("/post/myPost")
    public String myPost(Model model, HttpServletRequest request,
                         @PageableDefault(page=0, size=10, sort="postTime", direction= Sort.Direction.DESC)Pageable pageable)
    {
        AccountSession session = (AccountSession) request.getSession().getAttribute("accountSession");
        Page<PostPreview> list = postService.findAllById(pageable, session.getId());

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

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long accountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "post/myPostPage";
    }

    //나의 글 상태에 따른 페이지//
    @GetMapping("/post/myPost/{postStatus}")
    public String categoryHome(Model model,
                               HttpServletRequest request,
                               @PageableDefault(page=0, size=10, sort="postTime", direction= Sort.Direction.DESC)Pageable pageable,
                               @PathVariable("postStatus") PostStatus postStatus)
    {
        AccountSession session = (AccountSession) request.getSession().getAttribute("accountSession");
        Page<PostPreview> list = postService.findAllByIdAndStatus(pageable, session.getId(), postStatus);

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

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long accountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }


        return "post/myPostPage";
    }

    //글 수정
    @GetMapping("/post/update/{postId}")
    public String updatePostGet(@PathVariable("postId")String id, Model model, HttpServletRequest request) {
        Long postId = Long.parseLong(id);
        Optional<PostUpdate> postUpdateOpt = postService.findPostBasicUpdateById(postId);
        if(postUpdateOpt.isEmpty()) {
            model.addAttribute("msg", "잘못된 접근입니다");
            String referer = request.getHeader("Referer");
            if(referer == null) {
                model.addAttribute("url", "/");
            } else {
                model.addAttribute("url", referer);
            }
            return "alertPage";
        }

        PostUpdate postUpdate = postUpdateOpt.get();
        if(postUpdate.isDeleted()) {
            model.addAttribute("msg", "삭제된 글입니다");
            String referer = request.getHeader("Referer");
            if(referer == null) {
                model.addAttribute("url", "/");
            } else {
                model.addAttribute("url", referer);
            }
            return "alertPage";
        }
        model.addAttribute("postUpdate", postUpdate);

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long accountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "post/postUpdatePage";
    }

    @PostMapping("/post/update/{postId}")
    public String updatePostPost(@PathVariable("postId")String id, @Validated PostUpdate postUpdate, BindingResult bindingResult) throws IOException{
        Long postId = Long.parseLong(id);

        if(bindingResult.hasErrors()) {
            return "post/update/" + id;
        }

        postService.postUpdate(postId, postUpdate);

        return "redirect:/post/view/" + postId;
    }

    //글 삭제
    @GetMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable("postId")String id, Model model, HttpServletRequest request) {
        AccountSession session = (AccountSession) request.getSession().getAttribute("accountSession");
        Long postId = Long.parseLong(id);
        PostBasic postBasic = postService.findPostBasicById(postId);
        if(postBasic == null) {
            model.addAttribute("msg", "잘못된 접근입니다");
            model.addAttribute("url", "/");
            return "alertPage";
        } else if (postBasic.isDeleted()) {
            model.addAttribute("msg", "이미 삭제된 글입니다");
            String referer = request.getHeader("Referer");
            if(referer == null) {
                model.addAttribute("url", "/");
            } else {
                model.addAttribute("url", referer);
            }
            return "alertPage";
        }
        postService.deletePost(postId);

        return "redirect:/";
    }

}
