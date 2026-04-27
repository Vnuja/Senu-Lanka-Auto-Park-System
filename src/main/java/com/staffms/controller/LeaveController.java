package com.staffms.controller;

import com.staffms.config.CustomUserDetails;
import com.staffms.model.entity.LeaveRequest;
import com.staffms.model.entity.User;
import com.staffms.service.LeaveService;
import com.staffms.util.LeaveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @GetMapping
    public String myLeaves(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        model.addAttribute("title", "My Leave Requests");
        model.addAttribute("leaves", leaveService.getLeavesByUser(user));
        return "leave/list";
    }

    @GetMapping("/new")
    public String leaveForm(Model model) {
        model.addAttribute("title", "Submit Leave Request");
        model.addAttribute("leaveRequest", new LeaveRequest());
        model.addAttribute("leaveTypes", LeaveType.values());
        return "leave/form";
    }

    @PostMapping("/save")
    public String saveLeave(@ModelAttribute LeaveRequest request, 
                           @RequestParam(required=false) String source,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           RedirectAttributes ra) {
        request.setUser(userDetails.getUser());
        leaveService.submitLeave(request);
        ra.addFlashAttribute("successMsg", "Leave request submitted successfully!");
        if ("profile".equals(source)) {
            return "redirect:/profile";
        }
        return "redirect:/leave";
    }

    @PostMapping("/delete/{id}")
    public String deleteLeave(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes ra) {
        leaveService.deleteLeave(id, userDetails.getUser());
        ra.addFlashAttribute("successMsg", "Leave request deleted successfully!");
        return "redirect:/leave";
    }
}
