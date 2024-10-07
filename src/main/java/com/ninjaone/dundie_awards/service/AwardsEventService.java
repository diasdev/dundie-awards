package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.AwardsEventMessage;
import com.ninjaone.dundie_awards.model.AwardsRollbackData;
import com.ninjaone.dundie_awards.model.AwardsRollbackEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class AwardsEventService {
    private static final Logger logger = LoggerFactory.getLogger(AwardsEventService.class);

    private final MessageBroker messageBroker;
    private final AwardsRollbackService awardsRollbackService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AwardsEventService(MessageBroker messageBroker, AwardsRollbackService awardsRollbackService, ApplicationEventPublisher applicationEventPublisher) {
        this.messageBroker = messageBroker;
        this.awardsRollbackService = awardsRollbackService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void sendEvent(Activity activity, long orgId, int numberOfAwards) {
        messageBroker.sendMessage(buildMessage(activity, orgId, numberOfAwards));
    }

    public AwardsEventMessage buildMessage(Activity activity, long orgId, int numberOfAwards) {
        return new AwardsEventMessage(activity, awardsRollbackService.buildRollbackData(orgId, numberOfAwards));
    }

    public void publishRollbackEvent(String errorMessage, AwardsRollbackData rollbackData) {
        applicationEventPublisher.publishEvent(new AwardsRollbackEvent(rollbackData, errorMessage));

        logger.info("Rollback Message published");
    }
}
