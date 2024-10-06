package com.ninjaone.dundie_awards.model;

public record AwardsRollbackEvent(AwardsRollbackData rollbackData, String errorMessage) {
    public AwardsRollbackEvent {
        if (rollbackData == null) {
            throw new IllegalArgumentException("rollbackData must not be null");
        }
    }
}
