package com.ninjaone.dundie_awards.repository;

import java.util.List;

public class CustomEmployreeRepositoryImpl<T> implements CustomEmployreeRepository<T> {
    @Override
    public <S extends T> List<S> findAllByOrganizationId(long id) {
        return null;
    }
}
