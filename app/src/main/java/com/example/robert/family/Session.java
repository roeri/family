package com.example.robert.family;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;

import com.example.robert.family.main.MainActivity;
import com.example.robert.family.util.httptasks.GetUserId;

/**
 * Created by robert on 2015-03-07.
 */
public class Session {
    private static Session me = null;
    private Account userAccount;
    private MainActivity mainActivity;
    private int userId;

    public static Session getInstance() {
        if(me == null) {
            me = new Session();

        }
        return me;
    }

    private Session() {
        //Avoiding multiple instances.
    }

    public void initiateUserAccount(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        AccountManager accountManager = AccountManager.get(mainActivity);
        Account[] accounts = accountManager.getAccountsByType(mainActivity.getString(R.string.application_account_type));
        if(accounts.length > 0) {
            this.userAccount = accounts[0]; //TODO: Handle more than one account.
        }
        new GetUserId(this, userAccount.name).execute();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public MainActivity getMainActivity() {
        return this.mainActivity;
    }

    public String getUserEmail() {
        return userAccount.name;
    }
}
