package org.noip.roberteriksson.family.sections.shoppinglists.http;

import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;
import org.noip.roberteriksson.family.sections.shoppinglists.ShoppingListsFragment;
import org.noip.roberteriksson.family.util.HttpPoster;
import org.noip.roberteriksson.family.util.Url;

import java.io.UnsupportedEncodingException;

public class CreateShoppingList extends AsyncTask<String, Void, String> {

    private final ShoppingListsFragment shoppingListsFragment;
    private final String name;

    public CreateShoppingList(ShoppingListsFragment shoppingListsFragment, String name) {
        this.shoppingListsFragment = shoppingListsFragment;
        this.name = name;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            ShoppingListsFragment.ShoppingListsItemJson shoppingListsItemJson = new ShoppingListsFragment.ShoppingListsItemJson();
            shoppingListsItemJson.setName(name);
            String json = new ObjectMapper().writeValueAsString(shoppingListsItemJson);
            StringEntity entityToSend = new StringEntity(json);
            return HttpPoster.doHttpPost(Url.LIST_OF_SHOPPING_LISTS_CREATE_SHOPPING_LIST, entityToSend);
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return "FAILURE";
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("SUCCESS")) {
            new GetListOfShoppingLists(shoppingListsFragment).execute();
        } else {
            Toast.makeText(shoppingListsFragment.getActivity(), "ERROR in CreateShoppingList", Toast.LENGTH_SHORT).show();
        }
    }
}
