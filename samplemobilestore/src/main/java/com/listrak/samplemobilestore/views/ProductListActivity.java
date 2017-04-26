package com.listrak.samplemobilestore.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.listrak.samplemobilestore.R;
import com.listrak.samplemobilestore.models.DemoData;

/**
 * An activity representing a list of Products.
 */
public class ProductListActivity extends ProductActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.product_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        ProductListRecyclerViewAdapter adapter = new ProductListRecyclerViewAdapter(DemoData.PRODUCTS) {
            @Override
            public void onBindViewHolder(final ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra(ProductDetailFragment.ARG_SKU, holder.mItem.sku);

                        context.startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
