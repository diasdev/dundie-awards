package com.ninjaone.dundie_awards.model;

public record AwardMessage(Activity activity, AwardsRollbackData rollbackData) {
    public AwardMessage {
        if (activity == null || rollbackData == null) {
            throw new IllegalArgumentException("Activity and rollbackData command must not be null");
        }
    }
}
