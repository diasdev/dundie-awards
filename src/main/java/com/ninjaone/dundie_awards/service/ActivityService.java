package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ActivityService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;
    private final MessageBroker messageBroker;

    public ActivityService(ActivityRepository activityRepository, MessageBroker messageBroker) {
        this.activityRepository = activityRepository;
        this.messageBroker = messageBroker;
    }

    @Async
    @Scheduled(fixedRate = 5_000L)
    public void pollQueue() {
        Activity message = messageBroker.getMessage();
        if (message != null) {
            processMessage(message);
        }
        System.out.println(message);
    }

    private void processMessage(Activity message) {
        activityRepository.save(message);
        logger.info("Message processed: " + message);
    }

    private void rollBackMessage(Activity message) {
        String updateQuery = "UPDATE Employees e SET e.dundie_awards = e.dundie_awards - 1 WHERE e.organization_id = :orgId";
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", 1);
        logger.info("Message rolled back: " + message);
    }
}
