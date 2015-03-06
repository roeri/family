package com.example.robert.family.util.httptasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.robert.family.LoginActivity;
import com.example.robert.family.R;
import com.example.robert.family.UserJson;
import com.example.robert.family.util.Url;
import com.example.robert.family.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;

/**
 * Created by robert on 2015-03-06.
 */
public class CreateUser extends AsyncTask<String, Void, String> {

    private final LoginActivity loginActivity;
    private final String userName;
    private final String password;

    public CreateUser(LoginActivity loginActivity, String userName, String password) {
        this.loginActivity = loginActivity;
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected String doInBackground(String... urls) {
        UserJson userToCreate = new UserJson();
        userToCreate.setName(userName);
        userToCreate.setPassword(password);
        try {
            String json = new ObjectMapper().writeValueAsString(userToCreate);
            StringEntity entityToSend = new StringEntity(json);
            return Util.doHttpPost(Url.createUserUrl, true, entityToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("SUCCESS")) { //TODO: Remove all \n in HTTP results
            Account newAccount = new Account(userName, loginActivity.getString(R.string.application_account_type));
            Bundle userData = new Bundle();
            userData.putString("First Name", "users first name here");
            if(AccountManager.get(loginActivity).addAccountExplicitly(newAccount, password, userData)) {
                new Login(loginActivity, userName, password).execute();
                //new HttpTask(loginActivity, null, accountManager, HttpTasks.LOGIN, userName, password).execute();
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
