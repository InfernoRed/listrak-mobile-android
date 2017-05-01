package com.listrak.mobile;

import android.support.annotation.Nullable;

import com.listrak.mobile.interfaces.IListrakService;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Cart class
 * Created by Pam on 5/1/2017.
 */

public class Cart {
    static Map<String, CartItem> mItems = new HashMap<String, CartItem>();

    private Cart() { }

    public static Collection<CartItem> getItems() {
        return mItems.values();
    }

    public static void AddItem(String sku, int quantity, double price, String title) throws InstantiationException, UnsupportedEncodingException {
        AddItem(sku, quantity, price, title, null, null);
    }

    public static void AddItem(String sku, int quantity, double price, String title, @Nullable String imageUrl, @Nullable String linkUrl) throws InstantiationException, UnsupportedEncodingException {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("sku cannot be null or empty");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        CartItem item = new CartItem(sku, quantity, price, title, imageUrl, linkUrl);
        mItems.put(sku.toLowerCase(), item);

        updateCart();
    }

    private static void updateCart() throws InstantiationException, UnsupportedEncodingException {
        Config.resolve(IListrakService.class).updateCart(mItems.values());
    }
}