package com.example.robert.family;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.robert.family.fragments.Section2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-01.
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String> {

    private final String shoppingListUrl = "http://roberteriksson.no-ip.org/family/shoppinglist.php";
    private final String createItemUrl = "http://roberteriksson.no-ip.org/family/createitem.php";
    private final String deleteItemUrl = "http://roberteriksson.no-ip.org/family/deleteitem.php";

    private final String loginUrl = "http://roberteriksson.no-ip.org/family/login.php";
    private final String createUserUrl = "http://roberteriksson.no-ip.org/family/createuser.php";

    private final LoginActivity loginActivity;
    private final Section2 section2Fragment;
    private final AccountManager accountManager;

    private final HttpTask whatToDo;
    private final String param1;
    private final String param2;

    public HttpAsyncTask(LoginActivity loginActivity, Section2 section2Fragment, AccountManager accountManager, HttpTask whatToDo, String param1, String param2) {
        this.loginActivity = loginActivity;
        this.section2Fragment = section2Fragment;
        this.accountManager = accountManager;
        this.whatToDo = whatToDo;
        this.param1 = param1;
        this.param2 = param2;
    }

    @Override
    protected String doInBackground(String... urls) {
        switch(whatToDo) {
            case GET_SHOPPING_LIST:
                return Util.doHttpPost(shoppingListUrl, true, null);
            case CREATE_SHOPPING_LIST_ITEM:
                try {
                    StringEntity entityToSend = new StringEntity(param1);
                    Util.doHttpPost(createItemUrl, false, entityToSend);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case DELETE_SHOPPING_LIST_ITEM:
                try {
                    StringEntity entityToSend = new StringEntity(param1);
                    Util.doHttpPost(deleteItemUrl, false, entityToSend);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case LOGIN:
                User loginUser = new User();
                loginUser.setName(param1);
                loginUser.setPassword(param2);
                String loginJson = "";
                try {
                    loginJson = new ObjectMapper().writeValueAsString(loginUser);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                try {
                    StringEntity entityToSend = new StringEntity(loginJson);
                    return Util.doHttpPost(loginUrl, true, entityToSend);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                /*for (String credential : DUMMY_CREDENTIALS) {
                    String[] pieces = credential.split(":");
                    if (pieces[0].equals(mEmail)) {
                        //Account exists
                        if (pieces[1].equals(mPassword)) {
                            //Password correct, success!
                            return "SUCCESS";
                        } else {
                            //Wrong password, fail!
                            return "FAILURE";
                        }
                    }
                }*/
                //Account does not exist, create a new user by not breaking.
                return "CREATE USER";
            case CREATE_USER:
                User user = new User();
                user.setName(param1);
                user.setPassword(param2);
                String json = "";
                try {
                    json = new ObjectMapper().writeValueAsString(user);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                try {
                    StringEntity entityToSend = new StringEntity(json);
                    return Util.doHttpPost(createUserUrl, true, entityToSend);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;
            default:
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        switch(whatToDo) {
            case GET_SHOPPING_LIST:
                section2Fragment.fillShoppingList(result);
                break;
            case CREATE_SHOPPING_LIST_ITEM:
                new HttpAsyncTask(null, section2Fragment, accountManager, HttpTask.GET_SHOPPING_LIST, "", "").execute();
                break;
            case DELETE_SHOPPING_LIST_ITEM:
                new HttpAsyncTask(null, section2Fragment, accountManager, HttpTask.GET_SHOPPING_LIST, "", "").execute();
                break;
            case LOGIN:
                loginActivity.mAuthTask = null;
                loginActivity.showProgress(false);
                switch(result) {
                    case "SUCCESS\n":
                        loginActivity.finish();
                        //TODO: Create some kind of session!
                        loginActivity.startActivity(new Intent(loginActivity, MainActivity.class));
                        break;
                    case "FAILURE":
                        loginActivity.mPasswordView.setError(loginActivity.getString(R.string.error_incorrect_password));
                        loginActivity.mPasswordView.requestFocus();
                        break;
                    case "CREATE USER":
                        new HttpAsyncTask(loginActivity, null, accountManager, HttpTask.CREATE_USER, param1, param2).execute();
                        break;
                }
                break;
            case CREATE_USER:
                if(result.equals("SUCCESS\n")) {
                    Account newAccount = new Account(param1, loginActivity.getString(R.string.application_account_type));
                    Bundle userData = new Bundle();
                    userData.putString("First Name", "users first name here");
                    if(accountManager.addAccountExplicitly(newAccount, param2, userData)) {
                        new HttpAsyncTask(loginActivity, null, accountManager, HttpTask.LOGIN, param1, param2).execute();
                        /*Bundle result = new Bundle();
                        result.putString(AccountManager.KEY_ACCOUNT_NAME, mEmail);
                        result.putString(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.application_account_type));
                        setAccountAuthenticatorResult(result);*/
                    } else {
                        //User could not be created or something?
                    }

                } else {
                    System.out.println("FAILURE TO CREATE USER!!!");
                }
                break;
            default:
        }
    }

    @Override
    protected void onCancelled() {
        switch(whatToDo) {
            case LOGIN:
                loginActivity.mAuthTask = null;
                loginActivity.showProgress(false);
                break;
            case CREATE_USER:

                break;
            default:
        }
    }
}