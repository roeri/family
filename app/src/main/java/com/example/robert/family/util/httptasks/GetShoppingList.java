package com.example.robert.family.util.httptasks;

import android.os.AsyncTask;

import com.example.robert.family.shoppinglist.ShoppingListFragment;
import com.example.robert.family.util.Url;
import com.example.robert.family.util.HttpPoster;

/**
 * Created by robert on 2015-03-06.
 */
public class GetShoppingList extends AsyncTask<String, Void, String> {

    private final ShoppingListFragment shoppingListFragment;

    public GetShoppingList(ShoppingListFragment shoppingListFragment) {
        this.shoppingListFragment = shoppingListFragment;
    }

    @Override
    protected String doInBackground(String... urls) {
        return HttpPoster.doHttpPost(Url.shoppingListUrl, true, null);
    }

    @Override
    protected void onPostExecute(String result) {
        shoppingListFragment.fillShoppingList(result);
    }
}
