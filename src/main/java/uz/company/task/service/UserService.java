package uz.company.task.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.company.task.domain.AppUser;
import uz.company.task.domain.Company;
import uz.company.task.domain.enums.Status;
import uz.company.task.dto.LoginDTO;
import uz.company.task.dto.TokenDTO;
import uz.company.task.errors.CommonException;
import uz.company.task.repository.UserRepository;
import uz.company.task.security.JwtUserDetailsService;
import uz.company.task.security.jwt.TokenProvider;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }

//    public AppUser findByUsername(String username) {
//        AppUser foundUser = userRepository.findByUsername(username);
//        return foundUser;
//    }

    public AppUser createUser(AppUser appUser) {
        return userRepository.save(appUser);
    }

    public AppUser authenticateUser(LoginDTO loginDTO) throws CommonException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
        } catch (DisabledException e) {
            throw new CommonException("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new CommonException("INVALID_CREDENTIALS");
        }
        return userRepository.findByUsername(loginDTO.getUsername());
    }

    public TokenDTO getToken(LoginDTO loginDTO, AppUser byUsername) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
        final String jwtToken = tokenProvider.generateToken(userDetails, byUsername.getUserType(), byUsername.getEmail());
        return new TokenDTO(jwtToken);
    }

    public Boolean checkLoginAndEmail(String username, String email) {
        return userRepository.existsAppUserByUsernameOrEmail(username, email);
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public AppUser deleteUser(AppUser appUser) {
        userRepository.deleteById(appUser.getId());
        return appUser;
    }


}
