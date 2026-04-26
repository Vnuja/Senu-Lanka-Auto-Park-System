package com.staffms.controller;

import com.staffms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private PayrollService payrollService;
    @Autowired
    private StaffService staffService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Reports & Analytics");
        return "reports/index";
    }

    @GetMapping("/attendance")
    public String attendanceReport(@RequestParam(required = false) String start, 
                                   @RequestParam(required = false) String end, 
                                   Model model) {
        LocalDate startDate = (start != null) ? LocalDate.parse(start) : LocalDate.now().minusMonths(1);
        LocalDate endDate = (end != null) ? LocalDate.parse(end) : LocalDate.now();
        
        model.addAttribute("title", "Attendance Trend Report");
        model.addAttribute("records", attendanceService.getAttendanceByRange(startDate, endDate));
        return "reports/attendance";
    }

    @GetMapping("/payroll")
    public String payrollReport(@RequestParam(required = false) Integer month, 
                                @RequestParam(required = false) Integer year, 
                                Model model) {
        int m = (month != null) ? month : LocalDate.now().getMonthValue();
        int y = (year != null) ? year : LocalDate.now().getYear();
        
        model.addAttribute("title", "Payroll Cost Report");
        model.addAttribute("payrolls", payrollService.getPayrollByMonth(m, y));
        return "reports/payroll";
    }
}
