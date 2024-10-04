package com.ninjaone.dundie_awards.model;

import java.util.Map;

public record RollbackCommand(String command, Map<String, Object> params) {
    public RollbackCommand {
        if (command == null) {
            throw new IllegalArgumentException("Command must not be null");
        }
    }
}
