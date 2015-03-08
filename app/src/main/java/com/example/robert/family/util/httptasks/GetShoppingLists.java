package com.example.robert.family.util.httptasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.robert.family.main.shoppinglist.ShoppingListFragment;
import com.example.robert.family.main.shoppinglist.ShoppingListsFragment;
import com.example.robert.family.util.HttpPoster;
import com.example.robert.family.util.Url;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-06.
 */
public class GetShoppingLists  extends AsyncTask<String, Void, String> {

    private final ShoppingListsFragment shoppingListsFragment;

    public GetShoppingLists(ShoppingListsFragment shoppingListsFragment) {
        this.shoppingListsFragment = shoppingListsFragment;
    }

    @Override
    protected String doInBackground(String... urls) {
        return HttpPoster.doHttpPost(Url.SHOPPING_LISTS_GET_SHOPPING_LISTS, null);
    }

    @Override
    protected void onPostExecute(String result) {
        if(!result.equals("FAILURE")) {
            shoppingListsFragment.fillListOfShoppingLists(result);
        } else {
            Toast.makeText(shoppingListsFragment.getActivity(), "ERROR in GetShoppingLists", Toast.LENGTH_SHORT).show();
        }
    }
}
