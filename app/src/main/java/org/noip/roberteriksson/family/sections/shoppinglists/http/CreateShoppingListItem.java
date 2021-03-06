package org.noip.roberteriksson.family.sections.shoppinglists.http;

import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;
import org.noip.roberteriksson.family.sections.shoppinglists.ShoppingListFragment;
import org.noip.roberteriksson.family.session.Session;
import org.noip.roberteriksson.family.util.HttpPoster;
import org.noip.roberteriksson.family.util.Url;

import java.io.UnsupportedEncodingException;

public class CreateShoppingListItem extends AsyncTask<String, Void, String> {

    private final ShoppingListFragment shoppingListFragment;
    private final String itemName;

    public CreateShoppingListItem(ShoppingListFragment shoppingListFragment, String itemName) {
        this.shoppingListFragment = shoppingListFragment;
        this.itemName = itemName;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            ShoppingListFragment.ShoppingListItemJson shoppingListItemJson = new ShoppingListFragment.ShoppingListItemJson();
            shoppingListItemJson.setShoppingListsId(shoppingListFragment.id);
            shoppingListItemJson.setUsersId(Session.getInstance().getUserId());
            shoppingListItemJson.setText(itemName);
            String json = new ObjectMapper().writeValueAsString(shoppingListItemJson);
            StringEntity entityToSend = new StringEntity(json);
            return HttpPoster.doHttpPost(Url.SHOPPING_LIST_CREATE_ITEM, entityToSend);
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return "FAILURE";
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("SUCCESS")) {
            new GetShoppingList(shoppingListFragment).execute();
        } else {
            Toast.makeText(shoppingListFragment.getActivity(), "ERROR in CreateShoppingListItem", Toast.LENGTH_SHORT).show();
        }
    }
}
