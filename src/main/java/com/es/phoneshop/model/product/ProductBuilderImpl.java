package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Currency;

public class ProductBuilderImpl implements ProductBuilder {
    Product product = new Product();

    @Override
    public ProductBuilder setId(Long id) {
        product.setId(id);
        return this;
    }

    @Override
    public ProductBuilder setCode(String code) {
        product.setCode(code);
        return this;
    }

    @Override
    public ProductBuilder setDescription(String description) {
        product.setDescription(description);
        return this;
    }

    @Override
    public ProductBuilder setPrice(BigDecimal price) {
        product.setPrice(price);
        return this;
    }

    @Override
    public ProductBuilder setCurrency(Currency currency) {
        product.setCurrency(currency);
        return this;
    }

    @Override
    public ProductBuilder setStock(int stock) {
        product.setStock(stock);
        return this;
    }

    @Override
    public ProductBuilder setImageUrl(String imageUrl) {
        product.setImageUrl(imageUrl);
        return this;
    }

    @Override
    public Product build() {
        return product;
    }
}
