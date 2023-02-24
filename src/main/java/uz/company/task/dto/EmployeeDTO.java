package uz.company.task.dto;

import lombok.Data;

@Data
public class EmployeeDTO {

    private Long id;

    private Long companyId;

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;
}
