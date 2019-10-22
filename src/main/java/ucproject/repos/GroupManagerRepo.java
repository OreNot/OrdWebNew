package ucproject.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ucproject.domain.GroupManager;
import ucproject.domain.User;

import java.util.List;


public interface GroupManagerRepo extends JpaRepository<GroupManager, Integer> {
    List<GroupManager> findAll();
    GroupManager findByUser(User user);
    List<GroupManager> findByWorkGroupId(int id);
}
