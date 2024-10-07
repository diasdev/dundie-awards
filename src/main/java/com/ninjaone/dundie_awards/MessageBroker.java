package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.ActivityQueue;
import com.ninjaone.dundie_awards.model.AwardMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageBroker {
    private static final Logger logger = LoggerFactory.getLogger(MessageBroker.class);

    private final ActivityQueue activityQueue;

    public MessageBroker(ActivityQueue activityQueue) {
        this.activityQueue = activityQueue;
    }

    public void sendMessage(AwardMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        activityQueue.addMessage(message);
        logger.info("Message sent");
    }

    public AwardMessage getMessage() {
        return activityQueue.getNextMessage();
    }

    public List<Activity> getMessages(){
        return activityQueue.getQueue();
    }
}
