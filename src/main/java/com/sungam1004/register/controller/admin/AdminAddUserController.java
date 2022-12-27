package com.sungam1004.register.controller.admin;

import com.sungam1004.register.Exception.CustomException;
import com.sungam1004.register.Exception.ErrorCode;
import com.sungam1004.register.domain.Team;
import com.sungam1004.register.dto.AddUserDto;
import com.sungam1004.register.dto.UserManagerDto;
import com.sungam1004.register.service.AdminService;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/manager")
@Slf4j
public class AdminAddUserController {

    private final AdminService adminService;

    @GetMapping
    public String userManagerHome(Model model) {
        List<UserManagerDto> users = adminService.findUserAll();
        model.addAttribute("users", users);
        return "admin/userManage/home";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("addUserDto", new AddUserDto.Request());
        model.addAttribute("teams", Team.getTeamNameList());
        return "admin/userManage/addUser";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("addUserDto") AddUserDto.Request requestDto,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teams", Team.getTeamNameList());
            return "admin/userManage/addUser";
        }
        try {
            adminService.addUser(requestDto);
        } catch (CustomException e) {
            if (e.getError() == ErrorCode.DUPLICATE_USER_NAME) {
                bindingResult.rejectValue("name", "0", e.getMessage());
            }
            model.addAttribute("teams", Team.getTeamNameList());
            return "admin/userManage/addUser";
        }

        return "admin/userManage/completeAddUser";
    }

}
