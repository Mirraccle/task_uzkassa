package uz.company.task.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.company.task.dto.*;
import uz.company.task.errors.CommonException;
import uz.company.task.security.JwtUserDetailsService;
import uz.company.task.security.jwt.TokenProvider;
import uz.company.task.service.CompanyService;
import uz.company.task.service.EmailService;
import uz.company.task.service.UserService;

@Slf4j
@RestController
@RequestMapping("api/company")
public class CompanyController {

    private final CompanyService companyService;

    private final AuthenticationManager authenticationManager;

    private final JwtUserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final EmailService emailService;

    public CompanyController(CompanyService companyService, AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService, TokenProvider tokenProvider,
                             PasswordEncoder passwordEncoder, UserService userService, EmailService emailService) {
        this.companyService = companyService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDTO> createCompany(@RequestBody CompanyDTO companyDTO) throws CommonException {
        return ResponseEntity.ok(companyService.createCompany(companyDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> createToken(@RequestBody LoginDTO loginDTO) throws Exception {
        return ResponseEntity.ok(companyService.authenticate(loginDTO));
    }



    @GetMapping("/activate/{code}")
    public ResponseEntity<ResponseDTO> activateCompany(@PathVariable String code) {
        ResponseDTO responseDTO = companyService.activateCompany(code);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/edit")
    public ResponseEntity<ResponseDTO> updateCompany(@RequestBody CompanyDataDTO company) throws CommonException {
        return ResponseEntity.ok(companyService.updateCompany(company));
    }
}
