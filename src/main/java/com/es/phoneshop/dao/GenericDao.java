package com.es.phoneshop.dao;

import com.es.phoneshop.model.Entity;

public interface GenericDao<T extends Entity> {
    T getElementById(Long id);

    void saveItem(T item);
}
