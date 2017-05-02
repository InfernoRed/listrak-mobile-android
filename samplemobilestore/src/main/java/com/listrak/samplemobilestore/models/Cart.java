package com.listrak.samplemobilestore.models;

import com.listrak.mobile.Order;
import com.listrak.mobile.Ordering;

import java.io.UnsupportedEncodingException;
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

            try {
                // LISTRAK SDK
                // let the sdk know we have added a new cart item
                //
                com.listrak.mobile.Cart.addItem(product.sku, 1, product.amount, product.name);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeProduct(Product product) {
        if (containsProduct(product)) {
            mProductsMap.remove(product.sku);
            notifyCartChanged();

            try {
                // LISTRAK SDK
                // let the sdk know we are removing a cart item
                //
                com.listrak.mobile.Cart.removeItem(product.sku);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearProducts() {
        mProductsMap.clear();
        notifyCartChanged();

        try {
            // LISTRAK SDK
            // have the sdk clear all cart items
            //
            com.listrak.mobile.Cart.clearItems();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public boolean processOrder(String email, String firstName, String lastName, String orderNum) {
        if (email == null || email.isEmpty() || firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() || orderNum == null || orderNum.isEmpty()) {
            return false;
        }
        try {
            // LISTRAK SDK
            // create an order from our cart and set necessary info
            // once order has been filled-out, submit it
            //
            Order order = Ordering.createOrderFromCart();
            order.setCustomer(email, firstName, lastName);
            order.setOrderNumber(orderNum);
            order.setOrderTotal(getProductTotalAmount());
            Ordering.submitOrder(order);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        clearProducts();
        return true;
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
