package org.noip.roberteriksson.family.sections.shoppinglists.http;

import android.os.AsyncTask;
import android.widget.Toast;

import org.noip.roberteriksson.family.sections.shoppinglists.ShoppingListsFragment;
import org.noip.roberteriksson.family.util.HttpPoster;
import org.noip.roberteriksson.family.util.Url;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-06.
 */
public class DeleteShoppingList extends AsyncTask<String, Void, String> {

    private final ShoppingListsFragment shoppingListsFragment;
    private final int shoppingListId;

    public DeleteShoppingList(ShoppingListsFragment shoppingListsFragment, int shoppingListId) {
        this.shoppingListsFragment = shoppingListsFragment;
        this.shoppingListId = shoppingListId;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            StringEntity entityToSend = new StringEntity(Integer.toString(shoppingListId));
            return HttpPoster.doHttpPost(Url.LIST_OF_SHOPPING_LISTS_DELETE_SHOPPING_LIST, entityToSend);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "FAILURE";
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("SUCCESS")) {
            new GetListOfShoppingLists(shoppingListsFragment).execute();
        } else {
            Toast.makeText(shoppingListsFragment.getActivity(), "ERROR in DeleteShoppingList", Toast.LENGTH_SHORT).show();
        }
    }
}
