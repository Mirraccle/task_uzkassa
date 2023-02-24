package uz.company.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.company.task.domain.AppUser;
import uz.company.task.domain.Company;
import uz.company.task.domain.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findEmployeeById(Long id);

    List<Employee> findAllByCompany(Company company);

    Employee findEmployeeByUser(AppUser user);
}
