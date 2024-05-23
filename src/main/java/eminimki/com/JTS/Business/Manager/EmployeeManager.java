package eminimki.com.JTS.Business.Manager;

import eminimki.com.JTS.Business.Rules.EmployeeBusinessRules;
import eminimki.com.JTS.Business.Services.EmployeeService;
import eminimki.com.JTS.Business.Services.LogService;
import eminimki.com.JTS.Business.requests.EmployeeRequests.CreateEmployeeRequest;
import eminimki.com.JTS.Business.requests.EmployeeRequests.UpdateEmployeeNoPasswordRequest;
import eminimki.com.JTS.Business.requests.EmployeeRequests.UpdateEmployeeRequest;
import eminimki.com.JTS.Business.responses.Employee.*;
import eminimki.com.JTS.Common.Utilities.CommonUtils;
import eminimki.com.JTS.Common.Utilities.Manager.EmailManager;
import eminimki.com.JTS.Common.Utilities.Mapper.ModelMapperService;
import eminimki.com.JTS.DataAccess.EmployeeRepository;
import eminimki.com.JTS.DataAccess.EmployeesDetailsRepository;
import eminimki.com.JTS.Entities.Employee;
import eminimki.com.JTS.Entities.EmployeesDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeManager implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private EmployeesDetailsRepository employeesDetailsRepository;
    private ModelMapperService modelMapperService;
    private EmployeeBusinessRules employeeBusinessRules;
    private BCryptPasswordEncoder passwordEncoder;
    private LogService logService;
    private EmailManager emailManager;




    @Override
    public List<GetAllEmployeeResponse> getAll() {
        List<Employee> employees = employeeRepository.findAll();
        List<GetAllEmployeeResponse> employeeResponses = employees.stream()
                .map(employee -> this.modelMapperService.forResponse().map(employee, GetAllEmployeeResponse.class))
                .collect(Collectors.toList());

        return employeeResponses;
    }

    @Override
    public List<GetAllUsernameResponse> getAllUsername() {
        List<Employee> employees = employeeRepository.findAll();
        List<GetAllUsernameResponse> employeeResponses = employees.stream()
                .map(employee -> this.modelMapperService.forResponse().map(employee, GetAllUsernameResponse.class))
                .collect(Collectors.toList());

        return employeeResponses;
    }

    @Override
    public GetByIdEmployeeResponse getByID(int employeeID) {
        Employee employee = this.employeeRepository.findById(employeeID).orElseThrow();
        GetByIdEmployeeResponse getByIdEmployeeResponse =
                this.modelMapperService.forResponse().map(employee, GetByIdEmployeeResponse.class);
        return getByIdEmployeeResponse;
    }

    @Override
    public GetEmployeeByUsernameResponse getByUsername(String username) {
        Employee employee = this.employeeRepository.findByUsername(username).orElseThrow();
        GetEmployeeByUsernameResponse getEmployeeByUsernameResponse =
                this.modelMapperService.forResponse().map(employee, GetEmployeeByUsernameResponse.class);
        return getEmployeeByUsernameResponse;
    }

    @Override
    public EmployeesDetails add(CreateEmployeeRequest createEmployeeRequest) {
        String currentTime = CommonUtils.getCurrentDateTimeFormatted();

        this.employeeBusinessRules.checkIfEmployeeMailExists(createEmployeeRequest.getMail());
        this.employeeBusinessRules.checkIfEmployeeUserNameExists(createEmployeeRequest.getUsername());
        createEmployeeRequest = this.employeeBusinessRules.checkIfEmployeeRole(createEmployeeRequest);
        String temporaryPassword = createEmployeeRequest.getPassword();

        createEmployeeRequest.setPassword(passwordEncoder.encode(createEmployeeRequest.getPassword()));

        Employee employee = this.modelMapperService.forRequest().map(createEmployeeRequest, Employee.class );
        EmployeesDetails employeesDetails = this.modelMapperService.forRequest().map(createEmployeeRequest, EmployeesDetails.class);

        employee.setCreatedTime(currentTime);
        employeesDetails.setCreatedTime(currentTime);
        employee.setModifiedTime(currentTime);
        employeesDetails.setModifiedTime(currentTime);



        // Employee nesnesini kaydetme
        Employee savedEmployee = this.employeeRepository.save(employee);

        // EmployeesDetails nesnesini kaydetme
        employeesDetails.setEmployee(savedEmployee);
        EmployeesDetails savedEmployeesDetails = this.employeesDetailsRepository.save(employeesDetails);


        this.logService.add("system", employee.getUsername() + " username ile yeni bir çalışan eklendi.");

        this.employeeRepository.save(employee);


        this.emailManager.sendEmployeeCreateMail(employee,temporaryPassword);

        return savedEmployeesDetails;
    }


    @Override
    public void delete(int employeeID) {
        this.employeeRepository.deleteById(employeeID);
    }

    @Override
    public EmployeesDetails update(UpdateEmployeeRequest updateEmployeeRequest) {
        String currentTime = CommonUtils.getCurrentDateTimeFormatted();

        this.employeeBusinessRules.checkIfEmployeeMailExists(updateEmployeeRequest.getMail(), updateEmployeeRequest.getId());
        this.employeeBusinessRules.checkIfEmployeeUserNameExists(updateEmployeeRequest.getUsername(), updateEmployeeRequest.getId());

        updateEmployeeRequest.setPassword(passwordEncoder.encode(updateEmployeeRequest.getPassword()));
        Optional<Employee> oldEmployee = this.employeeRepository.findById(updateEmployeeRequest.getId());

        Employee employee = this.modelMapperService.forRequest().map(updateEmployeeRequest, Employee.class );
        EmployeesDetails employeesDetails = this.modelMapperService.forRequest().map(updateEmployeeRequest, EmployeesDetails.class);

        employee.setCreatedTime(oldEmployee.get().getCreatedTime());
        employee.setModifiedTime(currentTime);
        employeesDetails.setModifiedTime(currentTime);
        employeesDetails.setCreatedTime(oldEmployee.get().getCreatedTime());

        // Employee nesnesini kaydetme
        Employee savedEmployee = this.employeeRepository.save(employee);

        // EmployeesDetails nesnesini kaydetme
        employeesDetails.setEmployee(savedEmployee);
        EmployeesDetails savedEmployeesDetails = this.employeesDetailsRepository.save(employeesDetails);

        this.employeeRepository.save(employee);

        this.logService.add("admin", employee.getUsername() + " usernameli kullanıcının bilgileri güncellendi.");

        return savedEmployeesDetails;

    }

    @Override
    public EmployeesDetails update(UpdateEmployeeNoPasswordRequest updateEmployeeNoPasswordRequest) {
        String currentTime = CommonUtils.getCurrentDateTimeFormatted();
        this.employeeBusinessRules.checkIfEmployeeMailExists(updateEmployeeNoPasswordRequest.getMail(), updateEmployeeNoPasswordRequest.getId());
        this.employeeBusinessRules.checkIfEmployeeUserNameExists(updateEmployeeNoPasswordRequest.getUsername(), updateEmployeeNoPasswordRequest.getId());

        Optional<Employee> oldEmployee = this.employeeRepository.findById(updateEmployeeNoPasswordRequest.getId());


        Employee employee = this.modelMapperService.forRequest().map(updateEmployeeNoPasswordRequest, Employee.class );
        employee.setPassword(passwordEncoder.encode(oldEmployee.get().getPassword()));
        EmployeesDetails employeesDetails = this.modelMapperService.forRequest().map(updateEmployeeNoPasswordRequest, EmployeesDetails.class);

        employee.setCreatedTime(oldEmployee.get().getCreatedTime());
        employee.setModifiedTime(currentTime);
        employeesDetails.setModifiedTime(currentTime);
        employeesDetails.setCreatedTime(oldEmployee.get().getCreatedTime());

        // Employee nesnesini kaydetme
        Employee savedEmployee = this.employeeRepository.save(employee);

        // EmployeesDetails nesnesini kaydetme
        employeesDetails.setEmployee(savedEmployee);
        EmployeesDetails savedEmployeesDetails = this.employeesDetailsRepository.save(employeesDetails);


        this.logService.add("admin", employee.getUsername() + " usernameli kullanıcının bilgileri güncellendi.");

        this.employeeRepository.save(employee);


        return savedEmployeesDetails;
    }



    //Security

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> user = employeeRepository.findByUsername(username);
        Employee employee = user.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new EmployeeDetails(employee); // Employee nesnesini EmployeeDetails'e dönüştür
    }

    @Override
    public GetEmployeeNoPasswordResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return modelMapperService.forResponse().
                map(employeeRepository.findByUsername(authentication.getName()),GetEmployeeNoPasswordResponse.class);
    }


}
