package com.example.robert.family.util.httptasks;

import android.os.AsyncTask;

import com.example.robert.family.shoppinglist.ShoppingList;
import com.example.robert.family.util.Url;
import com.example.robert.family.util.Util;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-06.
 */
public class CheckShoppingListItem extends AsyncTask<String, Void, String> {

    private final ShoppingList shoppingList;
    private final String itemName;

    public CheckShoppingListItem(ShoppingList shoppingList, String itemName) {
        this.shoppingList = shoppingList;
        this.itemName = itemName;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            StringEntity entityToSend = new StringEntity(itemName);
            Util.doHttpPost(Url.checkItemUrl, false, entityToSend);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        new GetShoppingList(shoppingList).execute();
    }
}
