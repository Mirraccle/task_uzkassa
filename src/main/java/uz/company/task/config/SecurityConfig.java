package uz.company.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.company.task.security.jwt.JwtAuthenticationEntryPoint;
import uz.company.task.security.jwt.JwtFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter filter;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(JwtFilter filter, JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.filter = filter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable()
                .and()
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/api/company/sign-up",
                        "/api/company/login",
                        "/api/company/activate/**",
                        "/api/employee/login",
                        "/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }


}
