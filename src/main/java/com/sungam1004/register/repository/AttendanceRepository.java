package com.sungam1004.register.repository;

import com.sungam1004.register.domain.Attendance;
import com.sungam1004.register.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUser(User user);
}
