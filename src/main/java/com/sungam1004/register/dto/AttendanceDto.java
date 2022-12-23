package com.sungam1004.register.dto;

import com.sungam1004.register.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AttendanceDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "이름이 공백일 수 없습니다.")
        private String name;

        @Pattern(regexp = "^[0-9]{4}$", message = "비밀번호는 숫자 4자리입니다.")
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Integer absenceNumber;
        private Integer attendanceNumber;

        public static AttendanceDto.Response of(User user) {
            return Response.builder()
                    .absenceNumber(user.getAbsenceNumber())
                    .attendanceNumber(user.getAttendanceNumber())
                    .build();
        }
    }
}
