package eminimki.com.JTS.DataAccess;

import eminimki.com.JTS.Entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findBySenderId(int senderId);
    List<Task> findByReceiverId(int receiverId);


    @Query("SELECT status, COUNT(*) AS count FROM Task WHERE sender.username = ?1 GROUP BY status")
    List<Object[]> getTaskStatisticsForSender(String sender);

    @Query("SELECT status, COUNT(*) AS count FROM Task WHERE receiver.username = ?1 GROUP BY status")
    List<Object[]> getTaskStatisticsForReceiver(String receiver);
}
