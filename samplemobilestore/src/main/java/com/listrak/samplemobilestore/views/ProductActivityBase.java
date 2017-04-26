package com.listrak.samplemobilestore.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.listrak.samplemobilestore.R;
import com.listrak.samplemobilestore.models.Account;
import com.listrak.samplemobilestore.models.Cart;

public abstract class ProductActivityBase extends AppCompatActivity implements Account.IAccountListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Account.getInstance().addAccountListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Account.getInstance().removeAccountListener(this);
    }

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
            switch (menuItem.getItemId()) {
                case R.id.menu_cart:
                    menuItem.setTitle(String.format(getResources().getString(R.string.cart_menu_item), Cart.getInstance().getProductCount()));
                    break;
                case R.id.menu_account:
                    menuItem.setTitle(Account.getInstance().isSignedIn() ?
                            Account.getInstance().getFirstName() : getResources().getString(R.string.account));
                    break;
                default:
                    break;
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

    @Override
    public void onAccountChanged() {
        invalidateOptionsMenu();
    }
}
