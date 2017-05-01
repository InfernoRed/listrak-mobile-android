package com.listrak.mobile;

import static org.mockito.Mockito.*;

import com.listrak.mobile.interfaces.IContext;
import com.listrak.mobile.interfaces.IHttpService;
import com.listrak.mobile.interfaces.IListrakService;

import org.junit.Before;

/**
 * Base Test class for all tests to inherit
 */
public abstract class BaseUnitTest {
    public static String CLIENT_TEMPLATE_ID = "test clientTemplateId";
    public static String CLIENT_MERCHANT_ID = "test clientMerchantId";

    protected final IListrakService mockListrackService;

    protected BaseUnitTest() {
        this.mockListrackService = mock(IListrakService.class);
    }

    @Before
    public void initialize(){
        Config.initialize(new Config.Builder(CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        Config.getContainer().removeComponent(IListrakService.class);
        Config.getContainer().addComponent(IListrakService.class, mockListrackService);

        Config.getContainer().removeComponent(IContext.class);
        Config.getContainer().removeComponent(IHttpService.class);
    }
}