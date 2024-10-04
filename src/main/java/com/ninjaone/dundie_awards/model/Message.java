package com.ninjaone.dundie_awards.model;

public record Message(Activity activity, RollbackCommand rollbackCommand) {
    public Message {
        if (activity == null ) {
            throw new IllegalArgumentException("Activity command must not be null");
        }
    }
}
