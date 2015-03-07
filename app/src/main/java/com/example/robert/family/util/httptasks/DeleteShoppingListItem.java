package com.example.robert.family.util.httptasks;

import android.os.AsyncTask;

import com.example.robert.family.shoppinglist.ShoppingListFragment;
import com.example.robert.family.util.Url;
import com.example.robert.family.util.HttpPoster;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-06.
 */
public class DeleteShoppingListItem extends AsyncTask<String, Void, String> {

    private final ShoppingListFragment shoppingListFragment;
    private final String itemName;

    public DeleteShoppingListItem(ShoppingListFragment shoppingListFragment, String itemName) {
        this.shoppingListFragment = shoppingListFragment;
        this.itemName = itemName;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            StringEntity entityToSend = new StringEntity(itemName);
            HttpPoster.doHttpPost(Url.deleteItemUrl, false, entityToSend);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        new GetShoppingList(shoppingListFragment).execute();
    }
}
