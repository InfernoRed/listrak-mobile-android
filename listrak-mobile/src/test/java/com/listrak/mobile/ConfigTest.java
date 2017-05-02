package com.listrak.mobile;

import android.content.SharedPreferences;

import com.listrak.mobile.interfaces.IContext;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Pam on 5/2/2017.
 */
public class ConfigTest extends BaseUnitTest {

    @Override
    public void initialize() {
        // don't initialize with Config from Base class
    }

    @Test(expected = IllegalArgumentException.class)
    public void initializeFromBuilder_withNullContext_throwsException() {
        new Config.Builder(null, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initializeFromBuilder_withNullTemplate_throwsException() {
        new Config.Builder(this.mockAndroidContext, null, CLIENT_MERCHANT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initializeFromBuilder_withNullMerchantId_throwsException() {
        new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, null);
    }

    @Test
    public void initializeFromBuilder_setUseHttpsToFalse_returnsFalseFromConfig() {
        Config.Builder builder = new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID);
        builder.setUseHttps(false);
        Config.initialize(builder.build());
        assertFalse(Config.getUseHttps());
    }

    @Test
    public void initializeFromBuilder_setClientTemplateIdToTestValue_returnsTestValueFromConfig() {
        String testValue = "test new value";
        Config.Builder builder = new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID);
        builder.setClientTemplateId(testValue);
        Config.initialize(builder.build());
        assertEquals(testValue, Config.getClientTemplateId());
    }

    @Test
    public void initializeFromBuilder_setClientMerchantIdToTestValue_returnsTestValueFromConfig() {
        String testValue = "test new value";
        Config.Builder builder = new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID);
        builder.setClientMerchantId(testValue);
        Config.initialize(builder.build());
        assertEquals(testValue, Config.getClientMerchantId());
    }

    @Test
    public void initializeFromBuilder_setHostOverrideToTestValue_returnsTestValueFromConfig() {
        String testValue = "test new value";
        Config.Builder builder = new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID);
        builder.setHostOverride(testValue);
        Config.initialize(builder.build());
        assertEquals(testValue, Config.getHostOverride());
    }

    @Test
    public void initialize_withNullAppIdInSharedPreference_savesNewUidToSharedPreferences() {
        when(mMockSharedPreferences.getString(APP_ID_KEY, null)).thenReturn(null);

        SharedPreferences.Editor mockEditor = mock(SharedPreferences.Editor.class);
        when(mMockSharedPreferences.edit()).thenReturn(mockEditor);
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        verify(mMockSharedPreferences).edit();
    }

    @Test
    public void contextAppId_matchesConfigGlobalSessionId() {
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        IContext context = new Context();
        assertEquals(Config.getAppId(), context.getGlobalSessionId());
    }

    @Test
    public void contextVisitId_matchesConfigVisit() {
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        IContext context = new Context();
        assertEquals(Config.getVisitId(), context.getVisitId());
    }

    @Test
    public void contextClientTemplateId_matchesConfigTemplateId() {
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        IContext context = new Context();
        assertEquals(Config.getClientTemplateId(), context.getTemplateId());
    }

    @Test
    public void contextClientMerchantId_matchesConfigMerchantId() {
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        IContext context = new Context();
        assertEquals(Config.getClientMerchantId(), context.getMerchantId());
    }

    @Test
    public void contextHostOverride_matchesConfigHostOverride() {
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        IContext context = new Context();
        assertEquals(Config.getHostOverride(), context.getHostOverride());
    }

    @Test
    public void contextUseHttps_matchesConfigUseHttps() {
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        IContext context = new Context();
        assertEquals(Config.getUseHttps(), context.getUseHttps());
    }
}