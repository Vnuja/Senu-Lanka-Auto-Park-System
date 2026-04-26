package com.staffms.controller;

import com.staffms.config.CustomUserDetails;
import com.staffms.model.entity.LeaveRequest;
import com.staffms.model.entity.SupportTicket;
import com.staffms.model.entity.User;
import com.staffms.model.entity.StaffProfile;
import com.staffms.service.LeaveService;
import com.staffms.service.StaffService;
import com.staffms.service.TicketService;
import com.staffms.util.LeaveType;
import com.staffms.util.TicketCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private StaffService staffService;
    
    @Autowired
    private LeaveService leaveService;
    
    @Autowired
    private TicketService ticketService;

    @GetMapping
    public String viewProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        model.addAttribute("title", "My Profile");
        model.addAttribute("user", user);
        
        Optional<StaffProfile> profileOpt = staffService.getStaffByUserId(user.getUserId());
        if(profileOpt.isPresent()) {
            model.addAttribute("profile", profileOpt.get());
        }
        
        model.addAttribute("leaves", leaveService.getLeavesByUser(user));
        model.addAttribute("leaveRequest", new LeaveRequest());
        model.addAttribute("leaveTypes", LeaveType.values());
        
        model.addAttribute("tickets", ticketService.getTicketsByUser(user));
        model.addAttribute("ticket", new SupportTicket());
        model.addAttribute("categories", TicketCategory.values());
        
        return "profile";
    }
}
