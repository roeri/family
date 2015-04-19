package org.noip.roberteriksson.family.login.http;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.noip.roberteriksson.family.login.LoginActivity;
import org.noip.roberteriksson.family.main.MainActivity;
import com.example.robert.family.R;
import org.noip.roberteriksson.family.util.Url;
import org.noip.roberteriksson.family.util.HttpPoster;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;

/**
 * Created by robert on 2015-03-06.
 */
public class Login extends AsyncTask<String, Void, String> {

    private final LoginActivity loginActivity;
    private final String email;
    private final String password;

    public Login(LoginActivity loginActivity, String email, String password) {
        this.loginActivity = loginActivity;
        this.email = email;
        this.password = password;
    }

    @Override
    protected String doInBackground(String... urls) {
        LoginActivity.UserToLoginJson userToLogin = new LoginActivity.UserToLoginJson();
        userToLogin.setEmail(email);
        userToLogin.setPassword(password);
        try {
            String json = new ObjectMapper().writeValueAsString(userToLogin);
            StringEntity entityToSend = new StringEntity(json);
            return HttpPoster.doHttpPost(Url.LOGIN_ATTEMPT_LOGIN, entityToSend);
        } catch (Exception e) { //JsonProcessingException or UnsupportedEncodingException
            e.printStackTrace();
        }
        return "CREATE USER";
    }

    @Override
    protected void onPostExecute(String result) {
        loginActivity.mAuthTask = null;
        loginActivity.showProgress(false);
        switch(result) {
            case "FAILURE": //TODO: Handler for failure.
            case "WRONG PASSWORD":
                loginActivity.passwordView.setError(loginActivity.getString(R.string.error_incorrect_password));
                loginActivity.passwordView.requestFocus();
                break;
            case "CREATE USER":
                new CreateUser(loginActivity, email, password).execute();
                break;
            default: //If none of the above, result is the users ID.
                //Check if account exists
                AccountManager accountManager = AccountManager.get(loginActivity);
                Account[] accounts = accountManager.getAccountsByType(loginActivity.getString(R.string.application_account_type));
                if(accounts.length == 0) {
                    //Account doesn't exist on device, create it.
                    //Toast.makeText(loginActivity, "Account does not exist on device", Toast.LENGTH_SHORT).show();
                    Account newAccount = new Account(email, loginActivity.getString(R.string.application_account_type));
                    Bundle userData = new Bundle();
                    userData.putString("id", result);
                    if(accountManager.addAccountExplicitly(newAccount, password, userData)) {
                        //Successfully added account to device.
                        //Toast.makeText(loginActivity, "Created account on device.", Toast.LENGTH_SHORT).show();
                    } else {
                        //Couldn't add user account, this shouldn't happen.
                        //Toast.makeText(loginActivity, "ERROR in Login", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Toast.makeText(loginActivity, "Account already existed on device.", Toast.LENGTH_SHORT).show();
                }
                loginActivity.finish();
                loginActivity.startActivity(new Intent(loginActivity, MainActivity.class));
                break;
        }
    }
}
