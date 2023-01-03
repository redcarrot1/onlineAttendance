package com.sungam1004.register.service;

import com.sungam1004.register.domain.Attendance;
import com.sungam1004.register.domain.User;
import com.sungam1004.register.dto.AttendanceDto;
import com.sungam1004.register.exception.CustomException;
import com.sungam1004.register.exception.ErrorCode;
import com.sungam1004.register.repository.AttendanceRepository;
import com.sungam1004.register.repository.UserRepository;
import com.sungam1004.register.utill.PasswordManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final PasswordManager passwordManager;

    public AttendanceDto.Response saveAttendance(String name, String password) {
        if (!passwordManager.isCorrectUserPassword(password)) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        if (!validSunday()) throw new CustomException(ErrorCode.INVALID_DAY_OF_WEEK);

        Optional<User> optionalUser = userRepository.findByName(name);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Attendance attendance = new Attendance(user);
            attendanceRepository.save(attendance);
            user.increaseAttendanceNumber();
            log.info("출석이 성공적으로 저장되었습니다. name={}, dateTime={}", name, attendance.getCreatedAt());
            return AttendanceDto.Response.of(user);
        }
        else {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
    }

    private boolean validSunday() {
        LocalDate date = LocalDate.now();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getValue() == 7; // 월=1, 일=7
    }
}
