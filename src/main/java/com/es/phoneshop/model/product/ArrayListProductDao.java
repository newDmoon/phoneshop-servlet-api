package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.util.ProductComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private final Object lock = new Object();
    private static volatile ProductDao instance;
    private static long maxId = 0;
    private List<Product> products;

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
        maxId = products.size();
    }

    public static ProductDao getInstance() {
        ProductDao localInstance = instance;
        if (localInstance == null) {
            synchronized (ProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ArrayListProductDao();
                }
            }
            instance = new ArrayListProductDao();
        }
        return instance;
    }


    @Override
    public Product getProduct(Long id) throws NoSuchElementException {
        synchronized (lock) {
            if (id != null) {
                return products.stream()
                        .filter(product -> id.equals(product.getId()))
                        .findAny()
                        .orElseThrow(NoSuchElementException::new);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        synchronized (lock) {
            return products.stream()
                    .filter(product -> query == null || query.isEmpty() || product.getDescription().contains(query))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(ProductComparator.compare(products, sortField, sortOrder))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void save(Product product) {
        synchronized (lock) {
            if (product != null) {
                if (product.getId() == null) {
                    product.setId(maxId++);
                    products.add(product);
                } else {
                    if (products.stream()
                            .anyMatch(productElem -> product.getId().equals(productElem.getId()))) {
                        products.set(products.indexOf(products.stream()
                                .filter(productElem -> product.getId().equals(productElem.getId()))
                                .findAny()
                                .get()), product);
                    } else {
                        products.add(product);
                    }
                }
            } else throw new IllegalArgumentException();
        }
    }

    @Override
    public void delete(Long id) {
        synchronized (lock) {
            if (id != null) {
                products.removeIf(product -> id.equals(product.getId()));
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

}
