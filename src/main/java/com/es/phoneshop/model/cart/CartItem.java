package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.join(StringUtils.SPACE, product.toString(), String.valueOf(quantity));
    }
}
