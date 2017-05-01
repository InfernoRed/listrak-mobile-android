package com.listrak.mobile;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pam on 5/1/2017.
 */
public class OrderTest extends BaseUnitTest {
    @Test
    public void initialize_startsEmpty() {
        Order order = new Order();
        assertEquals(0, order.getItems().size());
    }

    @Test
    public void addItem_withValidArgs_incrementsCollection() {
        Order order = new Order();
        order.addItem("sku", 1, 1.0);
        assertEquals(1, order.getItems().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addItem_withNullSku_throwsException() {
        Order order = new Order();
        order.addItem(null, 1, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addItem_withZeroQuantity_throwsException() {
        Order order = new Order();
        order.addItem("sku", 0, 1.0);
    }

    @Test
    public void setCustomer_withValidArgs_doesNotThrowException() {
        Order order = new Order();
        order.setCustomer("email", "first", "last");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCustomer_withNullEmail_throwsException() {
        Order order = new Order();
        order.setCustomer(null, "first", "last");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCustomer_withNullFirstName_throwsException() {
        Order order = new Order();
        order.setCustomer("email", null, "last");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCustomer_withNullLastName_throwsException() {
        Order order = new Order();
        order.setCustomer("email", "first", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCustomerFromSession_withoutSession_throwsException() {
        Order order = new Order();
        order.setCustomerFromSession();
    }

    @Test
    public void setCustomerFromSession_withSessionIdentityStarted_doesNotThrowException() throws InstantiationException {
        Session.startWithIdentity("email", "first", "last");
        Order order = new Order();
        order.setCustomerFromSession();
    }
}