package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private final Object lock = new Object();
    private final Currency USD = Currency.getInstance("USD");
    private static long maxId = 0;
    private List<Product> products;

    public ArrayListProductDao() {
        this.products = new ArrayList<>();
        saveSampleProducts();
        maxId = products.size();
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
    public List<Product> findProducts() {
        synchronized (lock) {
            return products.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void save(Product product) throws NoSuchElementException {
        synchronized (lock) {
            if (product != null) {
                if (product.getId() == null) {
                    product.setId(maxId++);
                    products.add(product);
                } else {
                    if(products.stream()
                            .anyMatch(productElem -> product.getId().equals(productElem.getId()))){
                        products.set(products.indexOf(products.stream()
                                .filter(productElem -> product.getId().equals(productElem.getId()))
                                .findAny()
                                .get()), product);
                    }
                    else {
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

    private void saveSampleProducts() {
        save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), USD, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), USD, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product("iphone", "Apple iPhone", new BigDecimal(200), USD, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), USD, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), USD, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product("palmp", "Palm Pixi", new BigDecimal(170), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product("simc56", "Siemens C56", new BigDecimal(70), USD, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product("simc61", "Siemens C61", new BigDecimal(80), USD, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), USD, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }
}
