package com.listrak.mobile.interfaces;

import com.listrak.mobile.CartItem;
import com.listrak.mobile.Order;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * Sends data to Listrak
 * Created by Pam on 4/28/2017.
 */

public interface IListrakService {
    void trackProductBrowse(String[] sku);
    void captureCustomer(String email);
    void clearCart();
    void updateCart(Collection<CartItem> cartItems) throws InstantiationException, UnsupportedEncodingException;
    void submitOrder(Order order);
    void finalizeCart(String email, String firstName, String lastName, String orderNumber);
}
