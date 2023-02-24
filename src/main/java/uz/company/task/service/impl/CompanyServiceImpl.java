package uz.company.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.company.task.domain.AppUser;
import uz.company.task.domain.Company;
import uz.company.task.domain.enums.Status;
import uz.company.task.domain.enums.UserType;
import uz.company.task.dto.*;
import uz.company.task.errors.CommonException;
import uz.company.task.repository.CompanyRepository;
import uz.company.task.security.SecurityUtils;
import uz.company.task.security.jwt.TokenProvider;
import uz.company.task.service.CompanyService;
import uz.company.task.service.EmailService;
import uz.company.task.service.UserService;

import java.util.UUID;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final EmailService emailService;

    private final TokenProvider tokenProvider;

    public CompanyServiceImpl(CompanyRepository companyRepository, PasswordEncoder passwordEncoder, UserService userService, EmailService emailService, TokenProvider tokenProvider) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailService = emailService;
        this.tokenProvider = tokenProvider;
    }
    @Override
    public ResponseDTO createCompany(CompanyDTO companyDTO) throws CommonException {
        if (!userService.checkLoginAndEmail(companyDTO.getUsername(), companyDTO.getEmail())) {
            String activationCode = sendActivation(companyDTO);

            AppUser appUser = new AppUser();
            appUser.setUserType(UserType.COMPANY);
            appUser.setEmail(companyDTO.getEmail());
            appUser.setUsername(companyDTO.getUsername());
            appUser.setPassword(passwordEncoder.encode(companyDTO.getPassword()));
            AppUser user = userService.createUser(appUser);


            Company company = companyDTO.toEntity();
            company.setUser(user);
            company.setActivationCode(activationCode);
            Company createdCompany = companyRepository.save(company);
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setStatus("Success");
            responseDTO.setData("Activate your account via link send through your email: " + createdCompany.getUser().getEmail());
            return responseDTO;
        }
        throw new CommonException("Username or Email is already in use");
    }



    @Override
    public Company findCompanyByUser(AppUser user) {
        return companyRepository.findCompanyByUser(user);
    }

    @Override
    public ResponseDTO activateCompany(String code) {
        Company companyByActivationCode = companyRepository.findCompanyByActivationCode(code);
        companyByActivationCode.setStatus(Status.ACTIVE);
        Company company = companyRepository.save(companyByActivationCode);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus("Success");
        responseDTO.setData(company.getUser().getEmail());
        return responseDTO;
    }


    @Override
    public TokenDTO authenticate(LoginDTO loginDTO) throws CommonException {
        AppUser appUser = userService.authenticateUser(loginDTO);
        Company company = companyRepository.findCompanyByUser(appUser);
        if (company == null) {
            throw new CommonException("Company with this credentials not found");
        }
        if (company.getStatus().equals(Status.ACTIVE)) {
            return userService.getToken(loginDTO, appUser);
        }
        throw new CommonException("Company not activated");
    }

    @Override
    public ResponseDTO updateCompany(CompanyDataDTO company) throws CommonException {
        Company companyById = companyRepository.findCompanyById(company.getId());
        if (!(companyById.getUser().getUsername().equals(tokenProvider.getUsernameFromToken(SecurityUtils.getCredentials())))) {
            throw new CommonException("It is not your companyId");
        }

        companyById.setCompanyAddress(company.getCompanyAddress());
        companyById.setCompanyName(company.getCompanyName());
        companyById.setCompanyZipCode(company.getCompanyZipCode());
        Company saved = companyRepository.save(companyById);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatus("Success");
        responseDTO.setData(company);
        return responseDTO;
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findCompanyById(id);
    }

    private String sendActivation(CompanyDTO companyDTO) {
        String activationCode = UUID.randomUUID().toString();
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(companyDTO.getEmail());
        emailDetails.setMsgBody("To confirm your account, please click here: " + "http://localhost:8183/api/company/activate/" + activationCode);
        emailDetails.setSubject("Your activation code");
        try {
            emailService.sendSimpleMail(emailDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return activationCode;
    }
}
