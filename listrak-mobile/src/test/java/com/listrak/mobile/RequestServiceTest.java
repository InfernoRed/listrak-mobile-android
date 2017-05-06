package com.listrak.mobile;

import android.os.AsyncTask;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Pam on 5/1/2017.
 */
public class RequestServiceTest extends BaseUnitTest {
    private RequestService service;
    private RequestService.EnqueueRequestTask mMockEnqueueTask;

    @Override
    public void initialize() throws InstantiationException, UnsupportedEncodingException {
        Config.initialize(new Config.Builder(this.mockAndroidContext, CLIENT_TEMPLATE_ID, CLIENT_MERCHANT_ID).build());
        setupMockHttpService(true);
        setupMockContext();

        mMockEnqueueTask = mock(RequestService.EnqueueRequestTask.class);
        Mockito.doAnswer(new Answer<AsyncTask<Void, Void, Void>>() {
            @Override
            public AsyncTask<Void, Void, Void> answer(InvocationOnMock invocation) throws Throwable {
                mMockEnqueueTask.doInBackground();
                return null;
            }
        }).when(mMockEnqueueTask).execute();
        when(mMockEnqueueTask.doInBackground()).thenCallRealMethod();

        service = new RequestService();
    }

    @Test
    public void getFormattedUrl_withContextForHttp_returnsHttpUrl() throws UnsupportedEncodingException, InstantiationException {
        when(mMockContext.getUseHttps()).thenReturn(false);
        String formattedUrl = service.getFormattedUrl("host", "path", null);
        assertEquals("http://host/path", formattedUrl);
    }

    @Test
    public void getFormattedUrl_withContextForHttps_returnsHttpsUrl() throws UnsupportedEncodingException, InstantiationException {
        String formattedUrl = service.getFormattedUrl("host", "path", null);
        assertEquals("https://host/path", formattedUrl);
    }

    @Test
    public void getFormattedUrl_withArgs_updatesUrlWithArgs() throws Exception {
        String formattedUrl = service.getFormattedUrl("host", "path/with/arg/%s", null, "test");
        assertEquals("https://host/path/with/arg/test", formattedUrl);
    }

    @Test
    public void getFormattedUrl_withAdditionalParams_updatesUrlWithQueryStrings() throws Exception {
        Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("key", "value");
        additionalParams.put("key1", "value1");
        String formattedUrl = service.getFormattedUrl("host", "path", additionalParams);
        assertTrue(formattedUrl.contains("key=value"));
        assertTrue(formattedUrl.contains("key1=value1"));
    }

    @Test
    public void getFormattedUrl_withAdditionalSpecialCharParam_updatesUrlWithEscapedQueryStrings() throws Exception {
        Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("key", "value with space");
        String formattedUrl = service.getFormattedUrl("host", "path", additionalParams);
        assertTrue(formattedUrl.contains("key=value+with+space"));
    }

    @Test
    public void enqueueRequest_withSuccessfulHttpService_callsGetResponseOnce() {
        service.enqueueRequest("test-url", mMockEnqueueTask);
        verify(this.mMockHttpService).getResponse("test-url");
    }

    @Test
    public void enqueueRequest_withFailureThenSuccessfulHttpService_callsGetResponseTwice() {
        String url = "test-url-1";
        when(this.mMockHttpService.getResponse(url)).thenReturn(false).thenReturn(true);
        service.enqueueRequest(url, mMockEnqueueTask);
        verify(this.mMockHttpService, times(2)).getResponse(url);
    }

}