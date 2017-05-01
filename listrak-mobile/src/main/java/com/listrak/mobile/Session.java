package com.listrak.mobile;

import com.listrak.mobile.interfaces.IListrakService;

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

    public static void startWithIdentity(String emailAddress, String firstName, String lastName) throws InstantiationException, IllegalArgumentException {
        Config.ensureInitialized();
        if (emailAddress == null || emailAddress.isEmpty()) {
            throw new IllegalArgumentException("emailAddress cannot be null or empty");
        }
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("firstName cannot be null or empty");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("lastName cannot be null or empty");
        }

        start();
        setIdentity(emailAddress, firstName, lastName);
    }

    public static void setIdentity(String emailAddress, String firstName, String lastName) throws InstantiationException, IllegalArgumentException {
        if (emailAddress == null || emailAddress.isEmpty()) {
            throw new IllegalArgumentException("emailAddress cannot be null or empty");
        }
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("firstName cannot be null or empty");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("lastName cannot be null or empty");
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

    protected static void reset() {
        getInstance().mSessionId = generateSessionId();
    }

    private static String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
