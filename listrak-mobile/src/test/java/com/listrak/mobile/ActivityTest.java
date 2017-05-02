package com.listrak.mobile;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.mockito.Mockito.verify;

/**
 * Created by Pam on 5/1/2017.
 */
public class ActivityTest extends BaseUnitTest {
    @Test
    public void trackProductBrowse_withValidSku_callsService() throws Exception {
        String sku = "test sku";
        Activity.trackProductBrowse(sku);
        verify(mMockListrackService).trackProductBrowse(new String[]{sku});
    }

    @Test(expected = IllegalArgumentException.class)
    public void trackProductBrowse_withNullSku_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Activity.trackProductBrowse(null);
    }
}