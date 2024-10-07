package com.ninjaone.dundie_awards.model;

public record AwardsEventMessage(Activity activity, AwardsRollbackData rollbackData) {
    public AwardsEventMessage {
        if (activity == null || rollbackData == null) {
            throw new IllegalArgumentException("Activity and rollbackData command must not be null");
        }
    }
}
