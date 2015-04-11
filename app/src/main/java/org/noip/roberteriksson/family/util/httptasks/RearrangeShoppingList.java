package org.noip.roberteriksson.family.util.httptasks;

import android.os.AsyncTask;
import android.widget.Toast;

import org.noip.roberteriksson.family.main.shoppinglist.ShoppingListFragment;
import org.noip.roberteriksson.family.main.shoppinglist.ShoppingListJson;
import org.noip.roberteriksson.family.util.HttpPoster;
import org.noip.roberteriksson.family.util.Url;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-06.
 */
public class RearrangeShoppingList extends AsyncTask<String, Void, String> {

    private final ShoppingListFragment shoppingListFragment;
    private final ShoppingListJson shoppingList;

    public RearrangeShoppingList(ShoppingListFragment shoppingListFragment, ShoppingListJson shoppingList) {
        this.shoppingListFragment = shoppingListFragment;
        this.shoppingList = shoppingList;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            String json = new ObjectMapper().writeValueAsString(shoppingList);
            StringEntity entityToSend = new StringEntity(json);
            return HttpPoster.doHttpPost(Url.SHOPPING_LIST_REARRANGE, entityToSend);
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
            new GetShoppingList(shoppingListFragment).execute();
        } else {
            Toast.makeText(shoppingListFragment.getActivity(), "ERROR in RearrangeShoppingList", Toast.LENGTH_SHORT).show();
        }
    }
}
