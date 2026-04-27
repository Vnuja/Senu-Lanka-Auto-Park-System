package com.staffms.service;

import com.staffms.model.entity.Payroll;
import com.staffms.model.entity.User;
import com.staffms.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    /**
     * Get all payroll records for a specific user
     */
    public List<Payroll> getPayrollByUser(User user) {
        return payrollRepository.findByUserOrderByYearDescMonthDesc(user);
    }

    /**
     * Get all payroll records for a specific month and year
     */
    public List<Payroll> getPayrollByMonth(int month, int year) {
        return payrollRepository.findByMonthAndYear(month, year);
    }

    /**
     * Get a single payroll record by ID
     */
    public Optional<Payroll> getPayrollById(Long payrollId) {
        return payrollRepository.findById(payrollId);
    }

    /**
     * Get payroll for a specific user, month, and year
     */
    public Optional<Payroll> getPayrollByUserAndMonthYear(User user, int month, int year) {
        return payrollRepository.findByUserAndMonthAndYear(user, month, year);
    }

    /**
     * Get all payroll records
     */
    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    /**
     * Generate a new payroll record
     */
    @Transactional
    public Payroll generatePayroll(User user, int month, int year, BigDecimal basic, BigDecimal allowances, BigDecimal deductions) {
        // Check if payroll already exists for this user, month, and year
        Optional<Payroll> existing = getPayrollByUserAndMonthYear(user, month, year);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Payroll already exists for this user in the specified month and year.");
        }

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

    /**
     * Update an existing payroll record
     */
    @Transactional
    public Payroll updatePayroll(Long payrollId, BigDecimal basic, BigDecimal allowances, BigDecimal deductions) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new IllegalArgumentException("Payroll record not found with ID: " + payrollId));
        
        payroll.setBasicSalary(basic);
        payroll.setAllowances(allowances);
        payroll.setDeductions(deductions);
        payroll.setNetSalary(basic.add(allowances).subtract(deductions));
        
        return payrollRepository.save(payroll);
    }

    /**
     * Delete a payroll record
     */
    @Transactional
    public void deletePayroll(Long payrollId) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new IllegalArgumentException("Payroll record not found with ID: " + payrollId));
        payrollRepository.deleteById(payrollId);
    }

    /**
     * Check if payroll exists for a user in a specific month/year
     */
    public boolean payrollExists(User user, int month, int year) {
        return getPayrollByUserAndMonthYear(user, month, year).isPresent();
    }

    /**
     * Calculate total payroll for a given month and year
     */
    public BigDecimal getTotalPayrollAmount(int month, int year) {
        return payrollRepository.findByMonthAndYear(month, year).stream()
                .map(Payroll::getNetSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Get count of payroll records
     */
    public long getPayrollCount() {
        return payrollRepository.count();
    }
}
