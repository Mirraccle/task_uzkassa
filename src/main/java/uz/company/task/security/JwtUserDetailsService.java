package uz.company.task.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.company.task.domain.AppUser;
import uz.company.task.repository.UserRepository;
import uz.company.task.service.UserService;

import java.util.ArrayList;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());

    }
}
