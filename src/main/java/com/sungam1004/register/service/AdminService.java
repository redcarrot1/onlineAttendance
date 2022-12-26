package com.sungam1004.register.service;

import com.sungam1004.register.Exception.CustomException;
import com.sungam1004.register.Exception.ErrorCode;
import com.sungam1004.register.domain.Attendance;
import com.sungam1004.register.dto.StatisticsDto;
import com.sungam1004.register.repository.AttendanceRepository;
import com.sungam1004.register.repository.UserRepository;
import com.sungam1004.register.utill.ExcelManager;
import com.sungam1004.register.utill.PasswordManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        statistics.setName(userRepository.findAll());

        List<Attendance> attendances = attendanceRepository.findAll();
        for (Attendance attendance : attendances) {
            statistics.addAttendance(attendance.getUser().getName(), attendance.getCreatedAt());
        }

        /**
         * Debug
         */
        {
            List<List<LocalDateTime>> statisticsAttendances = statistics.getAttendance();
            List<String> names = statistics.getNames();
            for (int i = 0; i < names.size(); i++) {
                System.out.print(names.get(i) + " : ");

                for (List<LocalDateTime> at : statisticsAttendances) {

                    for (LocalDateTime time : at) {
                        if (time != null) System.out.print("O ");
                        else System.out.print("X ");
                    }
                }
                System.out.println();
            }
        }
        return excelManager.createExcelFile(statistics);
    }
}
