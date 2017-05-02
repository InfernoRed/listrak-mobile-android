package com.listrak.mobile;

import com.listrak.mobile.interfaces.IContext;

import java.util.Date;
import java.util.UUID;

/**
 * IContext implementation for the current app's context
 * Created by Pam on 5/1/2017.
 */

class Context implements IContext {
    private final Date timestamp;

    protected Context() {
        this.timestamp = new Date();
    }

    @Override
    public String getGlobalSessionId() {
        return Config.getAppId();
    }

    @Override
    public String getVisitId() { return Config.getVisitId(); }

    @Override
    public String getTemplateId() {
        return Config.getClientTemplateId();
    }

    @Override
    public String getMerchantId() {
        return Config.getClientMerchantId();
    }

    @Override
    public String getSessionId() {
        return Session.getSessionId();
    }

    @Override
    public String getHostOverride() {
        return Config.getHostOverride();
    }

    @Override
    public boolean getUseHttps() {
        return Config.getUseHttps();
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public long getUnixTimestamp() {
        return timestamp.getTime() / 1000;
    }

    @Override
    public String generateUid() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void validate() throws InstantiationException {
        Session.ensureStarted();
    }
}
