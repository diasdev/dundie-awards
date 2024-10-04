package com.ninjaone.dundie_awards.repository;

import java.util.List;
import java.util.Map;

public interface CustomEmployreeRepository<T> {
    public int executeDynamicCommand(String jpql, Map<String, Object> params);
}
