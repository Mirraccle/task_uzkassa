package uz.company.task.service;

import uz.company.task.domain.AppUser;
import uz.company.task.domain.Company;
import uz.company.task.dto.*;
import uz.company.task.errors.CommonException;

public interface CompanyService {

    ResponseDTO createCompany(CompanyDTO company) throws CommonException;

    Company findCompanyByUser(AppUser user);

    ResponseDTO activateCompany(String code);

    TokenDTO authenticate(LoginDTO loginDTO) throws CommonException;


    ResponseDTO updateCompany(CompanyDataDTO company) throws CommonException;

    Company getCompanyById(Long id);
}
