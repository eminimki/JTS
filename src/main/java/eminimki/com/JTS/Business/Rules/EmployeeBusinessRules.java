package eminimki.com.JTS.Business.Rules;

import eminimki.com.JTS.Business.requests.EmployeeRequests.CreateEmployeeRequest;
import eminimki.com.JTS.Common.Utilities.Exceptions.BusinessException;
import eminimki.com.JTS.DataAccess.EmployeeRepository;
import eminimki.com.JTS.Entities.Employee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EmployeeBusinessRules {
    private EmployeeRepository employeeRepository;

    // Mail daha önce kullanıldı mı?
    public void checkIfEmployeeMailExists(String email){
        if (employeeRepository.existsByMail(email)){
            throw new BusinessException("Employee Mail already exists!");
        }
    }
    public void checkIfEmployeeMailExists(String email, int targetID) {
        // Belirtilen e-posta adresine sahip başka bir çalışan var mı kontrol et
        List<Employee> employeesWithSameEmail = employeeRepository.findByMail(email);

        // Eğer hiçbir çalışan bu e-posta adresini kullanmıyorsa veya sadece hedeflenen çalışan bu e-posta adresini kullanıyorsa devam et
        if (employeesWithSameEmail.isEmpty() || (employeesWithSameEmail.size() == 1 && employeesWithSameEmail.get(0).getId() == targetID)) {
            return;
        } else {
            throw new BusinessException("Employee Mail already exists!");
        }
    }

    // Username daha önce kullanıldı mı?
    public void checkIfEmployeeUserNameExists(String username){
        if (employeeRepository.existsByUsername(username)){
            throw new BusinessException("Employee Username already exists!");
        }
    }
    public void checkIfEmployeeUserNameExists(String username, int targetID) {
        // Belirtilen kullanıcı adına sahip başka bir çalışan var mı kontrol et
        Optional<Employee> employeesWithSameUsername = employeeRepository.findByUsername(username);

        // Eğer hiçbir çalışan bu kullanıcı adını kullanmıyorsa veya sadece hedeflenen çalışan bu kullanıcı adını kullanıyorsa devam et
        if (employeesWithSameUsername.isEmpty() || (employeesWithSameUsername.stream().count() == 1 && employeesWithSameUsername.get().getId() == targetID)) {
            return;
        } else {
            throw new BusinessException("Employee Username already exists!");
        }
    }

    // Add ile post edilen çalışan rol kontrolü
    public CreateEmployeeRequest checkIfEmployeeRole(CreateEmployeeRequest createEmployeeRequest ){
        List<String> validRoles = Arrays.asList("ADMIN", "MANAGER", "MOD", "EMPLOYEE" ,
                "ROLE_ADMIN" , "ROLE_MANAGER" , "ROLE_MOD" , "ROLE_EMPLOYEE");
        if (validRoles.contains(createEmployeeRequest.getRole())) {
            if (!createEmployeeRequest.getRole().startsWith("ROLE_")) {
                createEmployeeRequest.setRole("ROLE_" + createEmployeeRequest.getRole());
            }
        } else {
            throw new BusinessException("Employee Role invalid!");
        }
        return createEmployeeRequest;
    }

}
