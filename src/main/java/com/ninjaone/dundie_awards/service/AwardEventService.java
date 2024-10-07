package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.AwardsRollbackData;
import com.ninjaone.dundie_awards.model.AwardsRollbackEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AwardEventService {
    private static final Logger logger = LoggerFactory.getLogger(AwardEventService.class);

    private final MessageBroker messageBroker;
    private final AwardRollbackService awardRollbackService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AwardEventService(MessageBroker messageBroker, AwardRollbackService awardRollbackService, ApplicationEventPublisher applicationEventPublisher) {
        this.messageBroker = messageBroker;
        this.awardRollbackService = awardRollbackService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void sendEvent(Activity activity, long orgId, int numberOfAwards) {
        messageBroker.sendMessage(awardRollbackService.buildMessage(activity, orgId, numberOfAwards));
    }

    public void publishRollbackEvent(String errorMessage, AwardsRollbackData rollbackData) {
        applicationEventPublisher.publishEvent(new AwardsRollbackEvent(rollbackData, errorMessage));

        logger.info("Rollback Message published");
    }
}
