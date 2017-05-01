package com.listrak.mobile;

import com.listrak.mobile.interfaces.IContext;
import com.listrak.mobile.interfaces.IListrakService;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * IListrakService implementation that sends data to Listrak
 * Created by Pam on 4/28/2017.
 */

class ListrakService implements IListrakService {
    @Override
    public void trackProductBrowse(String[] sku) {

    }

    @Override
    public void captureCustomer(String email) {

    }

    @Override
    public void clearCart() {

    }

    @Override
    public void updateCart(Collection<CartItem> cartItems) throws InstantiationException, UnsupportedEncodingException {
        if (cartItems == null) {
            throw new NullPointerException("cartItems cannot be null");
        }

        Config.resolve(IContext.class).validate();

        IContext context = Config.resolve(IContext.class);
        Map<String, String> params = new HashMap<>();
        params.put("_tid", context.getTemplateId());
        params.put("_uid", context.generateUid());
        params.put("gsid", context.getGlobalSessionId());
        params.put("_sid", context.getSessionId());

        int index = 0;
        for (CartItem cartItem : cartItems) {
            params.put("s_" + index, cartItem.sku);
            params.put("q_" + index, String.valueOf(cartItem.quantity));
            params.put("p_" + index, String.valueOf(cartItem.amount));
            params.put("n_" + index, cartItem.title);
            params.put("iu_" + index, cartItem.imageUrl);
            params.put("lu_" + index, cartItem.linkUrl);
            index++;
        }

        String url = RequestUtility.getFormattedUrl("sca1.listrakbi.com", "/Handlers/Set.ashx", params);

        RequestUtility.sendRequest(url);
    }

    @Override
    public void submitOrder(Order order) {

    }

    @Override
    public void finalizeCart(String email, String firstName, String lastName, String orderNumber) {

    }
}
