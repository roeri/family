package com.example.robert.family.util.httptasks;

import android.os.AsyncTask;

import com.example.robert.family.shoppinglist.ShoppingList;
import com.example.robert.family.util.Url;
import com.example.robert.family.util.Util;

/**
 * Created by robert on 2015-03-06.
 */
public class GetShoppingList extends AsyncTask<String, Void, String> {

    private final ShoppingList shoppingList;

    public GetShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    @Override
    protected String doInBackground(String... urls) {
        return Util.doHttpPost(Url.shoppingListUrl, true, null);
    }

    @Override
    protected void onPostExecute(String result) {
        shoppingList.fillShoppingList(result);
    }
}
