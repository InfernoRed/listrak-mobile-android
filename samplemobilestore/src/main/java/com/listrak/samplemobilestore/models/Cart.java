package com.listrak.samplemobilestore.models;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cart model
 * Created by Pam on 4/25/2017.
 */

public class Cart {
    public interface ICartListener {
        void onCartChanged();
    }

    private Map<String, Product> mProductsMap = new HashMap<String, Product>();
    private List<ICartListener> mCartListeners = new ArrayList<>();

    private static Cart instance;

    private Cart(){}

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(mProductsMap.values());
    }

    public int getProductCount() {
        return mProductsMap.values().size();
    }

    public double getProductTotalAmount() {
        double total = 0;
        for (Product product : mProductsMap.values()) {
            total += product.amount;
        }
        return total;
    }

    public String getProductTotalFormatted() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(getProductTotalAmount());
    }

    public boolean containsProduct(Product product) {
        return mProductsMap.containsValue(product);
    }

    public void addProduct(Product product) {
        if (!containsProduct(product)) {
            mProductsMap.put(product.sku, product);
            notifyCartChanged();
            // TODO: invoke SDK's addItem
        }
    }

    public void removeProduct(Product product) {
        if (containsProduct(product)) {
            mProductsMap.remove(product.sku);
            notifyCartChanged();
            // TODO: invoke SDK's removeItem
        }
    }

    public void clearProducts() {
        mProductsMap.clear();
        notifyCartChanged();
        // TODO: invoke SDK's clearItems
    }

    public void notifyCartChanged() {
        for (ICartListener listener : mCartListeners) {
            listener.onCartChanged();
        }
    }

    public void addCartListener(ICartListener listener) {
        mCartListeners.add(listener);
    }

    public void removeCartListener(ICartListener listener) {
        mCartListeners.remove(listener);
    }
}
