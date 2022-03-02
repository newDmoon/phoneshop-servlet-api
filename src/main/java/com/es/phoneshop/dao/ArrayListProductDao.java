package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.util.ProductComparator;
import com.es.phoneshop.model.product.util.ProductSearchFilter;
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
        maxId = getListItems().size();
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
            return getListItems().stream()
                    .filter(product -> StringUtils.isEmpty(query) || product.getDescription().contains(query))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(ProductComparator.compare(sortField, sortOrder))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Product> findProducts(ProductSearchFilter productSearchFilter) {
        synchronized (lock) {
            List<Product> resultList = getListItems();
            if(!StringUtils.isEmpty(productSearchFilter.getCode())){
                resultList = resultList.stream()
                        .filter(product -> product.getCode().contains(productSearchFilter.getCode()))
                        .collect(Collectors.toList());
            }
            if (productSearchFilter.getMinStock() >= 0) {
                resultList = resultList.stream()
                        .filter(product -> product.getStock() >= productSearchFilter.getMinStock())
                        .collect(Collectors.toList());
            }

            if (productSearchFilter.getMinPrice() != null) {
                resultList = resultList.stream()
                        .filter(product -> product.getPrice().compareTo(productSearchFilter.getMinPrice()) >= 0)
                        .collect(Collectors.toList());
            }
            if (productSearchFilter.getMaxPrice() != null) {
                resultList = resultList.stream()
                        .filter(product -> product.getPrice().compareTo(productSearchFilter.getMaxPrice()) <= 0)
                        .collect(Collectors.toList());
            }
            return resultList;
        }
    }

    @Override
    public void delete(Long id) {
        synchronized (lock) {
            if (id != null) {
                getListItems().removeIf(product -> id.equals(product.getId()));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}