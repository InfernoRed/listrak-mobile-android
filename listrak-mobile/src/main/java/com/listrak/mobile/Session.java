package com.listrak.mobile;

import android.support.annotation.Nullable;

import com.listrak.mobile.interfaces.IListrakService;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Session class responsible for the user's identity
 * Created by Pam on 5/1/2017.
 */

public class Session {
    private static Session sInstance;

    private boolean mIsStarted;
    private boolean mHasIdentity;
    private String mSessionId;
    private String mFirstName;
    private String mLastName;
    private String mEmailAddress;

    private Session() {
        // private constructor for singleton instance
    }

    /**
     * Gets the current singleton instance of the Session
     * @return
     */
    public static Session getInstance() {
        if (sInstance == null) {
            sInstance = new Session();
        }
        return sInstance;
    }

    /**
     * Starts the session without an identity
     * @throws InstantiationException
     */
    public static void start() throws InstantiationException {
        Config.ensureInitialized();

        Session session = getInstance();

        session.mIsStarted = true;
        session.mHasIdentity = false;
        session.mSessionId = generateSessionId();

        session.mEmailAddress = "";
        session.mFirstName = "";
        session.mLastName = "";
    }

    /**
     * Starts the session with the given identity
     * @param emailAddress
     * @param firstName
     * @param lastName
     * @throws InstantiationException
     * @throws IllegalArgumentException
     */
    public static void startWithIdentity(String emailAddress, @Nullable String firstName, @Nullable String lastName) throws InstantiationException, IllegalArgumentException, UnsupportedEncodingException {
        Config.ensureInitialized();
        if (emailAddress == null || emailAddress.isEmpty()) {
            throw new IllegalArgumentException("emailAddress cannot be null or empty");
        }

        start();
        setIdentity(emailAddress, firstName, lastName);
    }

    /**
     * Updates the session with the given identity
     * @param emailAddress
     * @param firstName
     * @param lastName
     * @throws InstantiationException
     * @throws IllegalArgumentException
     */
    public static void setIdentity(String emailAddress, @Nullable String firstName, @Nullable String lastName) throws InstantiationException, IllegalArgumentException, UnsupportedEncodingException {
        if (emailAddress == null || emailAddress.isEmpty()) {
            throw new IllegalArgumentException("emailAddress cannot be null or empty");
        }

        Session session = getInstance();

        if (!session.mIsStarted) {
            throw new InstantiationException("Session has not been started yet.");
        }

        session.mEmailAddress = emailAddress;
        session.mFirstName = firstName;
        session.mLastName = lastName;

        session.mHasIdentity = true;

        Config.resolve(IListrakService.class).captureCustomer(emailAddress);
    }

    /**
     * Subscribes the current customer's identity with the given subscriber code
     * @param subscriberCode
     * @param meta
     */
    public static void subscribe(String subscriberCode, @Nullable Map<String, String> meta) throws InstantiationException, UnsupportedEncodingException {
        if (subscriberCode == null || subscriberCode.isEmpty()) {
            throw new IllegalArgumentException("subscriberCode cannot be null or empty");
        }

        Session session = getInstance();
        if (!session.mIsStarted) {
            throw new InstantiationException("Session has not been started yet.");
        }
        if (!session.mHasIdentity) {
            throw new InstantiationException("Session identity has not been set yet.");
        }

        if (meta == null) {
            meta = new HashMap<>();
        }

        Config.resolve(IListrakService.class).subscribeCustomer(subscriberCode, session.mEmailAddress, meta);
    }

    public static boolean isStarted() {
        return getInstance().mIsStarted;
    }

    public static boolean hasIdentity() {
        return getInstance().mHasIdentity;
    }

    public static String getSessionId() {
        return getInstance().mSessionId;
    }

    public static String getEmailAddress() {
        return getInstance().mEmailAddress;
    }

    public static String getFirstName() {
        return getInstance().mFirstName;
    }

    public static String getLastName() {
        return getInstance().mLastName;
    }

    protected static void ensureStarted() throws InstantiationException {
        if (!isStarted()) {
            throw new InstantiationException("com.listrak.mobile.Config has not been initialized yet");
        }
    }

    /**
     * used for testing purposes
     */
    protected static void endSession() {
        getInstance().mIsStarted = false;
        getInstance().mHasIdentity = false;
    }

    protected static void reset() {
        getInstance().mSessionId = generateSessionId();
    }

    private static String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
