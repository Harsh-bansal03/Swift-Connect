package com.scm.services;
import com.scm.entities.Activity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityService {


    List<Activity> getRecentActivities(String user);



    void logActivity(String description, String user);
}
