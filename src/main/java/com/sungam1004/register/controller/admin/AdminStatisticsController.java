package com.sungam1004.register.controller.admin;

import com.sungam1004.register.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/statistics")
@Slf4j
public class AdminStatisticsController {

    private final AdminService adminService;

    @GetMapping
    public String statisticsForm() {
        adminService.statistics();
        return "redirect:/";
    }
}

