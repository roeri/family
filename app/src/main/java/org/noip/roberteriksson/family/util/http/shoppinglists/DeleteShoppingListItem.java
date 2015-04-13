package org.noip.roberteriksson.family.util.http.shoppinglists;

import android.os.AsyncTask;
import android.widget.Toast;

import org.noip.roberteriksson.family.fragments.shoppinglists.ShoppingListFragment;
import org.noip.roberteriksson.family.util.http.Url;
import org.noip.roberteriksson.family.util.http.HttpPoster;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-06.
 */
public class DeleteShoppingListItem extends AsyncTask<String, Void, String> {

    private final ShoppingListFragment shoppingListFragment;
    private final int itemId;

    public DeleteShoppingListItem(ShoppingListFragment shoppingListFragment, int itemId) {
        this.shoppingListFragment = shoppingListFragment;
        this.itemId = itemId;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            StringEntity entityToSend = new StringEntity(Integer.toString(itemId));
            return HttpPoster.doHttpPost(Url.SHOPPING_LIST_DELETE_ITEM, entityToSend);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "FAILURE";
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("SUCCESS")) {
            new GetShoppingList(shoppingListFragment).execute();
        } else {
            Toast.makeText(shoppingListFragment.getActivity(), "ERROR in DeleteShoppingListItem", Toast.LENGTH_SHORT).show();
        }
    }
}
