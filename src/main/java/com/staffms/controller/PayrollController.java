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

    @GetMapping
    public String payrollList(Model model) {
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        List<Payroll> payrolls = payrollService.getPayrollByMonth(month, year);
        
        model.addAttribute("title", "Payroll Management");
        model.addAttribute("payrolls", payrolls);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        return "payroll/list";
    }

    @GetMapping("/generate")
    public String showGenerateForm(Model model) {
        model.addAttribute("title", "Generate Monthly Payroll");
        model.addAttribute("staffList", staffService.getAllStaff());
        return "payroll/generate";
    }

    @PostMapping("/generate")
    public String generatePayroll(@RequestParam Long staffId,
                                 @RequestParam int month,
                                 @RequestParam int year,
                                 @RequestParam BigDecimal allowances,
                                 @RequestParam BigDecimal deductions,
                                 RedirectAttributes ra) {
        StaffProfile staff = staffService.getStaffById(staffId).orElseThrow();
        payrollService.generatePayroll(staff.getUser(), month, year, staff.getSalaryBase(), allowances, deductions);
        ra.addFlashAttribute("successMsg", "Payroll generated for " + staff.getFullName());
        return "redirect:/payroll";
    }
}
