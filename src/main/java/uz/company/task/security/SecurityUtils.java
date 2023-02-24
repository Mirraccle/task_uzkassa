package uz.company.task.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getCredentials() {

        return String.valueOf(SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials());
    }
}
