package com.listrak.samplemobilestore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.listrak.samplemobilestore.models.Cart;

public abstract class ProductActivityBase extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.getItemId() == R.id.menu_cart) {
                menuItem.setTitle(String.format(getResources().getString(R.string.cart_menu_item), Cart.getProductCount()));
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateUpTo(new Intent(this, ProductListActivity.class));
                return true;
            case R.id.menu_cart:
                this.startActivity(new Intent(this, CartActivity.class));
                return true;
            case R.id.menu_account:
                this.startActivity(new Intent(this, AccountActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
