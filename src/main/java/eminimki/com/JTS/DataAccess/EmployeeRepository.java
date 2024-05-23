package eminimki.com.JTS.DataAccess;

import eminimki.com.JTS.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByMail(String mail);
    boolean existsByUsername(String username);


    Optional<Employee> findByUsername(String username);
    List<Employee> findByMail(String email);
}
