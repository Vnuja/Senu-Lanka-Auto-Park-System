package com.staffms.service;

import com.staffms.model.entity.Payroll;
import com.staffms.model.entity.User;
import com.staffms.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    public List<Payroll> getPayrollByUser(User user) {
        return payrollRepository.findByUserOrderByYearDescMonthDesc(user);
    }

    public List<Payroll> getPayrollByMonth(int month, int year) {
        return payrollRepository.findByMonthAndYear(month, year);
    }

    @Transactional
    public Payroll generatePayroll(User user, int month, int year, BigDecimal basic, BigDecimal allowances, BigDecimal deductions) {
        BigDecimal net = basic.add(allowances).subtract(deductions);
        
        Payroll payroll = Payroll.builder()
                .user(user)
                .month(month)
                .year(year)
                .basicSalary(basic)
                .allowances(allowances)
                .deductions(deductions)
                .netSalary(net)
                .build();
        
        return payrollRepository.save(payroll);
    }
}
