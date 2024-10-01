package com.ninjaone.dundie_awards.repository;

import java.util.List;

public interface CustomEmployreeRepository<T> {
    <S extends T> List<S> findAllByOrganizationId(long id);
}
