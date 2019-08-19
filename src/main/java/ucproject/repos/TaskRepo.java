package ucproject.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ucproject.domain.Status;
import ucproject.domain.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task, Integer> {

    List<Task> findByStatus(Status status);
    List<Task> findByExecutorNull();
    List<Task> findAll();
    Optional<Task> findById(Integer id);
}
