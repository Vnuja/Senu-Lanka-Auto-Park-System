package com.staffms.controller;

import com.staffms.config.CustomUserDetails;
import com.staffms.model.entity.SupportTicket;
import com.staffms.service.TicketService;
import com.staffms.service.UserService;
import com.staffms.util.TicketCategory;
import com.staffms.util.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String myTickets(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("title", "Support Tickets");
        if (userDetails.getUser().getRole().name().equals("IT_SUPPORT")) {
            model.addAttribute("tickets", ticketService.getAllTickets());
        } else {
            model.addAttribute("tickets", ticketService.getTicketsByUser(userDetails.getUser()));
        }
        return "tickets/list";
    }

    @GetMapping("/new")
    public String ticketForm(Model model) {
        model.addAttribute("title", "Raise Support Ticket");
        model.addAttribute("ticket", new SupportTicket());
        model.addAttribute("categories", TicketCategory.values());
        return "tickets/form";
    }

    @PostMapping("/save")
    public String saveTicket(@ModelAttribute SupportTicket ticket,
                             @RequestParam(required=false) String source,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes ra) {
        ticket.setRaisedBy(userDetails.getUser());
        ticketService.createTicket(ticket);
        ra.addFlashAttribute("successMsg", "Ticket raised successfully! Our support team will review it.");
        if ("profile".equals(source)) {
            return "redirect:/profile";
        }
        return "redirect:/tickets";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam TicketStatus status, RedirectAttributes ra) {
        ticketService.updateTicketStatus(id, status);
        ra.addFlashAttribute("successMsg", "Ticket status updated to " + status);
        return "redirect:/tickets";
    }
}
