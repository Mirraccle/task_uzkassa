package uz.company.task.service;


import uz.company.task.dto.EmployeeDTO;
import uz.company.task.dto.LoginDTO;
import uz.company.task.dto.ResponseDTO;
import uz.company.task.dto.TokenDTO;
import uz.company.task.errors.CommonException;

public interface EmployeeService {

    ResponseDTO createEmployee(EmployeeDTO employeeDTO) throws CommonException;

    ResponseDTO updateEmloyee(EmployeeDTO employeeDTO) throws CommonException;

    ResponseDTO getAllEmployees(Long companyId) throws CommonException;

    ResponseDTO deleteEmployee(EmployeeDTO employeeDTO) throws CommonException;

    TokenDTO authenticate(LoginDTO loginDTO) throws CommonException;
}
