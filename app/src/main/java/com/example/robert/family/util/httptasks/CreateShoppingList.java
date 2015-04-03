package com.example.robert.family.util.httptasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.robert.family.Session;
import com.example.robert.family.main.shoppinglist.ListOfShoppingListsFragment;
import com.example.robert.family.main.shoppinglist.ListOfShoppingListsItemJson;
import com.example.robert.family.main.shoppinglist.ShoppingListFragment;
import com.example.robert.family.main.shoppinglist.ShoppingListItemJson;
import com.example.robert.family.util.HttpPoster;
import com.example.robert.family.util.Url;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-06.
 */
public class CreateShoppingList extends AsyncTask<String, Void, String> {

    private final ListOfShoppingListsFragment listOfShoppingListsFragment;
    private final String name;

    public CreateShoppingList(ListOfShoppingListsFragment listOfShoppingListsFragment, String name) {
        this.listOfShoppingListsFragment = listOfShoppingListsFragment;
        this.name = name;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            ListOfShoppingListsItemJson listOfShoppingListsItemJson = new ListOfShoppingListsItemJson();
            listOfShoppingListsItemJson.setName(name);
            String json = new ObjectMapper().writeValueAsString(listOfShoppingListsItemJson);
            StringEntity entityToSend = new StringEntity(json);
            return HttpPoster.doHttpPost(Url.LIST_OF_SHOPPING_LISTS_CREATE_SHOPPING_LIST, entityToSend);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "FAILURE";
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("SUCCESS")) {
            new GetListOfShoppingLists(listOfShoppingListsFragment).execute();
        } else {
            Toast.makeText(listOfShoppingListsFragment.getActivity(), "ERROR in CreateShoppingList", Toast.LENGTH_SHORT).show();
        }
    }
}
