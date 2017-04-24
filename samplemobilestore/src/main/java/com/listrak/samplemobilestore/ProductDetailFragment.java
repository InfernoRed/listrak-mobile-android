package com.listrak.samplemobilestore;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.listrak.samplemobilestore.models.DemoData;
import com.listrak.samplemobilestore.models.Product;

/**
 * A fragment representing a single Product detail screen.
 */
public class ProductDetailFragment extends Fragment {
    /**
     * The fragment argument representing the product SKU that this fragment
     * represents.
     */
    public static final String ARG_SKU = "product_sku";

    /**
     * The Product this fragment is presenting.
     */
    private Product mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_SKU)) {
            mItem = DemoData.PRODUCT_MAP.get(getArguments().getString(ARG_SKU));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.product_detail)).setText(mItem.description);
        }

        return rootView;
    }
}
