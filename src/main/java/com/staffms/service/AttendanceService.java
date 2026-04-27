package com.staffms.service;

import com.staffms.model.entity.Attendance;
import com.staffms.model.entity.User;
import com.staffms.repository.AttendanceRepository;
import com.staffms.util.AttendanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Attendance> getAttendanceByUser(User user) {
        return attendanceRepository.findByUserOrderByDateDesc(user);
    }

    @Transactional
    public Attendance checkIn(User user) {
        LocalDate today = LocalDate.now();
        Optional<Attendance> existing = attendanceRepository.findByUserAndDate(user, today);
        
        Attendance attendance;
        if (existing.isPresent()) {
            attendance = existing.get();
        } else {
            attendance = Attendance.builder()
                    .user(user)
                    .date(today)
                    .status(AttendanceStatus.PRESENT)
                    .build();
        }
        
        if (attendance.getCheckIn() == null) {
            attendance.setCheckIn(LocalTime.now());
        }
        
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance checkOut(User user) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByUserAndDate(user, today)
                .orElseThrow(() -> new RuntimeException("No check-in record found for today"));
        
        attendance.setCheckOut(LocalTime.now());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByRange(LocalDate start, LocalDate end) {
        return attendanceRepository.findByDateBetween(start, end);
    }

    @Transactional
    public Attendance updateAttendance(Long id, LocalTime checkIn, LocalTime checkOut, AttendanceStatus status) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
        attendance.setCheckIn(checkIn);
        attendance.setCheckOut(checkOut);
        attendance.setStatus(status);
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
}
