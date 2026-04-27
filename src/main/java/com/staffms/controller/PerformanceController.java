package com.staffms.controller;

import com.staffms.config.CustomUserDetails;
import com.staffms.model.entity.Performance;
import com.staffms.model.entity.StaffProfile;
import com.staffms.service.PerformanceService;
import com.staffms.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/performance")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;
    @Autowired
    private StaffService staffService;

    @GetMapping
    public String myPerformance(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("title", "My Performance Evaluations");
        model.addAttribute("evals", performanceService.getEvaluationsByUser(userDetails.getUser()));
        return "performance/list";
    }

    @GetMapping("/evaluate/{userId}")
    public String evaluationForm(@PathVariable Long userId, Model model) {
        model.addAttribute("title", "Staff Performance Evaluation");
        model.addAttribute("targetUserId", userId);
        model.addAttribute("performance", new Performance());
        return "performance/form";
    }

    @PostMapping("/save")
    public String saveEvaluation(@ModelAttribute Performance performance,
            @RequestParam Long targetUserId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes ra) {
        performance.setEvaluatedBy(userDetails.getUser());
        // For simplicity, setting the user object directly. In real app, fetch from DB.
        performance.setUser(staffService.getStaffByUserId(targetUserId).get().getUser());
        performanceService.submitEvaluation(performance);
        ra.addFlashAttribute("successMsg", "Evaluation submitted successfully!");
        return "redirect:/manager/profile";
    }

    @PostMapping("/update/{id}")
    public String updateEvaluation(@PathVariable Long id, 
                                   @RequestParam String period, 
                                   @RequestParam int rating, 
                                   @RequestParam String comments, 
                                   RedirectAttributes ra) {
        performanceService.updateEvaluation(id, period, rating, comments);
        ra.addFlashAttribute("successMsg", "Evaluation updated successfully!");
        return "redirect:/performance";
    }

    @PostMapping("/delete/{id}")
    public String deleteEvaluation(@PathVariable Long id, RedirectAttributes ra) {
        performanceService.deleteEvaluation(id);
        ra.addFlashAttribute("successMsg", "Evaluation deleted successfully!");
        return "redirect:/performance";
    }
}
