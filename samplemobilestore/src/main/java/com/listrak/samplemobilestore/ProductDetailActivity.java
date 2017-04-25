package com.listrak.samplemobilestore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.listrak.samplemobilestore.models.Cart;

/**
 * An activity representing a single Product detail screen.
 */
public class ProductDetailActivity extends ProductActivityBase implements Cart.ICartListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ProductDetailFragment.ARG_SKU,
                    getIntent().getStringExtra(ProductDetailFragment.ARG_SKU));
            ProductDetailFragment fragment = new ProductDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.product_detail_container, fragment)
                    .commit();
        }
        Cart.addCartListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cart.removeCartListener(this);
    }

    @Override
    public void onCartChanged() {
        invalidateOptionsMenu();
    }
}
