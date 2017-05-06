package com.listrak.mobile;

import android.support.annotation.Nullable;
import android.support.v4.os.IResultReceiver;

import com.listrak.mobile.interfaces.IContext;
import com.listrak.mobile.interfaces.IRequestService;
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
        verifyArgument(skus, "skus");

        IContext context = getAndValidateContext();

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

        final String trackProductBrowseHost = "at1.listrakbi.com";
        final String trackProductBrowsePath = "/activity/%s";
        sendFormattedRequest(trackProductBrowseHost, trackProductBrowsePath, params, context.getMerchantId());
    }

    @Override
    public void captureCustomer(String email) throws InstantiationException, UnsupportedEncodingException {
        verifyArgument(email, "email");

        IContext context = getAndValidateContext();

        Map<String, String> params = new HashMap<>();
        params.put("_tid", context.getTemplateId());
        params.put("_uid", context.generateUid());
        params.put("gsid", context.getGlobalSessionId());
        params.put("_sid", context.getSessionId());
        params.put("_t", String.valueOf(context.getUnixTimestamp()));
        params.put("email", email);

        final String captureCustomerHost = "sca1.listrakbi.com";
        final String captureCustomerPath = "/Handlers/Update.ashx";
        sendFormattedRequest(captureCustomerHost, captureCustomerPath, params);
    }

    @Override
    public void clearCart() throws InstantiationException, UnsupportedEncodingException {
        IContext context = getAndValidateContext();

        Map<String, String> params = new HashMap<>();
        params.put("_tid", context.getTemplateId());
        params.put("_uid", context.generateUid());
        params.put("gsid", context.getGlobalSessionId());
        params.put("_sid", context.getSessionId());
        params.put("cc", "true");

        final String clearCartHost = "sca1.listrakbi.com";
        final String clearCartPath = "/Handlers/Set.ashx";
        sendFormattedRequest(clearCartHost, clearCartPath, params);
    }

    @Override
    public void updateCart(Collection<CartItem> cartItems) throws InstantiationException, UnsupportedEncodingException {
        verifyArgument(cartItems, "cartItems");

        IContext context = getAndValidateContext();

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

        final String updateCartHost = "sca1.listrakbi.com";
        final String updateCartPath = "/Handlers/Set.ashx";
        sendFormattedRequest(updateCartHost, updateCartPath, params);
    }

    @Override
    public void submitOrder(Order order) throws InstantiationException, UnsupportedEncodingException {
        verifyArgument(order, "order");

        IContext context = getAndValidateContext();

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

        final String submitOrderHost = "s1.listrakbi.com";
        final String submitOrderPath = "/t/T.ashx";
        sendFormattedRequest(submitOrderHost, submitOrderPath, params);
    }

    @Override
    public void finalizeCart(String orderNumber, String email, @Nullable String firstName, @Nullable String lastName) throws InstantiationException, UnsupportedEncodingException {
        verifyArgument(orderNumber, "orderNumber");
        verifyArgument(email, "email");

        IContext context = getAndValidateContext();

        Map<String, String> params = new HashMap<>();
        params.put("_tid", context.getTemplateId());
        params.put("_uid", context.generateUid());
        params.put("gsid", context.getGlobalSessionId());
        params.put("_sid", context.getSessionId());
        params.put("on", orderNumber);
        params.put("e", email);
        if (!(firstName == null || firstName.isEmpty())) params.put("fn", firstName);
        if (!(lastName == null || lastName.isEmpty())) params.put("ln", lastName);

        final String finalizeCartHost = "sca1.listrakbi.com";
        final String finalizeCartPath = "/Handlers/Set.ashx";
        sendFormattedRequest(finalizeCartHost, finalizeCartPath, params);
    }

    @Override
    public void subscribeCustomer(String subscriberCode, String email, Map<String, String> meta) throws InstantiationException, UnsupportedEncodingException {
        verifyArgument(subscriberCode, "subscriberCode");
        verifyArgument(email, "email");

        IContext context = getAndValidateContext();

        Map<String, String> params = new HashMap<>();
        params.put("ctid", context.getMerchantId());
        params.put("uid", context.generateUid());

        params.put("_t_0", "s");
        params.put("e_0", email);
        params.put("l_0", subscriberCode);

        for (Map.Entry<String, String> kv : meta.entrySet()) {
            params.put(kv.getKey() + "_0", kv.getValue());
        }

        final String subscribeCustomerHost = "s1.listrakbi.com";
        final String subscribeCustomerPath = "/t/S.ashx";
        sendFormattedRequest(subscribeCustomerHost, subscribeCustomerPath, params);
    }

    private void verifyArgument(Object arg, String argName) {
        if (arg == null) {
            throw new IllegalArgumentException(argName + " cannot be null");
        }
    }

    private void verifyArgument(String arg, String argName) {
        if (arg == null || arg.isEmpty()) {
            throw new IllegalArgumentException(argName + " cannot be null or empty");
        }
    }

    private IContext getAndValidateContext() throws InstantiationException {
        IContext context = Config.resolve(IContext.class);
        context.validate();
        return context;
    }

    private void sendFormattedRequest(String host, String path,
                                      @Nullable Map<String, String> additionalParams, Object... args)
            throws UnsupportedEncodingException {
        IRequestService requestService = Config.resolve(IRequestService.class);
        String url = requestService.getFormattedUrl(host, path, additionalParams, args);
        requestService.enqueueRequest(url);
    }
}
