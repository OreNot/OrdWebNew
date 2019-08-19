package ucproject.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ucproject.domain.User;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String username);


}
