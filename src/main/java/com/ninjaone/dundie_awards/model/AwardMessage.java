package com.ninjaone.dundie_awards.model;

public record AwardMessage(Activity activity, RollbackCommand rollbackCommand) {
    public AwardMessage {
        if (activity == null ) {
            throw new IllegalArgumentException("Activity command must not be null");
        }
    }
}
