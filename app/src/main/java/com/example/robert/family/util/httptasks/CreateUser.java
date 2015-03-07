package com.example.robert.family.util.httptasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.robert.family.login.LoginActivity;
import com.example.robert.family.R;
import com.example.robert.family.login.UserToCreateJson;
import com.example.robert.family.util.Url;
import com.example.robert.family.util.HttpPoster;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;

/**
 * Created by robert on 2015-03-06.
 */
public class CreateUser extends AsyncTask<String, Void, String> {

    private final LoginActivity loginActivity;
    private final String email;
    private final String password;

    public CreateUser(LoginActivity loginActivity, String email, String password) {
        this.loginActivity = loginActivity;
        this.email = email;
        this.password = password;
    }

    @Override
    protected String doInBackground(String... urls) {
        UserToCreateJson userToCreate = new UserToCreateJson();
        userToCreate.setEmail(email);
        userToCreate.setPassword(password);
        try {
            String json = new ObjectMapper().writeValueAsString(userToCreate);
            StringEntity entityToSend = new StringEntity(json);
            return HttpPoster.doHttpPost(Url.LOGIN_CREATE_USER, true, entityToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        if(!result.equals("FAILURE")) {
            Account newAccount = new Account(email, loginActivity.getString(R.string.application_account_type));
            Bundle userData = new Bundle();
            userData.putString("id", result);
            if(AccountManager.get(loginActivity).addAccountExplicitly(newAccount, password, userData)) {
                Account[] accounts = AccountManager.get(loginActivity).getAccounts();
                new Login(loginActivity, email, password).execute();
                /*Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, mEmail);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.application_account_type));
                setAccountAuthenticatorResult(result);*/
            } else {
                System.out.println("FAILURE TO CREATE USER");
            }
        } else {
            System.out.println("FAILURE TO CREATE USER");
        }
    }
}
