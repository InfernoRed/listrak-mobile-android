package com.listrak.samplemobilestore.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.listrak.samplemobilestore.R;
import com.listrak.samplemobilestore.models.Cart;

public class CartActivity extends AppCompatActivity implements Cart.ICartListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.cart));
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupRecyclerView();
        setupFooter();
        setupClickHandlers();

        Cart.getInstance().addCartListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Cart.getInstance().removeCartListener(this);
    }

    @Override
    public void onCartChanged() {
        setupRecyclerView();
        setupFooter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.product_list);
        assert recyclerView != null;
        ProductListRecyclerViewAdapter adapter = new ProductListRecyclerViewAdapter(Cart.getInstance().getProducts()){
            @Override
            public void onBindViewHolder(final ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Remove Item")
                                .setMessage("Are you sure you want to delete this entry?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        Cart.getInstance().removeProduct(holder.mItem);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void setupFooter() {
        TextView totalAmount = (TextView) findViewById(R.id.total_amount);
        totalAmount.setText(Cart.getInstance().getProductTotalFormatted());
        totalAmount.setVisibility(Cart.getInstance().getProductCount() > 0 ? View.VISIBLE : View.GONE);

        ((TextView) findViewById(R.id.instructions)).setText(getResources()
                .getString(Cart.getInstance().getProductCount() > 0 ?
                        R.string.cart_instructions_not_empty :
                        R.string.cart_instructions_empty));

        findViewById(R.id.btn_clear).setVisibility(Cart.getInstance().getProductCount() > 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.btn_checkout).setVisibility(Cart.getInstance().getProductCount() > 0 ? View.VISIBLE : View.GONE);
    }


    private void setupClickHandlers() {
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart.getInstance().clearProducts();
            }
        });

        findViewById(R.id.btn_checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) view.getContext();
                activity.startActivity(new Intent(activity, CheckoutActivity.class));
            }
        });
    }
}
