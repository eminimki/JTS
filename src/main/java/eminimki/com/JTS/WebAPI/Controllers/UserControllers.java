package eminimki.com.JTS.WebAPI.Controllers;
/*Bu class security için çalışmaktadır. employee nesne ile işlemler yapar.*/

import eminimki.com.JTS.Business.Manager.EmployeeManager;
import eminimki.com.JTS.Business.Services.EmployeeService;
import eminimki.com.JTS.Business.requests.EmployeeRequests.CreateEmployeeRequest;
import eminimki.com.JTS.Business.responses.Employee.GetEmployeeNoPasswordResponse;
import eminimki.com.JTS.Security.AuthRequest;
import eminimki.com.JTS.Security.Jwt.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@AllArgsConstructor
public class UserControllers {

    private final EmployeeManager employeeManager;
    private final EmployeeService employeeService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    @PostMapping("/addAdmin")
    public void addAdmin(CreateEmployeeRequest request) {


        request.setRole("ROLE_ADMIN");
        request.setName("admin");
        request.setMail("admin@eminimki.com");
        request.setUsername("admin");
        request.setLastname("");
        request.setPassword("admin");
        request.setAddress("System Adress");
        request.setPosition("System Admin");
        this.employeeManager.add(request);
    }

    @PostMapping("/generateToken")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(jwtService.generateToken(request.username()));
        }
        throw new UsernameNotFoundException("invalid username {} " + request.username());
    }

    @GetMapping("/get/user")
    public GetEmployeeNoPasswordResponse getCurrentUser(){
        return employeeService.getCurrentUser();

    }

}