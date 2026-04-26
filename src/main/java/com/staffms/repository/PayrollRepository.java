package com.staffms.repository;

import com.staffms.model.entity.Payroll;
import com.staffms.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByUserOrderByYearDescMonthDesc(User user);
    Optional<Payroll> findByUserAndMonthAndYear(User user, int month, int year);
    List<Payroll> findByMonthAndYear(int month, int year);
}
