package com.sungam1004.register.controller;

import com.sungam1004.register.dto.AttendanceDto;
import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import com.sungam1004.register.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("attendance")
    public String attendancePage(Model model) {
        model.addAttribute("AttendanceRequestDto", new AttendanceDto.Request());
        return "attendance/attendance";
    }

    @PostMapping("attendance")
    public String saveAttendance(@Valid @ModelAttribute("AttendanceRequestDto") AttendanceDto.Request requestDto,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "attendance/attendance";
        }

        String team = null;
        try {
            team = attendanceService.saveAttendance(requestDto.getName(), requestDto.getPassword());
        } catch (CustomException e) {
            if (e.getError() == ErrorCode.NOT_FOUND_USER) {
                bindingResult.rejectValue("name", "0", e.getMessage());
            }
            if (e.getError() == ErrorCode.INCORRECT_PASSWORD) {
                bindingResult.rejectValue("password", "0", e.getMessage());
            }
            if (e.getError() == ErrorCode.INVALID_DAY_OF_WEEK) {
                bindingResult.addError(new ObjectError(e.getError().name(), e.getMessage())); // 글로벌 오류
            }
            return "attendance/attendance";
        }
        redirectAttributes.addAttribute("team", team);
        return "redirect:/attendance/completeAttendance";
    }

    @GetMapping("attendance/completeAttendance")
    public String attendanceCompletePage(@RequestParam String team, Model model) {
        log.info("team={}", team);
        AttendanceDto.Response response = attendanceService.findTodayAttendanceByTeam(team);
        model.addAttribute("teamAttendance", response);
        return "attendance/completeAttendance";
    }

}
