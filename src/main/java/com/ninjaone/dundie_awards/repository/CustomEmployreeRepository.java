package com.ninjaone.dundie_awards.repository;

import java.util.Map;

public interface CustomEmployreeRepository {
    public int executeDynamicCommand(String jpql, Map<String, Object> params);
}
