package com.sungam1004.register.service;

import com.sungam1004.register.Exception.CustomException;
import com.sungam1004.register.Exception.ErrorCode;
import com.sungam1004.register.domain.Attendance;
import com.sungam1004.register.domain.User;
import com.sungam1004.register.dto.AddUserDto;
import com.sungam1004.register.dto.StatisticsDto;
import com.sungam1004.register.dto.UserDetailDto;
import com.sungam1004.register.dto.UserManagerDto;
import com.sungam1004.register.repository.AttendanceRepository;
import com.sungam1004.register.repository.UserRepository;
import com.sungam1004.register.utill.ExcelManager;
import com.sungam1004.register.utill.PasswordManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final PasswordManager passwordManager;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final ExcelManager excelManager;

    public void loginAdmin(String password) {
        if (!passwordManager.isCorrectAdminPassword(password)) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
    }

    public void changeUserPassword(String password) throws CustomException {
        passwordManager.changeUserPassword(password);
    }

    public void changeAdminPassword(String password) throws CustomException {
        passwordManager.changeAdminPassword(password);
    }

    public String statistics() {
        StatisticsDto statistics = new StatisticsDto();
        statistics.setName(userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getTeam))
                .toList());

        List<Attendance> attendances = attendanceRepository.findAll();
        for (Attendance attendance : attendances) {
            statistics.addAttendance(attendance.getUser().getName(), attendance.getCreatedAt());
        }

        /**
         * Debug
         */
        {
            List<StatisticsDto.NameAndAttendance> nameAndAttendances = statistics.getNameAndAttendances();
            for (StatisticsDto.NameAndAttendance nameAndAttendance : nameAndAttendances) {
                System.out.print(nameAndAttendance.getName() + " : ");
                for (LocalDateTime time : nameAndAttendance.getDateTimes()) {
                    if (time != null) System.out.print("O ");
                    else System.out.print("X ");
                }
                System.out.println();
            }
        }
        return excelManager.createExcelFile(statistics);
    }

    public void addUser(AddUserDto.Request requestDto) {
        if (userRepository.existsByName(requestDto.getName()))
            throw new CustomException(ErrorCode.DUPLICATE_USER_NAME);
        User user = requestDto.toEntity();
        userRepository.save(user);
    }

    public List<UserManagerDto> findUserAll() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getTeam))
                .map(UserManagerDto::of)
                .toList();
    }

    public UserDetailDto userDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        StatisticsDto statistics = new StatisticsDto();
        statistics.setName(List.of(user));

        List<Attendance> attendances = attendanceRepository.findByUser(user);
        for (Attendance attendance : attendances) {
            statistics.addAttendance(user.getName(), attendance.getCreatedAt());
        }

        List<LocalDateTime> dateTimes = statistics.getNameAndAttendances().get(0).getDateTimes();
        List<String> date = StatisticsDto.date;
        List<UserDetailDto.AttendanceDate> attendanceDates = new ArrayList<>();
        for (int i = 0; i < dateTimes.size(); i++) {
            attendanceDates.add(new UserDetailDto.AttendanceDate(date.get(i), dateTimes.get(i)));
        }

        return UserDetailDto.of(user, attendanceDates);
    }

    public void toggleAttendance(Long userId, String date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        List<Attendance> attendances = attendanceRepository.findByUser(user);

        for (Attendance attendance : attendances) {
            String createdAt = attendance.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (createdAt.equals(date)) {
                attendanceRepository.delete(attendance);
                user.decreaseAttendanceNumber();
                user.increaseAbsenceNumber();
                return;
            }
        }
        String[] splitDate = date.split("-");
        LocalDateTime saveDateTime = LocalDateTime.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]),
                Integer.parseInt(splitDate[2]), 0, 0);
        attendanceRepository.save(new Attendance(user, saveDateTime));
        user.decreaseAbsenceNumber();
        user.increaseAttendanceNumber();
    }
}
