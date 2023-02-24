package uz.company.task.dto;

import lombok.Data;
import uz.company.task.domain.Company;
import uz.company.task.domain.enums.Status;

@Data
public class CompanyDTO {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String companyName;

    private String companyAddress;

    private String companyZipCode;

    public Company toEntity() {
        Company company = new Company();
        company.setCompanyName(companyName);
        company.setCompanyAddress(companyAddress);
        company.setCompanyZipCode(companyZipCode);
        company.setStatus(Status.NOT_ACTIVE);
        return company;
    }
}
