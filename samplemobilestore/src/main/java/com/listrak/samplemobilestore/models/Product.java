package com.listrak.samplemobilestore.models;

/**
 * Product model for items that can be purchased in the app.
 * Created by Pam on 4/24/2017.
 */

public class Product {
    public final String sku;
    public final String name;
    public final String description;
    public final Double amount;

    public Product(String sku, String name, String description, Double amount) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return name;
    }
}
