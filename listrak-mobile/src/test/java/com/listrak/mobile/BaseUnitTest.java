package com.listrak.mobile;

import android.content.*;
import android.content.Context;

import static org.mockito.Mockito.*;

import com.listrak.mobile.interfaces.IContext;
import com.listrak.mobile.interfaces.IHttpService;
import com.listrak.mobile.interfaces.IListrakService;

import org.junit.Before;

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
    protected final IListrakService mockListrackService;

    protected BaseUnitTest() {
        this.mockListrackService = mock(IListrakService.class);
        this.mockAndroidContext = getAndroidContext();
    }

    @Before
    public void initialize(){
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        Config.getContainer().removeComponent(IListrakService.class);
        Config.getContainer().addComponent(IListrakService.class, mockListrackService);
    }

    private Context getAndroidContext() {
        mMockSharedPreferences = mock(SharedPreferences.class);
        when(mMockSharedPreferences.getString(APP_ID_KEY, null)).thenReturn(APP_ID);

        android.content.Context mockContext = mock(Context.class);
        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mMockSharedPreferences);
        return mockContext;
    }
}