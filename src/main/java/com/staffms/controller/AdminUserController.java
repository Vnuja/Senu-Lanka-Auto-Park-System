package com.staffms.controller;

import com.staffms.model.entity.User;
import com.staffms.service.UserService;
import com.staffms.util.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("title", "Manage Users");
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users/list";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("title", "Create New User");
        model.addAttribute("user", new User());
        model.addAttribute("roles", UserRole.values());
        return "admin/users/form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, RedirectAttributes ra) {
        if (user.getUserId() == null) {
            userService.createUser(user);
            ra.addFlashAttribute("successMsg", "User created successfully!");
        } else {
            User existing = userService.getUserById(user.getUserId()).orElseThrow();
            existing.setUsername(user.getUsername());
            existing.setEmail(user.getEmail());
            existing.setRole(user.getRole());
            existing.setActive(user.isActive());
            userService.updateUser(existing);
            ra.addFlashAttribute("successMsg", "User updated successfully!");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id).orElseThrow();
        model.addAttribute("title", "Edit User");
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());
        return "admin/users/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.deleteUser(id);
        ra.addFlashAttribute("successMsg", "User deleted successfully!");
        return "redirect:/admin/users";
    }
}
