package ucproject.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ucproject.domain.Status;
import ucproject.domain.Urgency;

import java.util.List;

public interface UrgencyRepo extends JpaRepository<Urgency, Integer> {
    Urgency findByName(String name);
    List<Urgency> findAll();
}
