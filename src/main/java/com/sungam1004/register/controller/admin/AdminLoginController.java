package com.sungam1004.register.controller.admin;

import com.sungam1004.register.Exception.CustomException;
import com.sungam1004.register.Exception.ErrorCode;
import com.sungam1004.register.dto.LoginAdminDto;
import com.sungam1004.register.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin")
@Slf4j
public class AdminLoginController {

    private final AdminService adminService;

    @GetMapping("/login")
    public String loginAdminForm(Model model) {
        model.addAttribute("LoginAdminDto", new LoginAdminDto.Request());
        return "admin/loginAdmin";
    }

    @PostMapping("/login")
    public String loginAdmin(@Valid @ModelAttribute("LoginAdminDto") LoginAdminDto.Request requestDto,
                             BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "admin/loginAdmin";
        }

        try {
            adminService.loginAdmin(requestDto.getPassword());
        } catch (CustomException e) {
            if (e.getError() == ErrorCode.INCORRECT_PASSWORD) {
                bindingResult.rejectValue("password", "0", e.getMessage());
            }
            return "admin/loginAdmin";
        }

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute("Admin", "successLogin");
        return "admin/adminHome";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // getSession(false) 를 사용해야 함 (세션이 없더라도 새로 생성하면 안되기 때문)
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/admin/login";
    }
}

