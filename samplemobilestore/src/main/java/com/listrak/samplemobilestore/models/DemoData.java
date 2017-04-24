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
                new Product("sku1", "test1", "", 0.00),
                new Product("sku2", "test2", "", 0.00),
                new Product("sku3", "test3", "", 0.00),
                new Product("sku4", "test4", "", 0.00)
        ));

        for (Product product : PRODUCTS) {
            PRODUCT_MAP.put(product.sku, product);
        }
    }
}
