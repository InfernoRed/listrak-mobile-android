package com.listrak.mobile;

import com.listrak.mobile.interfaces.IListrakService;

/**
 * Activity tracking class
 * Created by Pam on 5/1/2017.
 */

public class Activity {
    public static void trackProductBrowse(String sku) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("sku cannot be null or empty");
        }

        Config.resolve(IListrakService.class).trackProductBrowse(new String[]{sku});
    }
}
