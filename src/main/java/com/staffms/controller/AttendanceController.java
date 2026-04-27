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
        return "redirect:/staff/profile";
    }

    @PostMapping("/check-out")
    public String checkOut(@AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes ra) {
        try {
            attendanceService.checkOut(userDetails.getUser());
            ra.addFlashAttribute("successMsg", "Checked out successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/staff/profile";
    }

    @PostMapping("/update/{id}")
    public String updateAttendance(@org.springframework.web.bind.annotation.PathVariable Long id,
                                   @org.springframework.web.bind.annotation.RequestParam(required=false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.TIME) java.time.LocalTime checkIn,
                                   @org.springframework.web.bind.annotation.RequestParam(required=false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.TIME) java.time.LocalTime checkOut,
                                   @org.springframework.web.bind.annotation.RequestParam com.staffms.util.AttendanceStatus status,
                                   RedirectAttributes ra) {
        attendanceService.updateAttendance(id, checkIn, checkOut, status);
        ra.addFlashAttribute("successMsg", "Attendance record updated successfully!");
        return "redirect:/attendance";
    }

    @PostMapping("/delete/{id}")
    public String deleteAttendance(@org.springframework.web.bind.annotation.PathVariable Long id, RedirectAttributes ra) {
        attendanceService.deleteAttendance(id);
        ra.addFlashAttribute("successMsg", "Attendance record deleted successfully!");
        return "redirect:/attendance";
    }
}
