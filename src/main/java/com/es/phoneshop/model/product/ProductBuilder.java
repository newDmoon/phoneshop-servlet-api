package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Currency;

public interface ProductBuilder {
    ProductBuilder setId(Long id);
    ProductBuilder setCode(String code);
    ProductBuilder setDescription(String description);
    ProductBuilder setPrice(BigDecimal price);
    ProductBuilder setCurrency(Currency currency);
    ProductBuilder setStock(int stock);
    ProductBuilder setImageUrl(String imageUrl);
    Product build();
}
