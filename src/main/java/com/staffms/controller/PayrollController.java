package com.staffms.controller;

import com.staffms.model.entity.Payroll;
import com.staffms.model.entity.StaffProfile;
import com.staffms.service.PayrollService;
import com.staffms.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;
    @Autowired
    private StaffService staffService;

    /**
     * Display payroll list for current month with filtering options
     */
    @GetMapping
    public String payrollList(
            @RequestParam(defaultValue = "0") int month,
            @RequestParam(defaultValue = "0") int year,
            Model model) {
        
        try {
            int selectedMonth = month > 0 ? month : LocalDate.now().getMonthValue();
            int selectedYear = year > 0 ? year : LocalDate.now().getYear();
            
            List<Payroll> payrolls = payrollService.getPayrollByMonth(selectedMonth, selectedYear);
            if (payrolls == null) {
                payrolls = List.of();
            }
            
            BigDecimal totalAmount = payrollService.getTotalPayrollAmount(selectedMonth, selectedYear);
            if (totalAmount == null) {
                totalAmount = BigDecimal.ZERO;
            }
            
            model.addAttribute("title", "Payroll Management");
            model.addAttribute("payrolls", payrolls);
            model.addAttribute("month", selectedMonth);
            model.addAttribute("year", selectedYear);
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("recordCount", payrolls.size());
            
            return "payroll/list";
        } catch (Exception e) {
            model.addAttribute("errorMsg", "Error loading payroll data: " + e.getMessage());
            model.addAttribute("title", "Payroll Management");
            model.addAttribute("payrolls", List.of());
            model.addAttribute("month", LocalDate.now().getMonthValue());
            model.addAttribute("year", LocalDate.now().getYear());
            model.addAttribute("totalAmount", BigDecimal.ZERO);
            model.addAttribute("recordCount", 0);
            return "payroll/list";
        }
    }

    /**
     * Display details of a single payroll record
     */
    @GetMapping("/{id}")
    public String viewPayroll(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Payroll payroll = payrollService.getPayrollById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Payroll record not found"));
            
            model.addAttribute("title", "Payroll Details");
            model.addAttribute("payroll", payroll);
            model.addAttribute("monthName", getMonthName(payroll.getMonth()));
            
            return "payroll/detail";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", "Payroll record not found.");
            return "redirect:/payroll";
        }
    }

    /**
     * Show the form for generating new payroll
     */
    @GetMapping("/generate")
    public String showGenerateForm(Model model) {
        List<StaffProfile> staffList = staffService.getAllStaff();
        
        // Count staff with and without salary
        long staffWithSalary = staffList.stream()
                .filter(s -> s.getSalaryBase() != null && s.getSalaryBase().compareTo(BigDecimal.ZERO) > 0)
                .count();
        long staffWithoutSalary = staffList.size() - staffWithSalary;
        
        model.addAttribute("title", "Generate Monthly Payroll");
        model.addAttribute("staffList", staffList);
        model.addAttribute("staffWithSalary", staffWithSalary);
        model.addAttribute("staffWithoutSalary", staffWithoutSalary);
        
        if (staffWithoutSalary > 0) {
            model.addAttribute("warningMsg", staffWithoutSalary + " staff member(s) don't have salary information set.");
        }
        
        return "payroll/generate";
    }

    /**
     * Generate a new payroll record
     */
    @PostMapping("/generate")
    public String generatePayroll(
            @RequestParam Long staffId,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam BigDecimal allowances,
            @RequestParam BigDecimal deductions,
            RedirectAttributes ra) {
        
        try {
            StaffProfile staff = staffService.getStaffById(staffId)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            
            if (staff.getSalaryBase() == null) {
                ra.addFlashAttribute("errorMsg", "Staff member has no salary information.");
                return "redirect:/payroll/generate";
            }
            
            // Check if payroll already exists
            if (payrollService.payrollExists(staff.getUser(), month, year)) {
                ra.addFlashAttribute("errorMsg", "Payroll already exists for " + staff.getFullName() + " for this month and year.");
                return "redirect:/payroll/generate";
            }
            
            payrollService.generatePayroll(staff.getUser(), month, year, staff.getSalaryBase(), allowances, deductions);
            ra.addFlashAttribute("successMsg", "Payroll generated successfully for " + staff.getFullName());
            
            return "redirect:/payroll?month=" + month + "&year=" + year;
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error generating payroll: " + e.getMessage());
            return "redirect:/payroll/generate";
        }
    }

    /**
     * Show the edit form for a payroll record
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Payroll payroll = payrollService.getPayrollById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Payroll record not found"));
            
            model.addAttribute("title", "Edit Payroll");
            model.addAttribute("payroll", payroll);
            model.addAttribute("monthName", getMonthName(payroll.getMonth()));
            
            return "payroll/edit";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", "Payroll record not found.");
            return "redirect:/payroll";
        }
    }

    /**
     * Update an existing payroll record
     */
    @PostMapping("/update/{id}")
    public String updatePayroll(
            @PathVariable Long id,
            @RequestParam BigDecimal basicSalary,
            @RequestParam BigDecimal allowances,
            @RequestParam BigDecimal deductions,
            RedirectAttributes ra) {
        
        try {
            Payroll payroll = payrollService.updatePayroll(id, basicSalary, allowances, deductions);
            ra.addFlashAttribute("successMsg", "Payroll updated successfully.");
            
            return "redirect:/payroll/" + id;
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", "Error updating payroll: " + e.getMessage());
            return "redirect:/payroll";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "An unexpected error occurred: " + e.getMessage());
            return "redirect:/payroll";
        }
    }

    /**
     * Delete a payroll record
     */
    @PostMapping("/delete/{id}")
    public String deletePayroll(@PathVariable Long id, RedirectAttributes ra) {
        try {
            Payroll payroll = payrollService.getPayrollById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Payroll record not found"));
            
            int month = payroll.getMonth();
            int year = payroll.getYear();
            
            payrollService.deletePayroll(id);
            ra.addFlashAttribute("successMsg", "Payroll entry deleted successfully.");
            
            return "redirect:/payroll?month=" + month + "&year=" + year;
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", "Error deleting payroll: " + e.getMessage());
            return "redirect:/payroll";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "An unexpected error occurred: " + e.getMessage());
            return "redirect:/payroll";
        }
    }

    /**
     * Get payroll for a specific staff member
     */
    @GetMapping("/staff/{staffId}")
    public String staffPayrollHistory(@PathVariable Long staffId, Model model, RedirectAttributes ra) {
        try {
            StaffProfile staff = staffService.getStaffById(staffId)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
            
            List<Payroll> payrolls = payrollService.getPayrollByUser(staff.getUser());
            
            model.addAttribute("title", "Payroll History - " + staff.getFullName());
            model.addAttribute("staff", staff);
            model.addAttribute("payrolls", payrolls);
            
            return "payroll/staff-history";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", "Staff member not found.");
            return "redirect:/payroll";
        }
    }

    /**
     * Helper method to convert month number to month name
     */
    private String getMonthName(int month) {
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return month > 0 && month <= 12 ? months[month] : "Unknown";
    }
}
