package com.example.robert.family.util.httptasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.robert.family.main.shoppinglist.ListOfShoppingListsFragment;
import com.example.robert.family.main.shoppinglist.ShoppingListFragment;
import com.example.robert.family.util.HttpPoster;
import com.example.robert.family.util.Url;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-06.
 */
public class DeleteShoppingList extends AsyncTask<String, Void, String> {

    private final ListOfShoppingListsFragment listOfShoppingListsFragment;
    private final int id;

    public DeleteShoppingList(ListOfShoppingListsFragment listOfShoppingListsFragment, int id) {
        this.listOfShoppingListsFragment = listOfShoppingListsFragment;
        this.id = id;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            StringEntity entityToSend = new StringEntity(Integer.toString(id));
            return HttpPoster.doHttpPost(Url.LIST_OF_SHOPPING_LISTS_DELETE_SHOPPING_LIST, entityToSend);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "FAILURE";
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("SUCCESS")) {
            new GetListOfShoppingLists(listOfShoppingListsFragment).execute();
        } else {
            Toast.makeText(listOfShoppingListsFragment.getActivity(), "ERROR in DeleteShoppingList", Toast.LENGTH_SHORT).show();
        }
    }
}
