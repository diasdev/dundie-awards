package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.AwardMessage;
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
    private final AwardEventService awardEventService;

    public ActivityService(ActivityRepository activityRepository, MessageBroker messageBroker, AwardEventService awardEventService) {
        this.activityRepository = activityRepository;
        this.messageBroker = messageBroker;
        this.awardEventService = awardEventService;
    }

    @Async
    @Scheduled(fixedRate = 3_000L)
    public void pollQueue() {
        AwardMessage message = messageBroker.getMessage();
        if (message != null) {
            processMessage(message);
        }
    }

    private void processMessage(AwardMessage message) {
        try {
            activityRepository.save(message.activity());
            logger.info("Activity processed");
        } catch (Exception e) {
            logger.error("Exception occurred. Rollback of original operation will be perform.", e);
            awardEventService.publishRollbackEvent(e.getMessage(), message.rollbackData());
        }
    }
}
