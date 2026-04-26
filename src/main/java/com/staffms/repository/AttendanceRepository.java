package com.staffms.repository;

import com.staffms.model.entity.Attendance;
import com.staffms.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByUserAndDate(User user, LocalDate date);
    List<Attendance> findByUserOrderByDateDesc(User user);
    List<Attendance> findByDateBetween(LocalDate start, LocalDate end);
}
