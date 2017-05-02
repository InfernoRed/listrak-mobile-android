package com.listrak.mobile;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Order class
 * Created by Pam on 4/28/2017.
 */

public class Order {
    private Map<String, Item> mItems;
    private String mEmailAddress;
    private String mFirstName;
    private String mLastName;
    private String mCurrency;
    private String mOrderNumber;
    private double mOrderTotal;
    private double mTaxTotal;
    private double mShippingTotal;
    private double mHandlingTotal;

    public Order() {
        mItems = new HashMap<>();
    }

    public Collection<Item> getItems() {
        return mItems.values();
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public String getOrderNumber() {
        return mOrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        mOrderNumber = orderNumber;
    }

    public double getOrderTotal() {
        return mOrderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        mOrderTotal = orderTotal;
    }

    public double getTaxTotal() {
        return mTaxTotal;
    }

    public void setTaxTotal(double taxTotal) {
        mTaxTotal = taxTotal;
    }

    public double getShippingTotal() {
        return mShippingTotal;
    }

    public void setShippingTotal(double shippingTotal) {
        mShippingTotal = shippingTotal;
    }

    public double getHandlingTotal() {
        return mHandlingTotal;
    }

    public void setHandlingTotal(double handlingTotal) {
        mHandlingTotal = handlingTotal;
    }

    /**
     * Adds item to the current order
     * @param sku
     * @param quantity
     * @param price
     */
    public void addItem(String sku, int quantity, double price)
    {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("sku cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }

        Item item = new Item(sku, quantity, price);
        mItems.put(sku.toLowerCase(), item);
    }

    /**
     * Sets the customer's info for the given email and name
     * @param emailAddress
     * @param firstName
     * @param lastName
     */
    public void setCustomer(String emailAddress, @Nullable String firstName, @Nullable String lastName)
    {
        if (emailAddress == null || emailAddress.isEmpty()) {
            throw new IllegalArgumentException("emailAddress cannot be null or empty");
        }

        mEmailAddress = emailAddress;
        mFirstName = firstName;
        mLastName = lastName;
    }

    /**
     * Sets the customer's info from the current configured session
     */
    public void setCustomerFromSession()
    {
        setCustomer(Session.getEmailAddress(), Session.getFirstName(), Session.getLastName());
    }
}
