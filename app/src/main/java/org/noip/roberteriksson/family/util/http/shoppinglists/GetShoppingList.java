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
