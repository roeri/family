package com.example.robert.family;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;

import com.example.robert.family.main.MainActivity;

/**
 * Created by robert on 2015-03-07.
 */
public class Session {
    private static Session me = null;
    private Account userAccount;
    private AccountManager accountManager;
    //private MainActivity mainActivity;

    public static Session getInstance() {
        if(me == null) {
            me = new Session();

        }
        return me;
    }

    private Session() {
        //Avoiding multiple instances.
    }

    public void initiate(MainActivity mainActivity) {
        //this.mainActivity = mainActivity;
        this.accountManager = AccountManager.get(mainActivity);
        this.userAccount = accountManager.getAccountsByType(mainActivity.getString(R.string.application_account_type))[0]; //TODO: Handle if account does not exist.
    }

    public int getUserId() {
        int id = Integer.parseInt(accountManager.getUserData(userAccount, "id"));
        return id;
    }

    public String getUserEmail() {
        return userAccount.name;
    }
}
