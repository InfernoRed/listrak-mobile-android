package com.listrak.mobile;

import android.content.*;
import android.content.Context;

import static org.mockito.Mockito.*;

import com.listrak.mobile.interfaces.IContext;
import com.listrak.mobile.interfaces.IHttpService;
import com.listrak.mobile.interfaces.IRequestService;
import com.listrak.mobile.interfaces.IListrakService;

import org.junit.Before;
import org.mockito.ArgumentMatchers;

import java.io.UnsupportedEncodingException;

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
    protected IRequestService mMockRequestService;
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

    protected void setupMockHttpService(boolean responseIsSuccessful) {
        Config.getContainer().removeComponent(IHttpService.class);

        this.mMockHttpService = mock(IHttpService.class);
        when(this.mMockHttpService.getResponse(anyString())).thenReturn(responseIsSuccessful);
        Config.getContainer().addComponent(IHttpService.class, mMockHttpService);
    }

    protected void setupMockRequestService() throws UnsupportedEncodingException {
        Config.getContainer().removeComponent(IRequestService.class);

        this.mMockRequestService = mock(RequestService.class);
        when(this.mMockRequestService.getFormattedUrl(anyString(), anyString(),
                ArgumentMatchers.<String, String>anyMap(), any(Object.class))).thenCallRealMethod();
        Config.getContainer().addComponent(IRequestService.class, mMockRequestService);
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