package com.sungam1004.register.controller;

import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import com.sungam1004.register.dto.AttendanceDto;
import com.sungam1004.register.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "attendance/attendance";
        }

        AttendanceDto.Response response = null;
        try {
            response = attendanceService.saveAttendance(requestDto.getName(), requestDto.getPassword());
        } catch (CustomException e) {
            //bindingResult.addError(new ObjectError(e.getError().name(), e.getMessage())); // 글로벌 오류
            if (e.getError() == ErrorCode.NOT_FOUND_USER) {
                bindingResult.rejectValue("name", "0", e.getMessage());
            }
            if (e.getError() == ErrorCode.INCORRECT_PASSWORD) {
                bindingResult.rejectValue("password", "0", e.getMessage());
            }
            return "attendance/attendance";
        }
        model.addAttribute("attendance", response);
        return "attendance/completeAttendance";
    }

}
