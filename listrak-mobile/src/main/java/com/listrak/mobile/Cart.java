package com.listrak.mobile;

import android.content.res.Resources;
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
    static Map<String, CartItem> mItems = new HashMap<>();

    private Cart() { }

    /**
     * Returns the current collection of CartItems
     * @return
     */
    public static Collection<CartItem> getItems() {
        return mItems.values();
    }

    /**
     * Adds a CartItem to the cart given the sku, quantity, price, and title
     * @param sku
     * @param quantity
     * @param price
     * @param title
     * @throws InstantiationException
     * @throws UnsupportedEncodingException
     */
    public static void addItem(String sku, int quantity, double price, String title) throws InstantiationException, UnsupportedEncodingException {
        addItem(sku, quantity, price, title, null, null);
    }

    /**
     * Adds a CartItemto the cart given the sku, quantity, price, title, imageUrl, and linkUrl
     * @param sku
     * @param quantity
     * @param price
     * @param title
     * @param imageUrl
     * @param linkUrl
     * @throws InstantiationException
     * @throws UnsupportedEncodingException
     */
    public static void addItem(String sku, int quantity, double price, String title, @Nullable String imageUrl, @Nullable String linkUrl) throws InstantiationException, UnsupportedEncodingException {
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

    /**
     * Updates the quantity in the cart for the given sku
     * @param sku
     * @param quantity
     */
    public static void updateItemQuantity(String sku, int quantity) throws UnsupportedEncodingException, InstantiationException {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("sku cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        if (!hasItem(sku)) {
            throw new IndexOutOfBoundsException("cart does not contain the specified sku");
        }
        CartItem item = getItem(sku);
        item.quantity = quantity;
        updateCart();
    }

    /**
     * Removes the cart item with the given sku
     * @param sku
     * @throws UnsupportedEncodingException
     * @throws InstantiationException
     */
    public static void removeItem(String sku) throws UnsupportedEncodingException, InstantiationException {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("sku cannot be null or empty");
        }
        if (!hasItem(sku)) {
            throw new IndexOutOfBoundsException("cart does not contain the specified sku");
        }
        mItems.remove(sku);
        updateCart();
    }

    /**
     * Clears the cart items
     */
    public static void clearItems() throws UnsupportedEncodingException, InstantiationException {
        mItems.clear();
        Config.resolve(IListrakService.class).clearCart();
    }

    /**
     * Returns whether the given SKU exists in the cart
     * @param sku
     * @return
     */
    public static boolean hasItem(String sku) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("sku cannot be null or empty");
        }
        return mItems.containsKey(sku);
    }

    /**
     * Returns the cart item for the given sku
     * @param sku
     * @return
     */
    public static CartItem getItem(String sku) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("sku cannot be null or empty");
        }
        if (!hasItem(sku)) {
            return null;
        }
        return mItems.get(sku);
    }

    private static void updateCart() throws InstantiationException, UnsupportedEncodingException {
        Config.resolve(IListrakService.class).updateCart(mItems.values());
    }
}