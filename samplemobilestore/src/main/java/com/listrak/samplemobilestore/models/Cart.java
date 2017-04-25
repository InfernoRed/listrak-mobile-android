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

    private static Map<String, Product> mProductsMap = new HashMap<String, Product>();
    private static List<ICartListener> mCartListeners = new ArrayList<>();

    public static List<Product> getProducts() {
        return new ArrayList<>(mProductsMap.values());
    }

    public static int getProductCount() {
        return mProductsMap.values().size();
    }

    public static double getProductTotalAmount() {
        double total = 0;
        for (Product product : mProductsMap.values()) {
            total += product.amount;
        }
        return total;
    }

    public static String getProductTotalFormatted() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(getProductTotalAmount());
    }

    public static boolean containsProduct(Product product) {
        return mProductsMap.containsValue(product);
    }

    public static void addProduct(Product product) {
        if (!containsProduct(product)) {
            mProductsMap.put(product.sku, product);
            notifyCartChanged();
            // TODO: invoke SDK's addItem
        }
    }

    public static void removeProduct(Product product) {
        if (containsProduct(product)) {
            mProductsMap.remove(product.sku);
            notifyCartChanged();
            // TODO: invoke SDK's removeItem
        }
    }

    public static void clearProducts() {
        mProductsMap.clear();
        notifyCartChanged();
        // TODO: invoke SDK's clearItems
    }

    public static void notifyCartChanged() {
        for (ICartListener listener : mCartListeners) {
            listener.onCartChanged();
        }
    }

    public static void addCartListener(ICartListener listener) {
        mCartListeners.add(listener);
    }

    public static void removeCartListener(ICartListener listener) {
        mCartListeners.remove(listener);
    }
}
