package uz.company.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.company.task.domain.AppUser;
import uz.company.task.domain.Company;
import uz.company.task.domain.Employee;
import uz.company.task.domain.enums.UserType;
import uz.company.task.dto.EmployeeDTO;
import uz.company.task.dto.LoginDTO;
import uz.company.task.dto.ResponseDTO;
import uz.company.task.dto.TokenDTO;
import uz.company.task.errors.CommonException;
import uz.company.task.repository.EmployeeRepository;
import uz.company.task.security.SecurityUtils;
import uz.company.task.security.jwt.TokenProvider;
import uz.company.task.service.CompanyService;
import uz.company.task.service.EmployeeService;
import uz.company.task.service.UserService;

import java.util.List;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final CompanyService companyService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserService userService, PasswordEncoder passwordEncoder, TokenProvider tokenProvider, CompanyService companyService) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.companyService = companyService;
    }

    @Override
    public ResponseDTO createEmployee(EmployeeDTO employeeDTO) throws CommonException {
        Boolean userExists = userService.checkLoginAndEmail(employeeDTO.getUsername(), employeeDTO.getEmail());
        if (!userExists) {
            Company companyById = getCompany(employeeDTO.getCompanyId());

            AppUser appUser = new AppUser();
            appUser.setUserType(UserType.EMPLOYEE);
            appUser.setEmail(employeeDTO.getEmail());
            appUser.setUsername(employeeDTO.getUsername());
            appUser.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
            AppUser user = userService.createUser(appUser);

            Employee employee = new Employee();
            employee.setCompany(companyById);
            employee.setUser(user);
            employee.setFirstName(employeeDTO.getFirstName());
            employee.setLastName(employeeDTO.getLastName());

            Employee saved = employeeRepository.save(employee);
            return new ResponseDTO("Success", saved);
        }
        throw new CommonException("Username or Email is already in use");
    }



    @Override
    public ResponseDTO updateEmloyee(EmployeeDTO employeeDTO) throws CommonException {
        Company company = getCompany(employeeDTO.getCompanyId());
        AppUser user = userService.findByUsername(employeeDTO.getUsername());
        user.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        Employee employeeById = employeeRepository.findEmployeeById(employeeDTO.getId());
        employeeById.setLastName(employeeDTO.getLastName());
        employeeById.setFirstName(employeeDTO.getFirstName());
        employeeById.setUser(user);
        Employee saved = employeeRepository.save(employeeById);
        return new ResponseDTO("Success", saved);
    }

    @Override
    public ResponseDTO getAllEmployees(Long companyId) throws CommonException {
        Company company = getCompany(companyId);
        List<Employee> employees = employeeRepository.findAllByCompany(company);
        return new ResponseDTO("Success", employees);
    }

    @Override
    public ResponseDTO deleteEmployee(EmployeeDTO employeeDTO) throws CommonException {
        Company company = getCompany(employeeDTO.getCompanyId());
        Employee employeeById = employeeRepository.findEmployeeById(employeeDTO.getId());
        employeeRepository.deleteById(employeeDTO.getId());
        AppUser appUser = userService.deleteUser(employeeById.getUser());
        return new ResponseDTO("Success", appUser.getUsername());
    }

    @Override
    public TokenDTO authenticate(LoginDTO loginDTO) throws CommonException {
        AppUser appUser = userService.authenticateUser(loginDTO);
        Employee employee = employeeRepository.findEmployeeByUser(appUser);
        if (employee == null) {
            throw new CommonException("Employee with this credentials not found");
        }
        return userService.getToken(loginDTO,appUser);
    }

    private Company getCompany(Long companyId) throws CommonException {
        Company companyById = companyService.getCompanyById(companyId);
        String usernameFromToken = tokenProvider.getUsernameFromToken(SecurityUtils.getCredentials());

        if (!(companyById.getUser().getUsername().equals(usernameFromToken))) {
            throw new CommonException("It is not your companyId");
        }
        return companyById;
    }
}
