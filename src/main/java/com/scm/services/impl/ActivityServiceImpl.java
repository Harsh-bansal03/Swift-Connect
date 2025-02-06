package com.scm.services.impl;

import com.scm.entities.Activity;
import com.scm.repsitories.ActivityRepository;
import com.scm.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public List<Activity> getRecentActivities(String user) {
        return activityRepository.findTop5ByUserOrderByTimestampDesc(user); // You might want to sort by timestamp or limit results
    }





    @Override
    public void logActivity(String description, String user)  {
        Activity activity = new Activity();
        activity.setDescription(description);
        activity.setTimestamp(new Date());
        activity.setUser(user);

        activityRepository.save(activity);
    }
}
