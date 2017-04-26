package com.listrak.samplemobilestore.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.listrak.samplemobilestore.R;
import com.listrak.samplemobilestore.models.Cart;
import com.listrak.samplemobilestore.models.DemoData;
import com.listrak.samplemobilestore.models.Product;

import java.text.NumberFormat;

/**
 * A fragment representing a single Product detail screen.
 */
public class ProductDetailFragment extends Fragment implements Cart.ICartListener {
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
     * The button to Add/Remove the product from the cart
     */
    private Button mProductBtn;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cart.getInstance().addCartListener(this);

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
        mProductBtn = (Button) rootView.findViewById(R.id.btn_toggle_product);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.product_detail)).setText(mItem.description);
            ((TextView) rootView.findViewById(R.id.product_sku)).setText(mItem.sku);
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            ((TextView) rootView.findViewById(R.id.product_amount)).setText(formatter.format(mItem.amount));
            updateButton();
        }

        rootView.findViewById(R.id.btn_toggle_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (((Button) view).getText() == getResources().getString(R.string.add_to_cart)) {
                  Cart.getInstance().addProduct(mItem);
              } else {
                  Cart.getInstance().removeProduct(mItem);
              }
            }
        });

        return rootView;
    }

    @Override
    public void onCartChanged() {
        updateButton();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Cart.getInstance().removeCartListener(this);
    }

    protected void updateButton() {
        mProductBtn.setText(getResources().getString(
                Cart.getInstance().containsProduct(mItem) ? R.string.remove_from_cart : R.string.add_to_cart));
    }
}
