package com.staffms.controller;

import com.staffms.model.entity.StaffProfile;
import com.staffms.service.StaffService;
import com.staffms.service.UserService;
import com.staffms.util.EmploymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hr/staff")
public class HRStaffController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String listStaff(Model model) {
        model.addAttribute("title", "Staff Directory");
        model.addAttribute("staffList", staffService.getAllStaff());
        return "hr/staff/list";
    }

    @GetMapping("/new")
    public String newStaffForm(Model model) {
        model.addAttribute("title", "Add New Staff Member");
        model.addAttribute("profile", new StaffProfile());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("departments", staffService.getAllDepartments());
        model.addAttribute("empTypes", EmploymentType.values());
        return "hr/staff/form";
    }

    @PostMapping("/save")
    public String saveStaff(@ModelAttribute StaffProfile profile, RedirectAttributes ra) {
        staffService.saveStaffProfile(profile);
        ra.addFlashAttribute("successMsg", "Staff profile saved successfully!");
        return "redirect:/hr/staff";
    }

    @GetMapping("/edit/{id}")
    public String editStaffForm(@PathVariable Long id, Model model) {
        StaffProfile profile = staffService.getStaffById(id).orElseThrow();
        model.addAttribute("title", "Edit Staff Member");
        model.addAttribute("profile", profile);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("departments", staffService.getAllDepartments());
        model.addAttribute("empTypes", EmploymentType.values());
        return "hr/staff/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(@PathVariable Long id, RedirectAttributes ra) {
        staffService.deleteStaffProfile(id);
        ra.addFlashAttribute("successMsg", "Staff profile deleted successfully!");
        return "redirect:/hr/staff";
    }
}
