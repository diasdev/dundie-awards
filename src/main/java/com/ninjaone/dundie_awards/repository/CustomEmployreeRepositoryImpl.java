package com.ninjaone.dundie_awards.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository
public class CustomEmployreeRepositoryImpl implements CustomEmployreeRepository {
    private final EntityManager entityManager;

    public CustomEmployreeRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public int executeDynamicCommand(String jpql, Map<String, Object> params) {
        Query query = entityManager.createQuery(jpql);

        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        return query.executeUpdate();
    }
}
