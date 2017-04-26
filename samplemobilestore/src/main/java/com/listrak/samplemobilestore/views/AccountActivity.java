package com.listrak.samplemobilestore.views;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.listrak.samplemobilestore.R;
import com.listrak.samplemobilestore.models.Account;

public class AccountActivity extends AppCompatActivity implements Account.IAccountListener {

    private EditText mEmailEdit;
    private EditText mFirstNameEdit;
    private EditText mLastNameEdit;
    private Button mSignInOutBtn;
    private TextView mDescriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.account));
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDescriptionText = (TextView) findViewById(R.id.description);
        mEmailEdit = (EditText) findViewById(R.id.email);
        mFirstNameEdit = (EditText) findViewById(R.id.first_name);
        mLastNameEdit = (EditText) findViewById(R.id.last_name);
        mSignInOutBtn = (Button) findViewById(R.id.btn_sign_in_out);

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
        setupView();
    }

    private void setupView() {
        Account account = Account.getInstance();

        mEmailEdit.setText(account.getEmail());
        mFirstNameEdit.setText(account.getFirstName());
        mLastNameEdit.setText(account.getLastName());

        toggleForm(account.isSignedIn());

        mSignInOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account aInstance = Account.getInstance();
                if (aInstance.isSignedIn()) {
                    aInstance.signOut(view.getContext());
                } else {
                    boolean success = aInstance.signIn(view.getContext(), mEmailEdit.getText().toString(),
                            mFirstNameEdit.getText().toString(), mLastNameEdit.getText().toString());
                    if (!success) {
                        new AlertDialog.Builder(view.getContext())
                                .setTitle(getResources().getString(R.string.account_sign_in_error_title))
                                .setMessage(getResources().getString(R.string.account_sign_in_error_message))
                                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }
        });
    }

    private void toggleForm(boolean isSignedIn) {
        mEmailEdit.setEnabled(!isSignedIn);
        mFirstNameEdit.setEnabled(!isSignedIn);
        mLastNameEdit.setEnabled(!isSignedIn);
        mSignInOutBtn.setText(getResources().getString(isSignedIn ? R.string.sign_out : R.string.sign_in));
        mDescriptionText.setText(getResources().getString(isSignedIn ?
                R.string.account_description_signed_in : R.string.account_description_not_signed_in));
    }
}
