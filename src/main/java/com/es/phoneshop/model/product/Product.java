package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class Product {
    private Long id;
    private String code;
    private String description;
    /** null means there is no price because the product is outdated or new */
    private BigDecimal price;
    /** can be null if the price is null */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistoryItem> priceHistoryItemList;

    public Product() {
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl, List<PriceHistoryItem> priceHistoryItemList) {
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.priceHistoryItemList = priceHistoryItemList;
    }

    public List<PriceHistoryItem> getPriceHistoryItemList() {
        return priceHistoryItemList;
    }

    public void setPriceHistoryItemList(List<PriceHistoryItem> priceHistoryItemList) {
        this.priceHistoryItemList = priceHistoryItemList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return String.join(" ",
                id.toString(),
                code,
                description,
                price.toString(),
                currency.toString(),
                String.valueOf(stock),
                imageUrl);
    }
}