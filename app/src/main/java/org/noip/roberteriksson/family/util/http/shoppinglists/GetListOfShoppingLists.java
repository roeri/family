package org.noip.roberteriksson.family.util.http.shoppinglists;

import android.os.AsyncTask;
import android.widget.Toast;

import org.noip.roberteriksson.family.fragments.shoppinglists.ListOfShoppingListsFragment;
import org.noip.roberteriksson.family.util.http.HttpPoster;
import org.noip.roberteriksson.family.util.http.Url;

/**
 * Created by robert on 2015-03-06.
 */
public class GetListOfShoppingLists extends AsyncTask<String, Void, String> {

    private final ListOfShoppingListsFragment listOfShoppingListsFragment;

    public GetListOfShoppingLists(ListOfShoppingListsFragment listOfShoppingListsFragment) {
        this.listOfShoppingListsFragment = listOfShoppingListsFragment;
    }

    @Override
    protected String doInBackground(String... urls) {
        return HttpPoster.doHttpPost(Url.LIST_OF_SHOPPING_LISTS_GET_SHOPPING_LISTS, null);
    }

    @Override
    protected void onPostExecute(String result) {
        if(!result.equals("FAILURE")) {
            listOfShoppingListsFragment.fillListOfShoppingLists(result);
        } else {
            Toast.makeText(listOfShoppingListsFragment.getActivity(), "ERROR in GetShoppingLists", Toast.LENGTH_SHORT).show();
        }
    }
}
