package eminimki.com.JTS.Business.Rules;

import eminimki.com.JTS.Common.Utilities.Exceptions.BusinessException;
import eminimki.com.JTS.DataAccess.EmployeeRepository;
import eminimki.com.JTS.DataAccess.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class TaskBusinessRules {
    private TaskRepository taskRepository;
    private EmployeeRepository employeeRepository;

    public void checkDateBefore(String date){
        LocalDate today = LocalDate.now();
        LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        if (inputDate.isBefore(today)) {
            throw new BusinessException("The specified date is in the past!");
        }
    }
}
