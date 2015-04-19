package org.noip.roberteriksson.family.sections.shoppinglists.http;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.noip.roberteriksson.family.sections.shoppinglists.ShoppingListFragment;
import org.noip.roberteriksson.family.util.HttpPoster;
import org.noip.roberteriksson.family.util.Url;

import java.io.UnsupportedEncodingException;

public class GetShoppingList extends AsyncTask<String, Void, String> {

    private final ShoppingListFragment shoppingListFragment;

    public GetShoppingList(ShoppingListFragment shoppingListFragment) {
        this.shoppingListFragment = shoppingListFragment;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            StringEntity entityToSend = new StringEntity(Integer.toString(shoppingListFragment.id));
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
