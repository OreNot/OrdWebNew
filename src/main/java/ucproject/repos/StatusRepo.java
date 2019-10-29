package ucproject.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ucproject.domain.Status;

public interface StatusRepo extends JpaRepository<Status, Integer> {
    Status findByName(String name);
}
