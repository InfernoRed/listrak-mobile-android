package com.listrak.mobile;

/**
 * Item class
 * Created by Pam on 5/1/2017.
 */

public class Item {
    public final String sku;
    public final int quantity;
    public final double amount;

    protected Item(String sku, int quantity, double amount) {
        this.sku = sku;
        this.quantity = quantity;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return sku;
    }
}
