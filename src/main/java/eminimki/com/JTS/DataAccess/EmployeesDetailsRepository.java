package eminimki.com.JTS.DataAccess;

import eminimki.com.JTS.Entities.EmployeesDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesDetailsRepository extends JpaRepository<EmployeesDetails, Integer> {
}
