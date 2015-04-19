package org.noip.roberteriksson.family.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.robert.family.R;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.noip.roberteriksson.family.login.http.Login;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    public AsyncTask mAuthTask = null; //TODO: Make this private?

    private AutoCompleteTextView emailView;
    public EditText passwordView; //TODO: Make private!
    private View progressView;
    private View loginFormView;

    @Data
    public static class UserToCreateJson {
        @JsonProperty("email")
        private String email;
        @JsonProperty("password")
        private String password;
    }

    @Data
    public static class UserToLoginJson {
        @JsonProperty("email")
        private String email;
        @JsonProperty("password")
        private String password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        Button emailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        AccountManager accountManager = AccountManager.get(getApplicationContext());

        populateAutoComplete();

        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin(null);
                    return true;
                }
                return false;
            }
        });

        emailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(null);
            }
        });

        Account[] accounts = accountManager.getAccountsByType(getString(R.string.application_account_type));
        if(accounts != null) {
            for(Account account : accounts) {
                attemptLogin(account);
            }
        }
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    public void attemptLogin(Account account) {
        if (mAuthTask != null) {
            return;
        }
        AccountManager accountManager = AccountManager.get(getApplicationContext());

        boolean cancel = false;
        View focusView = null;
        String loginEmail;
        String loginPassword;

        emailView.setError(null);
        passwordView.setError(null);

        if(account == null) {
            loginEmail = emailView.getText().toString();
            loginPassword = passwordView.getText().toString();
        } else {
            loginEmail = account.name;
            loginPassword = accountManager.getPassword(account);
        }

        if (!TextUtils.isEmpty(loginPassword) && !isPasswordValid(loginPassword)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(loginEmail)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(loginEmail)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            new Login(this, loginEmail, loginPassword).execute(); //TODO: Save this in mAuthTask?
        }
    }

    private boolean isEmailValid(String email) { //TODO: Extend this with proper email verification.
        return email.contains("@") && (email.length() <= 30); //TODO: Perhaps change to a longer maximum length? Also in db.
    }

    private boolean isPasswordValid(String password) { //TODO: Extend this to make sure only letters and numbers are used.
        return (password.length() >= 6) && (password.length() <= 30);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
            Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

            ContactsContract.Contacts.Data.MIMETYPE +
                " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

            ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        emailView.setAdapter(adapter);
    }
}



