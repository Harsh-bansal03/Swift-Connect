package com.scm.repsitories;
import com.scm.entities.Activity;
import com.scm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    // Custom query methods can be defined here if needed


//    @Query("SELECT i FROM Activity i ORDER BY i.timestamp DESC")
    List<Activity> findTop5ByOrderByTimestampDesc();


    List<Activity> findByUser(User user);

    List<Activity> findTop5ByUserOrderByTimestampDesc(String user);
}
