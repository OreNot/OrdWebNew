package ucproject.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ucproject.domain.Urgency;
import ucproject.domain.WorkGroup;

import java.util.List;

public interface WorkGroupRepo extends JpaRepository<WorkGroup, Integer> {
    WorkGroup findByName(String name);
    List<WorkGroup> findAll();
}
