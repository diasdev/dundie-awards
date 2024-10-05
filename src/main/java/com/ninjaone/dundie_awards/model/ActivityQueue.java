package com.ninjaone.dundie_awards.model;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ActivityQueue {

    private final Queue<AwardMessage> activityQueue = new ConcurrentLinkedQueue<>();

    public void addMessage(AwardMessage event) {
        activityQueue.add(event);
    }

    public AwardMessage getNextMessage() {
        return activityQueue.poll();
    }

    public int getQueueSize() {
        return activityQueue.size();
    }

    public List<Activity> getQueue() {
        return activityQueue.stream().map(AwardMessage::activity).toList();
    }
}
