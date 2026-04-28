package com.staffms.controller;

import com.staffms.config.CustomUserDetails;
import com.staffms.model.entity.User;
import com.staffms.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public String viewAttendance(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        model.addAttribute("title", "My Attendance History");
        model.addAttribute("records", attendanceService.getAttendanceByUser(user));
        return "attendance/history";
    }

    @PostMapping("/check-in")
    public String checkIn(@AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes ra) {
        attendanceService.checkIn(userDetails.getUser());
        ra.addFlashAttribute("successMsg", "Checked in successfully!");
        return "redirect:/staff/dashboard";
    }

    @PostMapping("/check-out")
    public String checkOut(@AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes ra) {
        try {
            attendanceService.checkOut(userDetails.getUser());
            ra.addFlashAttribute("successMsg", "Checked out successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/staff/dashboard";
    }
}
