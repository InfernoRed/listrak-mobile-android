package com.listrak.mobile;

import android.content.SharedPreferences;

import com.listrak.mobile.interfaces.IContext;
import com.listrak.mobile.interfaces.IHttpService;
import com.listrak.mobile.interfaces.IListrakService;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import java.util.UUID;

/**
 * Config class responsible for initialization and global variables
 * Created by Pam on 4/27/2017.
 */

public class Config {
    private static Config sInstance;
    private static MutablePicoContainer sContainer;

    private boolean mInitialized;
    private boolean mUseHttps;
    private String mAppId;
    private String mVisitId;
    private String mClientTemplateId;
    private String mClientMerchantId;
    private String mHostOverride;

    /**
     * Private constructor for the singleton creation requiring a builder
     */
    protected Config(Builder builder) {
        AndroidSharedPreferences.initializeContext(builder.androidContext);
        mClientTemplateId = builder.mClientTemplateId;
        mClientMerchantId = builder.mClientMerchantId;
        mUseHttps = builder.mUseHttps;
        mHostOverride = builder.mHostOverride;
    }

    /**
     * Initialize the Config
     * @param config the config builer using the Builder
     * @see com.listrak.mobile.Config.Builder
     */
    public static void initialize(Config config) {
        config.mVisitId = UUID.randomUUID().toString();
        config.mAppId = getAppIdFromSharedPreferences();
        config.mInitialized = true;

        sInstance = config;
        sContainer = new DefaultPicoContainer();
        initializeContainer();
    }

    /***
     * Returns whether the instance of the Config has been initialized
     * @return
     */
    public static boolean isInitialized() {
        return getInstance().mInitialized;
    }

    /**
     * Returns the app id
     * @return
     */
    public static String getAppId() {
        return getInstance().mAppId;
    }

    /**
     * Returns the visit id
     * @return
     */
    public static String getVisitId() {
        return getInstance().mVisitId;
    }

    /**
     * Returns the client template id
     * @return
     */
    public static String getClientTemplateId() {
        return getInstance().mClientTemplateId;
    }

    /**
     * Returns the client merchant id
     * @return
     */
    public static String getClientMerchantId() {
        return getInstance().mClientMerchantId;
    }

    /**
     * Gets the current singleton instance of the Config
     * @return
     */
    public static Config getInstance() {
        return sInstance;
    }

    /**
     * Gets the singleton IoC Container
     * @return
     */
    protected static MutablePicoContainer getContainer() {
        return sContainer;
    }

    /**
     * Get components directly from the IoC Container
     * @param component the explicit type
     * @param <T>
     * @return a constructed instance of the type
     */
    protected static <T> T resolve(Class<T> component) {
        return getContainer().getComponent(component);
    }

    protected static boolean getUseHttps() {
        return getInstance().mUseHttps;
    }

    protected static String getHostOverride() {
        return getInstance().mHostOverride;
    }

    protected static void ensureInitialized() throws InstantiationException {
        if (!isInitialized()) {
            throw new InstantiationException("com.listrak.mobile.Config has not been initialized yet");
        }
    }

    private static void initializeContainer() {
        getContainer().addComponent(IListrakService.class, ListrakService.class);
        getContainer().addComponent(IContext.class, Context.class);
        getContainer().addComponent(IHttpService.class, HttpService.class);
    }

    private static String getAppIdFromSharedPreferences() {
        final String APP_ID_KEY = "LISTRAK-APP-ID";
        String appId = AndroidSharedPreferences.readSharedPreferences(APP_ID_KEY);
        if (appId == null || appId.isEmpty()) {
            appId = UUID.randomUUID().toString();
            AndroidSharedPreferences.saveSharedPreferences(APP_ID_KEY, appId);
        }
        return appId;
    }

    public static class Builder {
        private final android.content.Context androidContext;
        private boolean mUseHttps;
        private String mClientTemplateId;
        private String mClientMerchantId;
        private String mHostOverride;

        /**
         * Default builder constructor requiring the clientTemplateId and clientMerchantId to be initialized
         * @param clientTemplateId
         * @param clientMerchantId
         */
        public Builder(android.content.Context context, String clientTemplateId, String clientMerchantId) {
            if (context == null) {
                throw new IllegalArgumentException("Must supply the android context");
            }
            if (clientTemplateId == null || clientTemplateId.isEmpty()) {
                throw new IllegalArgumentException("Must supply clientTemplateId");
            }
            if (clientMerchantId == null || clientMerchantId.isEmpty()) {
                throw new IllegalArgumentException("Must supply clientMerchantId");
            }
            androidContext = context;
            mClientTemplateId = clientTemplateId;
            mClientMerchantId = clientMerchantId;
            mUseHttps = true;
        }

        /**
         * Optionally sets the use https flag
         *
         * @param useHttps
         * @return instance of this Config
         */
        public Builder setUseHttps(boolean useHttps) {
            mUseHttps = useHttps;
            return this;
        }

        /**
         * Optionally sets the client's template id
         *
         * @param clientTemplateId
         * @return instance of this Config
         */
        public Builder setClientTemplateId(String clientTemplateId) {
            mClientTemplateId = clientTemplateId;
            return this;
        }

        /**
         * Optionally sets the client's merchant id
         *
         * @param clientMerchantId
         * @return instance of this Config
         */
        public Builder setClientMerchantId(String clientMerchantId) {
            mClientMerchantId = clientMerchantId;
            return this;
        }

        /**
         * Optionally sets the host override value
         *
         * @param hostOverride
         * @return instance of this Config
         */
        public Builder setHostOverride(String hostOverride) {
            mHostOverride = hostOverride;
            return this;
        }

        /**
         * Final Builder method to return the new Config instance for the given Builder
         * @return the new Config instance to be used in com.listrak.mobile.Config.initialize
         */
        public Config build() {
            return new Config(this);
        }
    }
}
