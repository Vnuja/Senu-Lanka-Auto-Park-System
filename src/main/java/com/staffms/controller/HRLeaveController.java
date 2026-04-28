package com.staffms.controller;

import com.staffms.config.CustomUserDetails;
import com.staffms.model.entity.LeaveRequest;
import com.staffms.service.LeaveService;
import com.staffms.util.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hr/leave")
public class HRLeaveController {

    @Autowired
    private LeaveService leaveService;

    @GetMapping("/review/{id}")
    public String reviewForm(@PathVariable Long id, Model model) {
        LeaveRequest leave = leaveService.getLeaveById(id);
        model.addAttribute("title", "Review Leave Request");
        model.addAttribute("leave", leave);
        return "hr/leave/review";
    }

    @PostMapping("/approve/{id}")
    public String approveLeave(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes ra) {
        leaveService.reviewLeave(id, RequestStatus.APPROVED, userDetails.getUser());
        ra.addFlashAttribute("successMsg", "Leave request approved.");
        return "redirect:/hr/dashboard";
    }

    @PostMapping("/reject/{id}")
    public String rejectLeave(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes ra) {
        leaveService.reviewLeave(id, RequestStatus.REJECTED, userDetails.getUser());
        ra.addFlashAttribute("successMsg", "Leave request rejected.");
        return "redirect:/hr/dashboard";
    }
}
