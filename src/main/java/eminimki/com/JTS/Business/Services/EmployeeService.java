package eminimki.com.JTS.Business.Services;

import eminimki.com.JTS.Business.requests.EmployeeRequests.CreateEmployeeRequest;
import eminimki.com.JTS.Business.requests.EmployeeRequests.UpdateEmployeeNoPasswordRequest;
import eminimki.com.JTS.Business.requests.EmployeeRequests.UpdateEmployeeRequest;
import eminimki.com.JTS.Business.responses.Employee.*;
import eminimki.com.JTS.Entities.EmployeesDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface EmployeeService extends UserDetailsService {

    // Employee Controllers Methods
    List<GetAllEmployeeResponse> getAll();
    List<GetAllUsernameResponse> getAllUsername();
    GetByIdEmployeeResponse getByID(int employeeID);
    GetEmployeeByUsernameResponse getByUsername(String username);

    EmployeesDetails add(CreateEmployeeRequest createEmployeeRequest);

    void delete(int employeeID);

    EmployeesDetails update(UpdateEmployeeRequest updateEmployeeRequest);
    EmployeesDetails update(UpdateEmployeeNoPasswordRequest updateEmployeeNoPasswordRequest);

    // User Controllers Methods
    GetEmployeeNoPasswordResponse getCurrentUser();


}
