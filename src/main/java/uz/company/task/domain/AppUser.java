package uz.company.task.domain;

import lombok.Data;
import uz.company.task.domain.enums.UserType;

import javax.persistence.*;

@Data
@Entity(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "user_type")
    private UserType userType;
}
