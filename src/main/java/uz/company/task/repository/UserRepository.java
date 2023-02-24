package uz.company.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.company.task.domain.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);

    Boolean existsAppUserByUsernameOrEmail(String username, String email);
}
