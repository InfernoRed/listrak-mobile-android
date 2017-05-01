package com.listrak.mobile;

import com.listrak.mobile.interfaces.IListrakService;

/**
 * Ordering class
 * Created by Pam on 5/1/2017.
 */

public class Ordering {
    private Ordering() { }

    public static Order createOrder() {
        return new Order();
    }
    
    public static Order createOrderFromCart() {
        Order order = createOrder();

        for (CartItem item : Cart.getItems()) {
            order.addItem(item.sku, item.quantity, item.amount);
        }

        return order;
    }

    public static void submitOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order cannot be null");
        }

        IListrakService svc = Config.resolve(IListrakService.class);

        // submit the order
        svc.submitOrder(order);

        // prevent SCA emails
        svc.finalizeCart(order.getEmailAddress(), order.getFirstName(), order.getLastName(), order.getOrderNumber());

        // reset session
        Session.reset();
    }
}
