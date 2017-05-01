package com.listrak.mobile;

import android.support.annotation.Nullable;

/**
 * Cart Item class
 * Created by Pam on 4/28/2017.
 */

public class CartItem extends Item {
    public final String title;
    public final String imageUrl;
    public final String linkUrl;

    protected CartItem(String sku, int quantity, double amount, String title, @Nullable String imageUrl, @Nullable String linkUrl) {
        super(sku, quantity, amount);

        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
    }
}
