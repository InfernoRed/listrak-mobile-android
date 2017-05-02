package com.listrak.mobile;

import com.listrak.mobile.interfaces.IListrakService;

import java.io.UnsupportedEncodingException;

/**
 * Ordering class
 * Created by Pam on 5/1/2017.
 */

public class Ordering {
    private Ordering() { }

    /**
     * Creates an empty order object
     * @return
     */
    public static Order createOrder() {
        return new Order();
    }

    /**
     * Creates a new order object with items from the Cart
     * @return
     */
    public static Order createOrderFromCart() {
        Order order = createOrder();

        for (CartItem item : Cart.getItems()) {
            order.addItem(item.sku, item.quantity, item.amount);
        }

        return order;
    }

    /**
     * Submits the order to the listrak service and restart the session
     * @param order
     */
    public static void submitOrder(Order order) throws UnsupportedEncodingException, InstantiationException {
        if (order == null) {
            throw new IllegalArgumentException("order cannot be null");
        }

        IListrakService svc = Config.resolve(IListrakService.class);

        // submit the order
        svc.submitOrder(order);

        // prevent SCA emails
        svc.finalizeCart(order.getOrderNumber(), order.getEmailAddress(), order.getFirstName(), order.getLastName());

        // reset session
        Session.reset();
    }
}
