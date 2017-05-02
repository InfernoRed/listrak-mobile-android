package com.listrak.mobile.interfaces;

import android.support.annotation.Nullable;

import com.listrak.mobile.CartItem;
import com.listrak.mobile.Order;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

/**
 * Sends data to Listrak
 * Created by Pam on 4/28/2017.
 */

public interface IListrakService {
    /**
     * Sends the array of skus that have been browsed to the Listrak api
     * @param sku
     */
    void trackProductBrowse(String[] sku) throws InstantiationException, UnsupportedEncodingException;

    /**
     * Sends current customer's identity and session to the Listrak api
     * @param email
     */
    void captureCustomer(String email) throws InstantiationException, UnsupportedEncodingException;

    /**
     * Sends clear cart information the Listrak api
     */
    void clearCart() throws InstantiationException, UnsupportedEncodingException;

    /**
     * Sends the updated cart items to the Listrak api
     * @param cartItems
     * @throws InstantiationException
     * @throws UnsupportedEncodingException
     */
    void updateCart(Collection<CartItem> cartItems) throws InstantiationException, UnsupportedEncodingException;

    /**
     * Sends information that the order has been submitted to the Listrak api
     * @param order
     */
    void submitOrder(Order order) throws InstantiationException, UnsupportedEncodingException;

    /**
     * Sends information that the cart has been finalized to the Listrak api
     * @param orderNumber
     * @param email
     * @param firstName
     * @param lastName
     */
    void finalizeCart(String orderNumber, String email, @Nullable String firstName, @Nullable String lastName) throws InstantiationException, UnsupportedEncodingException;

    /**
     * Sends the customer and suscriber information to the Listrak api
     * @param subscriberCode
     * @param email
     * @param meta
     */
    void subscribeCustomer(String subscriberCode, String email, Map<String, String> meta) throws InstantiationException, UnsupportedEncodingException;
}
