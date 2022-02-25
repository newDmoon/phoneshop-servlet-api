package com.es.phoneshop.dao;

import com.es.phoneshop.model.Entity;

public interface GenericDao<T extends Entity> {
    T getById(Long id);

    void save(T item);
}
