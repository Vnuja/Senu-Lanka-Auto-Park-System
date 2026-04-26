package com.staffms.controller;

import com.staffms.repository.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/logs")
public class AdminLogController {

    @Autowired
    private SystemLogRepository logRepository;

    @GetMapping
    public String viewLogs(Model model) {
        model.addAttribute("title", "System Activity Logs");
        model.addAttribute("logs", logRepository.findByOrderByTimestampDesc());
        return "admin/logs";
    }
}
