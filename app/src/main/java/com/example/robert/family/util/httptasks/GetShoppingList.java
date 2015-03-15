package com.example.robert.family.util.httptasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.robert.family.main.shoppinglist.ShoppingListFragment;
import com.example.robert.family.util.Url;
import com.example.robert.family.util.HttpPoster;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

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
        StringEntity entityToSend = null;
        try {
            entityToSend = new StringEntity(Integer.toString(shoppingListFragment.id));
            return HttpPoster.doHttpPost(Url.SHOPPING_LIST_GET_SHOPPING_LIST, entityToSend);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "FAILURE";
    }

    @Override
    protected void onPostExecute(String result) {
        if(!result.equals("FAILURE")) {
            shoppingListFragment.fillShoppingList(result);
        } else {
            Toast.makeText(shoppingListFragment.getActivity(), "ERROR in GetShoppingList", Toast.LENGTH_SHORT).show();
        }
    }
}
