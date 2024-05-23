package eminimki.com.JTS.WebAPI.Controllers;

import eminimki.com.JTS.Business.Services.EmployeeService;
import eminimki.com.JTS.Business.requests.EmployeeRequests.CreateEmployeeRequest;
import eminimki.com.JTS.Business.requests.EmployeeRequests.UpdateEmployeeNoPasswordRequest;
import eminimki.com.JTS.Business.requests.EmployeeRequests.UpdateEmployeeRequest;
import eminimki.com.JTS.Business.responses.Employee.GetAllEmployeeResponse;
import eminimki.com.JTS.Business.responses.Employee.GetAllUsernameResponse;
import eminimki.com.JTS.Business.responses.Employee.GetByIdEmployeeResponse;
import eminimki.com.JTS.Business.responses.Employee.GetEmployeeByUsernameResponse;
import eminimki.com.JTS.Entities.EmployeesDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeesControllers {
    private EmployeeService employeeService;

    @GetMapping("/get/all")
    public List<GetAllEmployeeResponse> getAll(){
        return employeeService.getAll();
    }

    @GetMapping("/get/allUsername")
    public List<GetAllUsernameResponse> getAllUsername(){
        return employeeService.getAllUsername();
    }

    @GetMapping("/getDetails/{employeeId}")
    public GetByIdEmployeeResponse getById(@PathVariable int employeeId){
        return employeeService.getByID(employeeId);
    }

    @PostMapping("/post/add")
    public ResponseEntity<EmployeesDetails> add(@RequestBody CreateEmployeeRequest createEmployeeRequest){
        EmployeesDetails response = this.employeeService.add(createEmployeeRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{employeeId}")
    public void delete(@PathVariable int employeeId ){
        this.employeeService.delete(employeeId);

    }

    @PutMapping("/put/update")
    public ResponseEntity<EmployeesDetails> update(@RequestBody UpdateEmployeeRequest updateEmployeeRequest){
        EmployeesDetails response = this.employeeService.update(updateEmployeeRequest);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/put/updateNoPassword")
    public ResponseEntity<EmployeesDetails> update(@RequestBody UpdateEmployeeNoPasswordRequest updateEmployeeNoPasswordRequest){
        EmployeesDetails response = this.employeeService.update(updateEmployeeNoPasswordRequest);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get/username/{username}")
    public GetEmployeeByUsernameResponse getUsernameByUsername(@PathVariable String username){
        return this.employeeService.getByUsername(username);

    }






}
