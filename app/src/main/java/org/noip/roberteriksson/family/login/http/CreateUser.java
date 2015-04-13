package org.noip.roberteriksson.family.login.http;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.noip.roberteriksson.family.login.LoginActivity;
import com.example.robert.family.R;
import org.noip.roberteriksson.family.login.UserToCreateJson;
import org.noip.roberteriksson.family.util.Url;
import org.noip.roberteriksson.family.util.HttpPoster;
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
            return HttpPoster.doHttpPost(Url.LOGIN_CREATE_USER, entityToSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "FAILURE";
    }

    @Override
    protected void onPostExecute(String result) {
        if(!result.equals("FAILURE")) {
            Account newAccount = new Account(email, loginActivity.getString(R.string.application_account_type));
            Bundle userData = new Bundle();
            userData.putString("id", result);
            if(AccountManager.get(loginActivity).addAccountExplicitly(newAccount, password, userData)) {
                new Login(loginActivity, email, password).execute();
            } else {
                Toast.makeText(loginActivity, "ERROR in CreateUser", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(loginActivity, "ERROR in CreateUser", Toast.LENGTH_SHORT).show();
        }
    }
}
