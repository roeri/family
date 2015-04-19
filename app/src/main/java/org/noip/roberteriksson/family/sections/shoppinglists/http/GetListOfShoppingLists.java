package org.noip.roberteriksson.family.sections.shoppinglists.http;

import android.os.AsyncTask;
import android.widget.Toast;

import org.noip.roberteriksson.family.sections.shoppinglists.ShoppingListsFragment;
import org.noip.roberteriksson.family.util.HttpPoster;
import org.noip.roberteriksson.family.util.Url;

public class GetListOfShoppingLists extends AsyncTask<String, Void, String> {

    private final ShoppingListsFragment shoppingListsFragment;

    public GetListOfShoppingLists(ShoppingListsFragment shoppingListsFragment) {
        this.shoppingListsFragment = shoppingListsFragment;
    }

    @Override
    protected String doInBackground(String... urls) {
        return HttpPoster.doHttpPost(Url.LIST_OF_SHOPPING_LISTS_GET_SHOPPING_LISTS, null);
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
