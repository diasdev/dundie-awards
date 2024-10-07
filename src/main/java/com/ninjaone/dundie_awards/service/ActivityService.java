package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.AwardsEventMessage;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;
    private final MessageBroker messageBroker;
    private final AwardsEventService awardsEventService;

    public ActivityService(ActivityRepository activityRepository, MessageBroker messageBroker, AwardsEventService awardsEventService) {
        this.activityRepository = activityRepository;
        this.messageBroker = messageBroker;
        this.awardsEventService = awardsEventService;
    }

    @Async
    @Scheduled(fixedRate = 3_000L)
    public void pollQueue() {
        AwardsEventMessage message = messageBroker.getMessage();
        if (message != null) {
            processMessage(message);
        }
    }

    private void processMessage(AwardsEventMessage message) {
        try {
            activityRepository.save(message.activity());
            logger.info("Activity processed");
        } catch (Exception e) {
            logger.error("Exception occurred. Rollback of original operation will be perform.", e);
            awardsEventService.publishRollbackEvent(e.getMessage(), message.rollbackData());
        }
    }
}
