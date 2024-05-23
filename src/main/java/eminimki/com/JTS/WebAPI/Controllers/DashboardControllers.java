package eminimki.com.JTS.WebAPI.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardControllers {

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "/employee/dashboard";
    }

    @GetMapping("/profile/{username}")
    public String showProfile(@PathVariable String username) {
        return "/employee/profile";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "/employee/profile";
    }

    @GetMapping("/logs")
    public String showLogs(){
        return "/employee/logs";
    }
}
