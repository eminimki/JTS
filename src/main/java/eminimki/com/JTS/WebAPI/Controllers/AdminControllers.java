package eminimki.com.JTS.WebAPI.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminControllers {

    @GetMapping("/dashboard")
    public String showAdminPage() {
        return "admin/dashboard";
    }

    @GetMapping("/employees")
    public String showAdminEmployeePage() {
        return "admin/employees";
    }

    @GetMapping("/employees/details/{username}")
    public String showAdminEmployeeDetailsPage(@PathVariable String username){return "admin/employeeDetails";}

    @GetMapping("/tasks")
    public String showAdminTasksPage() {
        return "admin/tasks";
    }

    @GetMapping("/logs")
    public String showAdminLogsPage() {
        return "admin/logs";
    }
}
