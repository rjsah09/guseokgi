package com.yang.guseokgi.controller;

import com.yang.guseokgi.dto.account.AccountSession;
import com.yang.guseokgi.dto.favorite.FavoriteAjax;
import com.yang.guseokgi.dto.favorite.FavoriteIdAjax;
import com.yang.guseokgi.dto.favorite.FavoritePreview;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import com.yang.guseokgi.service.FavoriteService;
import com.yang.guseokgi.service.NotificationService;
import com.yang.guseokgi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final NotificationService notificationService;
    private final PostService postService;

    @GetMapping("/favorite/myFavorite")
    public String myFavorite(Model model, HttpServletRequest request) {
        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        Long accountId = accountSession.getId();

        List<FavoritePreview> list = favoriteService.findPostPreviewByAccountId(accountSession.getId());
        model.addAttribute("list", list);

        if(accountSession != null) {
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "favorite/myFavoritePage";
    }

    //즐겨찾기 등록
    @RequestMapping("/favorite/addFavorite")
    @ResponseBody
    public Map<String, String> addFavorite(@RequestBody FavoriteAjax favoriteAjax) {
        Long postId = Long.parseLong(favoriteAjax.getPostId());
        Long accountId = Long.parseLong(favoriteAjax.getAccountId());

        Map<String, String> map = new HashMap<>();

        //20개가 넘는지 확인 - result:false
        Long size = favoriteService.findCountByAccountId(accountId);
        if(size >= 20) {
            map.put("result", "false");
            return map;
        }

        //즐겨찾기에 등록 - result:true
        favoriteService.save(postId, accountId);
        map.put("result", "true");

        return map;
    }

    @RequestMapping("/favorite/deleteFavorite")
    @ResponseBody
    //즐겨찾기 해제
    public void deleteByPostIdAndAccountId(@RequestBody FavoriteAjax favoriteAjax) {
        Long postId = Long.parseLong(favoriteAjax.getPostId());
        Long accountId = Long.parseLong(favoriteAjax.getAccountId());

        favoriteService.deleteByPostIdAndAccountId(postId, accountId);
    }

    @RequestMapping("/favorite/deleteFavoriteById")
    @ResponseBody
    public void deleteById(@RequestBody FavoriteIdAjax favoriteIdAjax) {
        Long notificationId = Long.parseLong(favoriteIdAjax.getId());

        favoriteService.deleteById(notificationId);
    }

}
