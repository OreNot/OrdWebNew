package ucproject.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ucproject.domain.*;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task, Integer> {

    List<Task> findByStatus(Status status);
    Integer countByExecutorAndStatusNot(User user, Status status);
    Integer countByWorkGroupAndStatusNot(WorkGroup workGroup, Status status);
    Integer countByWorkGroupAndStatus(WorkGroup workGroup, Status status);
    Integer countByWorkGroupAndUrgency(WorkGroup workGroup, Urgency urgency);
    Integer countByWorkGroupAndUrgencyAndStatusNot(WorkGroup workGroup, Urgency urgency, Status status);
    List<Task> findByStatusNameAndWorkGroupName(String status, String workgroup);
    List<Task> findByStatusName(String status);
    List<Task> findByUrgencyName(String urgency);
    List<Task> findByUrgencyNameAndWorkGroupName(String urgency, String workgroup);
    List<Task> findByExecutorNull();
    List<Task> findByExecutor(User user);
    List<Task> findByExecutorAndStatusNot(User user, Status status);
    List<Task> findAll();
    Optional<Task> findById(Integer id);
    List<Task> findByworkGroupName(String workGroup);
}
