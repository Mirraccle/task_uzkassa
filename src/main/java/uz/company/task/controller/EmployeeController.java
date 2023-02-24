package uz.company.task.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.company.task.dto.EmployeeDTO;
import uz.company.task.dto.LoginDTO;
import uz.company.task.dto.ResponseDTO;
import uz.company.task.dto.TokenDTO;
import uz.company.task.errors.CommonException;
import uz.company.task.service.EmployeeService;

@Slf4j
@RestController
@RequestMapping("api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) throws CommonException {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateEmloyee(@RequestBody EmployeeDTO employeeDTO) throws CommonException {
        return ResponseEntity.ok(employeeService.updateEmloyee(employeeDTO));
    }

    @GetMapping("/get/all/{companyId}")
    public ResponseEntity<ResponseDTO> getAllEmployees(@PathVariable Long companyId) throws CommonException {
        return ResponseEntity.ok(employeeService.getAllEmployees(companyId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteEmployee(@RequestBody EmployeeDTO employeeDTO) throws CommonException {
        return ResponseEntity.ok(employeeService.deleteEmployee(employeeDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) throws CommonException {
        return ResponseEntity.ok(employeeService.authenticate(loginDTO));
    }
}
