package com.example.robert.family.util.httptasks;

import android.content.Intent;
import android.os.AsyncTask;

import com.example.robert.family.LoginActivity;
import com.example.robert.family.MainActivity;
import com.example.robert.family.R;
import com.example.robert.family.UserJson;
import com.example.robert.family.util.Url;
import com.example.robert.family.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;

/**
 * Created by robert on 2015-03-06.
 */
public class Login extends AsyncTask<String, Void, String> {

    private final LoginActivity loginActivity;
    private final String userName;
    private final String password;

    public Login(LoginActivity loginActivity, String userName, String password) {
        this.loginActivity = loginActivity;
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected String doInBackground(String... urls) {
        UserJson userToLogin = new UserJson();
        userToLogin.setName(userName);
        userToLogin.setPassword(password);
        try {
            String json = new ObjectMapper().writeValueAsString(userToLogin);
            StringEntity entityToSend = new StringEntity(json);
            return Util.doHttpPost(Url.loginUrl, true, entityToSend);
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
            case "SUCCESS":
                loginActivity.finish();
                //TODO: Create some kind of session!
                loginActivity.startActivity(new Intent(loginActivity, MainActivity.class));
                break;
            case "FAILURE":
                loginActivity.passwordView.setError(loginActivity.getString(R.string.error_incorrect_password));
                loginActivity.passwordView.requestFocus();
                break;
            case "CREATE USER":
                new CreateUser(loginActivity, userName, password);
                break;
        }
    }
}
