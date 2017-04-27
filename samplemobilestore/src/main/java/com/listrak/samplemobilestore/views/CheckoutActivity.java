package com.listrak.samplemobilestore.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.listrak.samplemobilestore.R;
import com.listrak.samplemobilestore.models.Account;
import com.listrak.samplemobilestore.models.Cart;

import java.text.DecimalFormat;
import java.util.Random;

public class CheckoutActivity extends AppCompatActivity implements Account.IAccountListener {

    private EditText mEmailEdit;
    private EditText mFirstNameEdit;
    private EditText mLastNameEdit;
    private TextView mDescriptionText;
    private TextView mOrderNumText;
    private TextView mAmountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.checkout));
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupView();

        Account.getInstance().addAccountListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Account.getInstance().removeAccountListener(this);
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

    @Override
    public void onAccountChanged() {
        setupAccount();
    }

    private void setupView() {
        mDescriptionText = (TextView) findViewById(R.id.description);
        mOrderNumText = (TextView) findViewById(R.id.order_num);
        mAmountText = (TextView) findViewById(R.id.total_amount);
        mEmailEdit = (EditText) findViewById(R.id.email);
        mFirstNameEdit = (EditText) findViewById(R.id.first_name);
        mLastNameEdit = (EditText) findViewById(R.id.last_name);

        Random rand = new Random();
        DecimalFormat df = new DecimalFormat("000000");
        final String orderNumber = df.format(rand.nextInt(1000000)) + "-" + df.format(rand.nextInt(1000000));
        mOrderNumText.setText(String.format(getResources().getString(R.string.checkout_order_number), orderNumber));
        mAmountText.setText(String.format(getResources().getString(R.string.checkout_total),
                Cart.getInstance().getProductTotalFormatted()));

        setupAccount();

        findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(getResources().getString(R.string.checkout))
                        .setMessage(getResources().getString(R.string.checkout_confirm_message))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                boolean success = Cart.getInstance().processOrder(mEmailEdit.getText().toString(),
                                        mFirstNameEdit.getText().toString(), mLastNameEdit.getText().toString(), orderNumber);
                                if (!success) {
                                    new AlertDialog.Builder(view.getContext())
                                            .setTitle(getResources().getString(R.string.checkout_error_title))
                                            .setMessage(getResources().getString(R.string.checkout_error_message))
                                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                } else {
                                    // navigate to root
                                    navigateUpTo(new Intent(view.getContext(), ProductListActivity.class));
                                }
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

    private void setupAccount() {
        Account account = Account.getInstance();

        mEmailEdit.setText(account.getEmail());
        mFirstNameEdit.setText(account.getFirstName());
        mLastNameEdit.setText(account.getLastName());

        toggleForm(account.isSignedIn());
    }

    private void toggleForm(boolean isSignedIn) {
        mEmailEdit.setEnabled(!isSignedIn);
        mFirstNameEdit.setEnabled(!isSignedIn);
        mLastNameEdit.setEnabled(!isSignedIn);
        mDescriptionText.setText(getResources().getString(isSignedIn ?
                R.string.checkout_description_signed_in : R.string.checkout_description_not_signed_in));
    }
}
