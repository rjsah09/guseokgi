package com.yang.guseokgi.controller;

import com.yang.guseokgi.domain.Account;
import com.yang.guseokgi.dto.account.*;
import com.yang.guseokgi.dto.notification.NotificationBasic;
import com.yang.guseokgi.service.AccountService;
import com.yang.guseokgi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final NotificationService notificationService;

    //회원가입
    @GetMapping("/register")
    public String registerRedirect(Model model, HttpServletRequest request) {
        request.getSession().invalidate();  //세션이 있으면 삭제
        model.addAttribute("accountRegister", new AccountRegister());

        return "account/registerPage";
    }

    @PostMapping("/register")
    public String register(@Validated AccountRegister accountRegister, BindingResult bindingResult, HttpServletRequest request,
                           Model model)
    {
        //유효성 검사
        if(bindingResult.hasErrors()) {
            return "account/registerPage";
        } else if(!accountRegister.getPassword().equals(accountRegister.getPasswordConfirm())) {
            bindingResult.addError(new FieldError("AccountRegister", "passwordConfirm", "비밀번호와 일치하지 않습니다."));
            return "account/registerPage";
        }

        //성공시
        accountService.join(accountRegister);

        model.addAttribute("msg", "회원가입을 완료했습니다");
        model.addAttribute("url", "/");
        return "alertPage";
    }

    //로그인
    @GetMapping("/login")
    public String loginRedirect(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        //Referer 등록
        if(session.getAttribute("referer") != null) {
            session.removeAttribute("referer");
        }
        String referer = request.getHeader("Referer");
        log.info(referer);
        session.setAttribute("referer", referer);

        model.addAttribute("accountLogin", new AccountLogin());
        return "account/loginPage";
    }

    @PostMapping("/login")
    public String login(@Validated AccountLogin accountLogin,  BindingResult bindingResult, HttpServletRequest request) {
        HttpSession session = request.getSession();

        //유효성 검사
        if(bindingResult.hasErrors()) {
            return "account/loginPage";
        }

        //아이디, 비밀번호 확인
        Optional<AccountSession> accountSession = accountService.findByIdAndPassword(accountLogin.getUid(), accountLogin.getPassword());
        if(accountSession.isEmpty()) {
            bindingResult.addError(new ObjectError("AccountLogin", "아이디 혹은 비밀번호가 일치하지 않습니다"));
            return "account/loginPage";
        }

        //성공시
        String referer = (String) session.getAttribute("referer");
        session.setAttribute("accountSession", accountSession.get());
        if(referer != null) {
            session.removeAttribute("referer");
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

    //로그아웃
    @GetMapping("/logout")
    public String logoutRedirect(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }

    //닉네임 변경
    @GetMapping("/account/edit/nickname")
    public String editNickname(Model model, HttpServletRequest request) {
        model.addAttribute("changeNickname", new ChangeNickname());

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long accountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "account/changeNicknamePage";
    }

    @PostMapping("/account/edit/nickname")
    public String editNickanme(@Validated ChangeNickname changeNickname, BindingResult bindingResult,
                               HttpServletRequest request, HttpServletResponse response,
                               Model model) throws IOException {
        AccountSession session = (AccountSession) request.getSession().getAttribute("accountSession");
        Account account = accountService.findById(session.getId()).get();

        if(bindingResult.hasErrors()) {
            return "account/changeNicknamePage";
        } else if(!changeNickname.getPassword().equals(account.getPassword())) {
            bindingResult.addError(new FieldError("changeNickname", "password", "비밀번호가 일치하지 않습니다."));
            return "account/changeNicknamePage";
        }

        accountService.editNickname(account.getId(), changeNickname.getNickname());

        session.setNickname(changeNickname.getNickname());
        request.getSession().removeAttribute("accountSession");
        request.getSession().setAttribute("accountSession", session);

        model.addAttribute("msg", "닉네임이 변경되었습니다");
        model.addAttribute("url", "/");

        return "alertPage";
    }

    //비밀번호 변경
    @GetMapping("/account/edit/password")
    public String editPassword(Model model, HttpServletRequest request) {
        model.addAttribute("changePassword", new ChangePassword());

        AccountSession accountSession = (AccountSession) request.getSession().getAttribute("accountSession");
        if(accountSession != null) {
            Long accountId = accountSession.getId();
            List<NotificationBasic> notification = notificationService.findByIdNotChecked(accountId);
            model.addAttribute("notification", notification);
        }

        return "account/changePasswordPage";
    }

    @PostMapping("/account/edit/password")
    public String editPassword(@Validated ChangePassword changePassword, BindingResult bindingResult,
                               HttpServletRequest request, HttpServletResponse response,
                               Model model) throws IOException {
        AccountSession session = (AccountSession) request.getSession().getAttribute("accountSession");
        Account account = accountService.findById(session.getId()).get();

        if(bindingResult.hasErrors()) {
            return "account/changePasswordPage";
        } else if(!changePassword.getPassword().equals(account.getPassword())) {
            bindingResult.addError(new FieldError("changePassword", "password", "비밀번호가 일치하지 않습니다."));
            return "account/changeNicknamePage";
        } else if(!changePassword.getNewPassword().equals(changePassword.getNewPasswordConfirm())) {
            bindingResult.addError(new FieldError("changePassword", "newPasswordConfirm", "새 비밀번호와 일치하지 않습니다"));
        }

        accountService.editPassword(account.getId(), changePassword.getNewPassword());

        model.addAttribute("msg", "비밀번호가 변경되었습니다. 다시 로그인해주세요");
        model.addAttribute("url", "/login");
        return "alertPage";
    }

}
