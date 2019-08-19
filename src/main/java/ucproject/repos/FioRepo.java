package ucproject.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import ucproject.domain.Fio;

import java.util.List;

public interface FioRepo extends JpaRepository<Fio, Integer> {
    Fio findByFio(String fio);
    Fio findById(String id);
    Fio findByFioIgnoreCase(String fio);
    Fio findByFioContaining(String fio);
    // Fio findByFioContainingIgnoreCase(String fio);
    List<Fio> findByFioContainingIgnoreCase(String fio);

}