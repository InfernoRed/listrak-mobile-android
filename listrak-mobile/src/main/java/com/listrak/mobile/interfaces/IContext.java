package com.listrak.mobile.interfaces;

import java.util.Date;

/**
 * Current context information
 * Created by Pam on 5/1/2017.
 */

public interface IContext {
    /**
     * Return the current app id/global session id
     * @return String GUID of the current app
     */
    String getGlobalSessionId();

    /**
     * Return the client's template id
     * @return String of the client template id
     */
    String getTemplateId();

    /**
     * Return the client's merchant id
     * @return String of the client merchant id
     */
    String getMerchantId();

    /**
     * Return the current session id
     * @return String of session id
     */
    String getSessionId();

    /**
     * Return the host override value
     * @return String of the host override setting
     */
    String getHostOverride();

    /**
     * Return whether to use https, false indicates to use http
     * @return boolean to use https
     */
    boolean getUseHttps();

    /**
     * Returns the timestamp for when the context was created
     * @return Date representing the timestamp
     */
    Date getTimestamp();

    /**
     * Returns a new string of a generated GUID
     * @return String of a new GUID
     */
    String generateUid();

    /**
     * Validates that the session has started
     */
    void validate() throws InstantiationException;
}
