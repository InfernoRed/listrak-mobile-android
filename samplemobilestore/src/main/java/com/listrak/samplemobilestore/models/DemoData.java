package com.listrak.samplemobilestore.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing demo data
 */
public class DemoData {

    /**
     * An array of demo products
     */
    public static final List<Product> PRODUCTS = new ArrayList<>();

    /**
     * A map of sample (demo) products, by SKU.
     */
    public static final Map<String, Product> PRODUCT_MAP = new HashMap<String, Product>();

    static {
        PRODUCTS.addAll(Arrays.asList(
            new Product("BHP-100", "Bluetooth Headphones", "Wireless headphones with super bass.", 19.99),
            new Product("BSP-100", "Bluetooth Speakers", "Wireless speakers for you bluetooth-enabled phone.", 49.99),
            new Product("ADP-100", "Bluetooth Adapter", "Use this adapter to make your normal speakers bluetooth-enabled.", 12.99),
            new Product("ADP-200", "Auto Charging Adapter", "Charge your phone in the car with this adapter.", 12.99),
            new Product("USB-100", "USB Charging Cable", "Extra charging cable for your USB phone", 2.99),
            new Product("ADP-300", "USB Outlet Adapter", "Adapter to charge your USB phone from a wall outlet.", 5.99),
            new Product("CSE-100", "Phone Case", "Protect your phone with this awesome, rubberized case.", 39.99),
            new Product("EXT-100", "USB Extension Cable", "Extend your USB charging cable with this extension cable.", 9.99)
        ));

        for (Product product : PRODUCTS) {
            PRODUCT_MAP.put(product.sku, product);
        }
    }
}
