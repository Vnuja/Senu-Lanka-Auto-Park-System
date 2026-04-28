package com.staffms.controller;

import com.staffms.service.*;
import com.staffms.model.entity.User;
import com.staffms.model.entity.Attendance;
import com.staffms.repository.AttendanceRepository;
import com.staffms.model.entity.SystemLog;
import com.staffms.repository.SystemLogRepository;
import com.staffms.config.CustomUserDetails;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private SystemLogRepository logRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("title", "Admin Dashboard");
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("activeTickets", ticketService.getAllOpenTickets().size());
        model.addAttribute("pendingLeaves", leaveService.getPendingLeaves().size());
        model.addAttribute("recentLogs", logRepository.findByOrderByTimestampDesc().stream().limit(5).toList());
        return "dashboards/admin";
    }

    @GetMapping("/hr/dashboard")
    public String hrDashboard(Model model) {
        model.addAttribute("title", "HR Dashboard");
        model.addAttribute("pendingLeaves", leaveService.getPendingLeaves().size());
        model.addAttribute("totalStaff", staffService.getAllStaff().size());
        model.addAttribute("payrollGenerated", false);
        model.addAttribute("leaves", leaveService.getPendingLeaves());
        return "dashboards/hr";
    }

    @GetMapping("/staff/dashboard")
    public String staffDashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        model.addAttribute("title", "My Dashboard");
        model.addAttribute("myTickets", ticketService.getTicketsByUser(user).size());
        model.addAttribute("myLeaves", leaveService.getLeavesByUser(user).size());
        model.addAttribute("todayAttendance", attendanceRepository.findByUserAndDate(user, LocalDate.now()).orElse(null));
        model.addAttribute("recentAttendance", attendanceService.getAttendanceByUser(user).stream().limit(5).toList());
        return "dashboards/staff";
    }

    @GetMapping("/manager/dashboard")
    public String managerDashboard(Model model) {
        model.addAttribute("title", "Manager Dashboard");
        model.addAttribute("pendingLeaves", leaveService.getPendingLeaves().size());
        return "dashboards/manager";
    }

    @GetMapping("/it/dashboard")
    public String itDashboard(Model model) {
        model.addAttribute("title", "IT Support Dashboard");
        model.addAttribute("openTickets", ticketService.getAllOpenTickets().size());
        return "dashboards/it";
    }
    
    @GetMapping("/analytics/dashboard")
    public String analyticsDashboard(Model model) {
        model.addAttribute("title", "Analytics Dashboard");
        return "dashboards/analytics";
    }
}
