package org.noip.roberteriksson.family.session;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.example.robert.family.R;

import org.noip.roberteriksson.family.main.MainActivity;
import org.noip.roberteriksson.family.session.http.GetUserId;

public class Session {
    private static Session me = null;
    private Account userAccount;
    private MainActivity mainActivity;
    private int userId;
    private int widgetShoppingListId;

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
            new GetUserId(userAccount.name).execute();
        } else {
            showError("Could not initiate local user account");
        }
        //new GetWidgetShoppingList().execute();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setWidgetShoppingListId(int shoppingListId) {
        this.widgetShoppingListId = shoppingListId;
    }

    public int getWidgetShoppingListId() {
        return widgetShoppingListId;
    }

    public String getUserEmail() {
        return userAccount.name;
    }

    public void showError(String errorText) {
        mainActivity.showError(errorText);
    }
}
