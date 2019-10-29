package ucproject.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ucproject.domain.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByFio(String fio);
    User findById(int id);
    List<User> findAll();


}
