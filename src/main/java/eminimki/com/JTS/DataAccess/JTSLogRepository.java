package eminimki.com.JTS.DataAccess;

import eminimki.com.JTS.Entities.JTSLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JTSLogRepository extends JpaRepository<JTSLog, Integer> {
    List<JTSLog> findByActioner(String actioner);
    List<JTSLog> findByActionerContainingOrDescriptionContaining(String actioner, String description);

}
