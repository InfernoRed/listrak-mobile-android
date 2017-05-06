package com.listrak.mobile;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Created by Pam on 5/2/2017.
 */
public class ListrakServiceTest extends BaseUnitTest {
    private ListrakService service;

    @Override
    public void initialize() throws InstantiationException, UnsupportedEncodingException {
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        setupMockRequestService();
        setupMockContext();
        setupSession(false);
        service = new ListrakService();
    }

    @Test
    public void trackProductBrowse_callsHttpServiceWithFormattedBaseUrl() throws UnsupportedEncodingException, InstantiationException {
        service.trackProductBrowse(new String[] {"test-sku1", "test-sku2"});
        String urlRequest = getCapturedUrlArgument();
        String formattedBaseUrl = "https://at1.listrakbi.com/activity/test-merchant-id?";
        assertTrue(urlRequest.startsWith(formattedBaseUrl));
    }

    @Test(expected = IllegalArgumentException.class)
    public void trackProductBrowse_withNullSkus_throwsException() throws UnsupportedEncodingException, InstantiationException {
        service.trackProductBrowse(null);
    }

    @Test
    public void captureCustomer_callsHttpServiceWithFormattedBaseUrl() throws UnsupportedEncodingException, InstantiationException {
        service.captureCustomer("test-email");
        String urlRequest = getCapturedUrlArgument();
        String formattedBaseUrl = "https://sca1.listrakbi.com/Handlers/Update.ashx";
        assertTrue(urlRequest.startsWith(formattedBaseUrl));
    }

    @Test(expected = IllegalArgumentException.class)
    public void captureCustomer_withNullEmail_throwsException() throws UnsupportedEncodingException, InstantiationException {
        service.captureCustomer(null);
    }

    @Test
    public void clearCart_callsHttpServiceWithFormattedBaseUrl() throws UnsupportedEncodingException, InstantiationException {
        service.clearCart();
        String urlRequest = getCapturedUrlArgument();
        String formattedBaseUrl = "https://sca1.listrakbi.com/Handlers/Set.ashx";
        assertTrue(urlRequest.startsWith(formattedBaseUrl));
    }

    @Test
    public void updateCart_callsHttpServiceWithFormattedBaseUrl() throws UnsupportedEncodingException, InstantiationException {
        service.updateCart(new ArrayList<CartItem>() {{new CartItem("sku", 1, 1, "title", null, null);}});
        String urlRequest = getCapturedUrlArgument();
        String formattedBaseUrl = "https://sca1.listrakbi.com/Handlers/Set.ashx";
        assertTrue(urlRequest.startsWith(formattedBaseUrl));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateCart_withNullCartItems_throwsException() throws UnsupportedEncodingException, InstantiationException {
        service.updateCart(null);
    }

    @Test
    public void submitOrder_callsHttpServiceWithFormattedBaseUrl() throws UnsupportedEncodingException, InstantiationException {
        Order order = new Order();
        order.setCustomer("email", null, null);
        order.setOrderNumber("order-number");
        service.submitOrder(order);
        String urlRequest = getCapturedUrlArgument();
        String formattedBaseUrl = "https://s1.listrakbi.com/t/T.ashx";
        assertTrue(urlRequest.startsWith(formattedBaseUrl));
    }

    @Test(expected = IllegalArgumentException.class)
    public void submitOrder_withNullOrder_throwsException() throws UnsupportedEncodingException, InstantiationException {
        service.submitOrder(null);
    }

    @Test
    public void finalizeCart_callsHttpServiceWithFormattedBaseUrl() throws UnsupportedEncodingException, InstantiationException {
        service.finalizeCart("order-number", "email", null, null);
        String urlRequest = getCapturedUrlArgument();
        String formattedBaseUrl = "https://sca1.listrakbi.com/Handlers/Set.ashx";
        assertTrue(urlRequest.startsWith(formattedBaseUrl));
    }

    @Test(expected = IllegalArgumentException.class)
    public void finalizeCart_withNullOrderNumber_throwsException() throws UnsupportedEncodingException, InstantiationException {
        service.finalizeCart(null, "email", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void finalizeCart_withNullEmail_throwsException() throws UnsupportedEncodingException, InstantiationException {
        service.finalizeCart("order-number", null, null, null);
    }

    @Test
    public void subscribeCustomer_callsHttpServiceWithFormattedBaseUrl() throws UnsupportedEncodingException, InstantiationException {
        service.subscribeCustomer("subscriber-code", "email", new HashMap<String, String>());
        String urlRequest = getCapturedUrlArgument();
        String formattedBaseUrl = "https://s1.listrakbi.com/t/S.ashx";
        assertTrue(urlRequest.startsWith(formattedBaseUrl));
    }

    @Test(expected = IllegalArgumentException.class)
    public void subscribeCustomer_withNullOrderNumber_throwsException() throws UnsupportedEncodingException, InstantiationException {
        service.subscribeCustomer(null, "email", new HashMap<String, String>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void subscribeCustomer_withNullEmail_throwsException() throws UnsupportedEncodingException, InstantiationException {
        service.subscribeCustomer("subscriber-code", null, new HashMap<String, String>());
    }

    private String getCapturedUrlArgument() {
        ArgumentCaptor<String> urlArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(this.mMockRequestService).enqueueRequest(urlArgCaptor.capture());
        return urlArgCaptor.getValue();
    }
}