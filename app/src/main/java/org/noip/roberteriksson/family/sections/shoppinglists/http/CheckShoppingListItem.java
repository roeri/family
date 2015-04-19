package org.noip.roberteriksson.family.sections.shoppinglists.http;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.noip.roberteriksson.family.sections.shoppinglists.ShoppingListFragment;
import org.noip.roberteriksson.family.util.HttpPoster;
import org.noip.roberteriksson.family.util.Url;

import java.io.UnsupportedEncodingException;

public class CheckShoppingListItem extends AsyncTask<String, Void, String> {

    private final ShoppingListFragment shoppingListFragment;
    private final int itemId;

    public CheckShoppingListItem(ShoppingListFragment shoppingListFragment, int itemId) {
        this.shoppingListFragment = shoppingListFragment;
        this.itemId = itemId;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            StringEntity entityToSend = new StringEntity(Integer.toString(itemId));
            return HttpPoster.doHttpPost(Url.SHOPPING_LIST_CHECK_ITEM, entityToSend);
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
            Toast.makeText(shoppingListFragment.getActivity(), "ERROR in CheckShoppingListItem", Toast.LENGTH_SHORT).show();
        }
    }
}
