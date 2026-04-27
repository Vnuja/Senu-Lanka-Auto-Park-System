package com.staffms.controller;

import com.staffms.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hr/history")
public class HistoryController {

    @Autowired
    private SystemLogService logService;

    @GetMapping
    public String viewHistory(Model model) {
        model.addAttribute("title", "System Activity History");
        model.addAttribute("logs", logService.getAllLogs());
        return "hr/history";
    }
}
