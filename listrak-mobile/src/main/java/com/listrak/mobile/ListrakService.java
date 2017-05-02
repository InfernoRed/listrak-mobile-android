package com.listrak.mobile;

import android.support.annotation.Nullable;

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
    public void trackProductBrowse(String[] skus) throws InstantiationException, UnsupportedEncodingException {
        if (skus == null || skus.length != 0) {
            throw new IllegalArgumentException("skus cannot be null or empty");
        }

        IContext context = Config.resolve(IContext.class);
        context.validate();

        Map<String, String> params = new HashMap<>();
        params.put("vuid", context.getVisitId());
        params.put("uid", context.generateUid());
        params.put("gsid", context.getGlobalSessionId());
        params.put("sid", context.getSessionId());

        int index = 0;
        for (String sku : skus) {
            params.put("_t_" + index, "at");
            params.put("t_" + index, "ProductBrowse");
            params.put("k_" + index, sku);
            index++;
        }

        String url = RequestUtility.getFormattedUrl("at1.listrakbi.com", "/activity/{0}", params, context.getMerchantId());

        RequestUtility.sendRequest(url);
    }

    @Override
    public void captureCustomer(String email) throws InstantiationException, UnsupportedEncodingException {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        IContext context = Config.resolve(IContext.class);
        context.validate();

        Map<String, String> params = new HashMap<>();
        params.put("_tid", context.getTemplateId());
        params.put("_uid", context.generateUid());
        params.put("gsid", context.getGlobalSessionId());
        params.put("_sid", context.getSessionId());
        params.put("_t", String.valueOf(context.getUnixTimestamp()));
        params.put("email", email);

        String url = RequestUtility.getFormattedUrl("sca1.listrakbi.com", "/Handlers/Update.ashx", params);

        RequestUtility.sendRequest(url);
    }

    @Override
    public void clearCart() throws InstantiationException, UnsupportedEncodingException {
        IContext context = Config.resolve(IContext.class);
        context.validate();

        Map<String, String> params = new HashMap<>();
        params.put("_tid", context.getTemplateId());
        params.put("_uid", context.generateUid());
        params.put("gsid", context.getGlobalSessionId());
        params.put("_sid", context.getSessionId());
        params.put("cc", "true");

        String url = RequestUtility.getFormattedUrl("sca1.listrakbi.com", "/Handlers/Set.ashx", params);

        RequestUtility.sendRequest(url);
    }

    @Override
    public void updateCart(Collection<CartItem> cartItems) throws InstantiationException, UnsupportedEncodingException {
        if (cartItems == null) {
            throw new IllegalArgumentException("cartItems cannot be null");
        }

        IContext context = Config.resolve(IContext.class);
        context.validate();

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
    public void submitOrder(Order order) throws InstantiationException, UnsupportedEncodingException {
        if (order == null) {
            throw new IllegalArgumentException("order cannot be null");
        }

        IContext context = Config.resolve(IContext.class);
        context.validate();

        Map<String, String> params = new HashMap<>();
        params.put("ctid", context.getMerchantId());
        params.put("uid", context.generateUid());
        params.put("gsid", context.getGlobalSessionId());

        params.put("on", order.getOrderNumber());
        params.put("_t_0", "o");
        params.put("ot_0", String.valueOf(order.getOrderTotal()));
        params.put("tt_0",  String.valueOf(order.getTaxTotal()));
        params.put("st_0",  String.valueOf(order.getShippingTotal()));
        params.put("ht_0",  String.valueOf(order.getHandlingTotal()));
        params.put("it_0",  String.valueOf(order.getItems().size()));
        params.put("_t_1", "tt");
        params.put("e_1", "t");
        params.put("_t_2", "c");
        params.put("e_2", order.getEmailAddress());
        if (!(order.getFirstName() == null || order.getFirstName().isEmpty())) params.put("fn_2", order.getFirstName());
        if (!(order.getLastName() == null || order.getLastName().isEmpty())) params.put("ln_2", order.getLastName());
        params.put("_t_3", "i");

        String url = RequestUtility.getFormattedUrl("s1.listrakbi.com", "/t/T.ashx", params);

        RequestUtility.sendRequest(url);
    }

    @Override
    public void finalizeCart(String orderNumber, String email, @Nullable String firstName, @Nullable String lastName) throws InstantiationException, UnsupportedEncodingException {
        if (orderNumber == null || orderNumber.isEmpty()) {
            throw new IllegalArgumentException("orderNumber cannot be null or empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        IContext context = Config.resolve(IContext.class);
        context.validate();

        Map<String, String> params = new HashMap<>();
        params.put("_tid", context.getTemplateId());
        params.put("_uid", context.generateUid());
        params.put("gsid", context.getGlobalSessionId());
        params.put("_sid", context.getSessionId());
        params.put("on", orderNumber);
        params.put("e", email);
        if (!(firstName == null || firstName.isEmpty())) params.put("fn", firstName);
        if (!(lastName == null || lastName.isEmpty())) params.put("ln", lastName);

        String url = RequestUtility.getFormattedUrl("sca1.listrakbi.com", "/Handlers/Set.ashx", params);

        RequestUtility.sendRequest(url);
    }

    @Override
    public void subscribeCustomer(String subscriberCode, String email, Map<String, String> meta) throws InstantiationException, UnsupportedEncodingException {
        if (subscriberCode == null || subscriberCode.isEmpty()) {
            throw new IllegalArgumentException("subscriberCode cannot be null or empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        IContext context = Config.resolve(IContext.class);
        context.validate();

        Map<String, String> params = new HashMap<>();
        params.put("ctid", context.getMerchantId());
        params.put("uid", context.generateUid());

        params.put("_t_0", "s");
        params.put("e_0", email);
        params.put("l_0", subscriberCode);

        for (Map.Entry<String, String> kv : meta.entrySet()) {
            params.put(kv.getKey() + "_0", kv.getValue());
        }

        String url = RequestUtility.getFormattedUrl("s1.listrakbi.com", "/t/S.ashx", params);

        RequestUtility.sendRequest(url);
    }
}
