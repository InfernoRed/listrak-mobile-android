package com.listrak.mobile;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Pam on 5/1/2017.
 */
public class OrderingTest extends BaseUnitTest {
    @Test
    public void createOrder_initializesEmptyOrder() {
        Order order = Ordering.createOrder();
        assertEquals(0, order.getItems().size());
    }

    @Test
    public void createOrderFromCart_withNoCartItems_initializesEmptyOrder() throws UnsupportedEncodingException, InstantiationException {
        Cart.clearItems();
        Order order = Ordering.createOrderFromCart();
        assertEquals(0, order.getItems().size());
    }

    @Test
    public void createOrderFromCart_withValidCartItem_incrementsCollectionSize() throws UnsupportedEncodingException, InstantiationException {
        Cart.clearItems();
        Cart.addItem("sku", 1, 1.0, "title");
        Order order = Ordering.createOrderFromCart();
        assertEquals(1, order.getItems().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void submitOrder_withNullOrder_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Ordering.submitOrder(null);
    }

    @Test
    public void submitOrder_withValidOrder_callsServiceSubmitOrder() throws UnsupportedEncodingException, InstantiationException {
        Order mockOrder = mock(Order.class);
        Ordering.submitOrder(mockOrder);
        verify(mMockListrackService).submitOrder(mockOrder);
    }

    @Test
    public void submitOrder_withValidOrder_callsServiceFinalizeCart() throws UnsupportedEncodingException, InstantiationException {
        Order mockOrder = mock(Order.class);
        Ordering.submitOrder(mockOrder);
        verify(mMockListrackService).finalizeCart(mockOrder.getEmailAddress(), mockOrder.getFirstName(),
                mockOrder.getLastName(), mockOrder.getOrderNumber());
    }

    @Test
    public void submitOrder_withValidOrder_resetsSession() throws UnsupportedEncodingException, InstantiationException {
        String sessionId = Session.getSessionId();
        Order mockOrder = mock(Order.class);
        Ordering.submitOrder(mockOrder);
        String newSessionId = Session.getSessionId();
        assertNotEquals(sessionId, newSessionId);
    }
}