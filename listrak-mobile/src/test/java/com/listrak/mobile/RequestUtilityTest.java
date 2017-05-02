package com.listrak.mobile;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Pam on 5/1/2017.
 */
public class RequestUtilityTest extends BaseUnitTest {
    @Test
    public void getFormattedUrl_withContextForHttp_returnsHttpUrl() throws UnsupportedEncodingException, InstantiationException {
        setupMockContext();
        when(mMockContext.getUseHttps()).thenReturn(false);
        String formattedUrl = RequestUtility.getFormattedUrl("host", "path", null);
        assertEquals("http://host/path", formattedUrl);
    }

    @Test
    public void getFormattedUrl_withContextForHttps_returnsHttpsUrl() throws UnsupportedEncodingException, InstantiationException {
        setupMockContext();
        String formattedUrl = RequestUtility.getFormattedUrl("host", "path", null);
        assertEquals("https://host/path", formattedUrl);
    }

    @Test
    public void getFormattedUrl_withArgs_updatesUrlWithArgs() throws Exception {
        setupMockContext();
        String formattedUrl = RequestUtility.getFormattedUrl("host", "path/with/arg/%s", null, "test");
        assertEquals("https://host/path/with/arg/test", formattedUrl);
    }

    @Test
    public void getFormattedUrl_withAdditionalParams_updatesUrlWithQueryStrings() throws Exception {
        setupMockContext();
        Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("key", "value");
        additionalParams.put("key1", "value1");
        String formattedUrl = RequestUtility.getFormattedUrl("host", "path", additionalParams);
        assertTrue(formattedUrl.contains("key=value"));
        assertTrue(formattedUrl.contains("key1=value1"));
    }

    @Test
    public void getFormattedUrl_withAdditionalSpecialCharParam_updatesUrlWithEscapedQueryStrings() throws Exception {
        setupMockContext();
        Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("key", "value with space");
        String formattedUrl = RequestUtility.getFormattedUrl("host", "path", additionalParams);
        assertTrue(formattedUrl.contains("key=value+with+space"));
    }
}