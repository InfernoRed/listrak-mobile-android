package com.listrak.mobile;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.io.UnsupportedEncodingException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by Pam on 5/6/2017.
 */

public class SessionTest extends BaseUnitTest {

    @Test(expected = IllegalArgumentException.class)
    public void startWithIdentity_withNullEmail_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Session.startWithIdentity(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setIdentity_withNullEmail_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Session.setIdentity(null, null, null);
    }

    @Test(expected = InstantiationException.class)
    public void setIdentity_notStarted_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Session.endSession();
        Session.setIdentity("email", null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void subscribe_withNullSubscriberCode_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Session.subscribe(null, null);
    }

    @Test(expected = InstantiationException.class)
    public void subscribe_notStarted_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Session.endSession();
        Session.subscribe("code", null);
    }

    @Test(expected = InstantiationException.class)
    public void subscribe_noIdentity_throwsException() throws UnsupportedEncodingException, InstantiationException {
        Session.endSession();
        Session.start();
        Session.subscribe("code", null);
    }

    @Test
    public void subscribe_withValidArgsAndSession_callsService() throws UnsupportedEncodingException, InstantiationException {
        Session.startWithIdentity("test-email", null, null);
        Session.subscribe("code", null);
        verify(this.mMockListrackService).subscribeCustomer(anyString(), anyString(), ArgumentMatchers.<String, String>anyMap());
    }

    @Test(expected = InstantiationException.class)
    public void ensureStarted_notStarted_throwsException() throws InstantiationException {
        Session.endSession();
        Session.ensureStarted();
    }

}
