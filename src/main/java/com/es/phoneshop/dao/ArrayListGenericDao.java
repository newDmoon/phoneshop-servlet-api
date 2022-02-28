package com.es.phoneshop.dao;

import com.es.phoneshop.model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class ArrayListGenericDao<T extends Entity> implements GenericDao<T> {
    private static Long maxId = 0L;

    private final Object lock = new Object();

    private List<T> listItems;

    public ArrayListGenericDao() {
        this.listItems = new ArrayList<>();
    }

    public T getElementById(Long id) throws NoSuchElementException {
        synchronized (lock) {
            if (id != null) {
                return listItems.stream()
                        .filter(listItem -> id.equals(listItem.getId()))
                        .findAny()
                        .orElseThrow(NoSuchElementException::new);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<T> getListItems() {
        return listItems;
    }

    public void saveItem(T item) {
        synchronized (lock) {
            if (item != null) {
                if (item.getId() == null) {
                    item.setId(maxId++);
                    listItems.add(item);
                } else {
                    if (listItems.stream()
                            .anyMatch(productElem -> item.getId().equals(productElem.getId()))) {
                        listItems.set(listItems.indexOf(listItems.stream()
                                .filter(productElem -> item.getId().equals(productElem.getId()))
                                .findAny()
                                .get()), item);
                    } else {
                        listItems.add(item);
                    }
                }
            } else throw new IllegalArgumentException();
        }
    }
}