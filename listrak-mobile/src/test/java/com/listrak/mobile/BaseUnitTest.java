package com.listrak.mobile;

import android.content.*;
import android.content.Context;

import static org.mockito.Mockito.*;

import com.listrak.mobile.interfaces.IContext;
import com.listrak.mobile.interfaces.IHttpService;
import com.listrak.mobile.interfaces.IListrakService;

import org.junit.Before;

import java.io.UnsupportedEncodingException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Base Test class for all tests to inherit
 */
public abstract class BaseUnitTest {
    final String APP_ID_KEY = "LISTRAK-APP-ID";
    public static String APP_ID = "test-app-id";
    public static String CLIENT_TEMPLATE_ID = "test clientTemplateId";
    public static String CLIENT_MERCHANT_ID = "test clientMerchantId";

    protected final Context mockAndroidContext;
    protected SharedPreferences mMockSharedPreferences;
    protected IListrakService mMockListrackService;
    protected IContext mMockContext;
    protected IHttpService mMockHttpService;

    protected BaseUnitTest() {
        this.mockAndroidContext = getAndroidContext();
    }

    @Before
    public void initialize() throws InstantiationException, UnsupportedEncodingException {
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        setupMockListrackService();
    }

    private Context getAndroidContext() {
        mMockSharedPreferences = mock(SharedPreferences.class);
        when(mMockSharedPreferences.getString(APP_ID_KEY, null)).thenReturn(APP_ID);

        android.content.Context mockContext = mock(Context.class);
        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mMockSharedPreferences);
        return mockContext;
    }

    protected void setupMockListrackService() {
        Config.getContainer().removeComponent(IListrakService.class);
        this.mMockListrackService = mock(IListrakService.class);
        Config.getContainer().addComponent(IListrakService.class, mMockListrackService);
    }

    protected void setupMockHttpService(boolean withSuccess) {
        Config.getContainer().removeComponent(IHttpService.class);

        this.mMockHttpService = mock(IHttpService.class);
//        Response mockResponse = new Response.Builder()
//                                    .request(new Request.Builder())
//                                    .code(withSuccess ? 200 : 400)
//                                    .build();
//        when(mockResponse.isSuccessful()).thenReturn(withSuccess);
        //when(mMockHttpService.getResponse(any(Request.class))).thenReturn(any(Response.class));
        Config.getContainer().addComponent(IHttpService.class, mMockHttpService);
    }

    protected void setupMockContext() throws InstantiationException {
        Config.getContainer().removeComponent(IContext.class);

        this.mMockContext = mock(IContext.class);
        when(mMockContext.getGlobalSessionId()).thenReturn("test-global-session-id");
        when(mMockContext.getSessionId()).thenReturn("test-session-id");
        when(mMockContext.getVisitId()).thenReturn("test-visit-id");
        when(mMockContext.getTemplateId()).thenReturn("test-template-id");
        when(mMockContext.getMerchantId()).thenReturn("test-merchant-id");
        when(mMockContext.getUseHttps()).thenReturn(true);
        when(mMockContext.getUnixTimestamp()).thenReturn((long) 111111);
        when(mMockContext.generateUid()).thenReturn("test-generated-mock-uid");
        Config.getContainer().addComponent(IContext.class, mMockContext);
    }

    protected void setupSession(boolean withIdentity) throws InstantiationException, UnsupportedEncodingException {
        if (withIdentity) {
            Session.startWithIdentity("test-email", "test-first", "test-last");
        } else {
            Session.start();
        }
    }
}