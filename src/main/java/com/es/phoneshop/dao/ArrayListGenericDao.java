package com.es.phoneshop.dao;

import com.es.phoneshop.model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class ArrayListGenericDao<T extends Entity> implements GenericDao<T>{
    private static Long maxId = 0L;
    private final Object lock = new Object();

    private List<T> list;

    public ArrayListGenericDao() {
        this.list = new ArrayList<>();
    }

    public T getById(Long id) throws NoSuchElementException {
        synchronized (lock) {
            if (id != null) {
                return list.stream()
                        .filter(listItem -> id.equals(listItem.getId()))
                        .findAny()
                        .orElseThrow(NoSuchElementException::new);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void save(T item) {
        synchronized (lock) {
            if (item != null) {
                if (item.getId() == null) {
                    item.setId(maxId++);
                    list.add(item);
                } else {
                    if (list.stream()
                            .anyMatch(productElem -> item.getId().equals(productElem.getId()))) {
                        list.set(list.indexOf(list.stream()
                                .filter(productElem -> item.getId().equals(productElem.getId()))
                                .findAny()
                                .get()), item);
                    } else {
                        list.add(item);
                    }
                }
            } else throw new IllegalArgumentException();
        }
    }
}
