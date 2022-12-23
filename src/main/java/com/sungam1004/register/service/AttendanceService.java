package com.sungam1004.register.service;

import com.sungam1004.register.Exception.CustomException;
import com.sungam1004.register.Exception.ErrorCode;
import com.sungam1004.register.config.PasswordManager;
import com.sungam1004.register.domain.Attendance;
import com.sungam1004.register.domain.User;
import com.sungam1004.register.dto.AttendanceDto;
import com.sungam1004.register.repository.AttendanceRepository;
import com.sungam1004.register.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
