package com.yang.guseokgi.controller;

import com.yang.guseokgi.domain.Post;
import com.yang.guseokgi.domain.category.PostStatus;
import com.yang.guseokgi.domain.category.TradeStatus;
import com.yang.guseokgi.dto.account.AccountSession;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import com.yang.guseokgi.dto.post.PostBasic;
import com.yang.guseokgi.dto.post.PostIdAndTitle;
import com.yang.guseokgi.dto.trade.TradeAJAX;
import com.yang.guseokgi.dto.trade.TradeBasic;
import com.yang.guseokgi.dto.trade.TradeSend;
import com.yang.guseokgi.service.NotificationService;
import com.yang.guseokgi.service.PostService;
import com.yang.guseokgi.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TradeController {

    private final TradeService tradeService;
    private final PostService postService;
    private final NotificationService notificationService;

    //거래 신청(Get)
    @GetMapping("/trade/request/{postId}")
    public String sendRequestRedirect(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("postId") String postId) throws IOException {
        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        Long accountId = accountSession.getId();
        Long targetPostId = Long.parseLong(postId);
        PostBasic targetPostBasic = postService.findPostBasicById(targetPostId);

        if(targetPostBasic == null) {
            model.addAttribute("msg", "잘못된 접근입니다");
            String referer = request.getHeader("Referer");
            if(referer == null) {
                model.addAttribute("url", "/");
            } else {
                model.addAttribute("url", referer);
            }
            return "alertPage";
        } else if (targetPostBasic.isDeleted()) {
            model.addAttribute("msg", "삭제된 글입니다");
            String referer = request.getHeader("Referer");
            if(referer == null) {
                model.addAttribute("url", "/");
            } else {
                model.addAttribute("url", referer);
            }
            return "alertPage";
        }

        //거래 신청이 불가한 경우(거래완료된 물품이거나 이미 거래가 진행중인 물품)
        if(targetPostBasic.getPostStatus() != PostStatus.WAITING) {
            model.addAttribute("msg", "거래 신청이 불가능한 물품입니다");
            String referer = request.getHeader("Referer");
            if(referer == null) {
                model.addAttribute("url", "/");
            } else {
                model.addAttribute("url", referer);
            }
            return "alertPage";
        }

        List<PostIdAndTitle> list = postService.findNotCompleted(accountId);    //거래 신청 가능한 품목(id, 제목)

        //나의 물품이 없는 경우
        if(list.size() == 0) {
            model.addAttribute("msg", "거래 신청 가능한 물품이 없습니다");
            String referer = request.getHeader("Referer");
            if(referer == null) {
                model.addAttribute("url", "/");
            } else {
                model.addAttribute("url", referer);
            }
            return "alertPage";
        }

        if(accountSession != null) {
            Long myAccountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(myAccountId);
            model.addAttribute("notification", notification);
        }

        model.addAttribute("targetPostTitle", targetPostBasic.getTitle());
        model.addAttribute("list", list);
        model.addAttribute("requestPost", new TradeSend());

        return "trade/tradeRequestPage";
    }

    //거래 신청(Post)
    @PostMapping("/trade/request/{postId}")
    public String sendRequest(TradeSend tradeSend, BindingResult bindingResult, @PathVariable("postId") String otherPId,
                              HttpServletResponse response, HttpServletRequest request,
                              Model model) throws IOException{
        Long myPostId = tradeSend.getPostId();
        Long otherPostId = Long.parseLong(otherPId);

        Post myPost = postService.findById(myPostId).get();
        Post otherPost = postService.findById(otherPostId).get();

        Long tradeId = tradeService.saveWithPostId(myPostId, otherPostId);
        notificationService.tradeStartNotification(tradeId);

        return "redirect:/chat/room/" + tradeId;
    }

    //진행중인 거래
    @GetMapping("/trade/trading")
    public String trading(Model model, HttpServletRequest request, Pageable pageable) {
        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        Long accountId = accountSession.getId();

        Page<TradeBasic> list = tradeService.findTradingList(accountId, pageable);

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

        if(accountSession != null) {
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "trade/tradingPage";
    }

    //지난 거래
    @GetMapping("/trade/history")
    public String tradeHistory(Model model, HttpServletRequest request, Pageable pageable) {
        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        Long accountId = accountSession.getId();

        Page<TradeBasic> list = tradeService.findTradeHistory(accountId, pageable);

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

        if(accountSession != null) {
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "trade/tradeHistoryPage";
    }

    @GetMapping("/trade/history/{tradeStatus}")
    public String categoryHome(Model model,
                               HttpServletRequest request,
                               @PageableDefault(page=0, size=10, sort="endTime", direction= Sort.Direction.DESC)Pageable pageable,
                               @PathVariable("tradeStatus") TradeStatus tradeStatus)
    {
        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        Long accountId = accountSession.getId();

        Page<TradeBasic> list = tradeService.findTradeHistoryByStatus(accountId, pageable, tradeStatus);

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

        if(accountSession != null) {
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "trade/tradeHistoryPage";
    }

    /** AJAX 통신 */

    //거래 취소 요청
    @ResponseBody
    @RequestMapping(value = "/trade/itemReject", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> itemReject(@RequestBody TradeAJAX tradeAJAX) {
        Long tradeId = Long.parseLong(tradeAJAX.getTradeId());

        boolean complete = tradeService.itemReject(tradeAJAX.getTradeId(), tradeAJAX.getAccountId());
        Map<String, String> map = new HashMap<String, String>();

        if(complete) {
            map.put("complete", "true");
            TradeBasic tradeBasic = tradeService.findById(tradeId).get();
            notificationService.tradeCancelNotification(tradeId);
            notificationService.favoritePostTradeAbleNotification(tradeBasic.getMyPostId());
            notificationService.favoritePostTradeAbleNotification(tradeBasic.getOtherPostId());
        } else {
            map.put("complete", "false");
        }

        return map;
    }

    //거래 취소 철회
    @ResponseBody
    @RequestMapping(value = "/trade/itemRejectWithdraw", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void itemRejectWithdraw(@RequestBody TradeAJAX tradeAJAX) {
        tradeService.itemRejectWithdraw(tradeAJAX.getTradeId(), tradeAJAX.getAccountId());
        Map<String, String> map = new HashMap<String, String>();
        map.put("complete", "false");
    }

    //물품 수령
    @ResponseBody
    @RequestMapping(value = "/trade/itemRCV", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> itemRCV(@RequestBody TradeAJAX tradeAJAX) {
        Long tradeId = Long.parseLong(tradeAJAX.getTradeId());

        boolean complete = tradeService.itemArrived(tradeAJAX.getTradeId(), tradeAJAX.getAccountId());
        Map<String, String> map = new HashMap<String, String>();

        if(complete) {
            map.put("complete", "true");
            TradeBasic tradeBasic = tradeService.findById(tradeId).get();
            notificationService.tradeSucceedNotification(tradeId);
            notificationService.favoritePostTradeUnableNotification(tradeBasic.getMyPostId());
            notificationService.favoritePostTradeUnableNotification(tradeBasic.getOtherPostId());
        } else {
            map.put("complete", "false");
        }

        return map;
    }

}
