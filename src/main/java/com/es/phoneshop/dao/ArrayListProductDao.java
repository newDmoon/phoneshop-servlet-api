package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.util.ProductComparator;
import com.es.phoneshop.model.product.util.SortField;
import com.es.phoneshop.model.product.util.SortOrder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ArrayListProductDao extends ArrayListGenericDao<Product> implements ProductDao {
    private static volatile ProductDao instance;
    private static long maxId = 0;

    private final Object lock = new Object();

    private ArrayListProductDao() {
        maxId = getList().size();
    }

    public static ProductDao getInstance() {
        if (instance == null) {
            synchronized (ProductDao.class) {
                if (instance == null) {
                    instance = new ArrayListProductDao();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        synchronized (lock) {
            return getList().stream()
                    .filter(product -> StringUtils.isEmpty(query) || product.getDescription().contains(query))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(ProductComparator.compare(getList(), sortField, sortOrder))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void delete(Long id) {
        synchronized (lock) {
            if (id != null) {
                getList().removeIf(product -> id.equals(product.getId()));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}