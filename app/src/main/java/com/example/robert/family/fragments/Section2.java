package com.example.robert.family.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.robert.family.HttpTasks;
import com.example.robert.family.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by robert on 2015-02-23.
 */
public class Section2 extends Fragment {
    private Typeface font;
    ShoppingListAdapter shoppingListAdapter;

    private final String shoppingListUrl = "http://roberteriksson.no-ip.org/family/shoppinglist.php";
    private final String createItemUrl = "http://roberteriksson.no-ip.org/family/createitem.php";
    private final String deleteItemUrl = "http://roberteriksson.no-ip.org/family/deleteitem.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_section2, container, false);

        Button button = (Button) view.findViewById(R.id.section2_button);
        button.setTypeface(font);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showCreateShoppingListItem();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        new HttpAsyncTask(HttpTasks.GET_SHOPPING_LIST, "").execute();
    }

    public void showCreateShoppingListItem() {
        System.out.println("DEBUG: Trying to show the shoppinglist item");
        final View addItemLayout = getView().findViewById(R.id.section2_addItemLayout);
        addItemLayout.setVisibility(View.VISIBLE);

        Button cancelButton = (Button) addItemLayout.findViewById(R.id.item_shoppinglist_cancelButton);
        cancelButton.setTypeface(font);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Refactor to use View v param?
                addItemLayout.setVisibility(View.INVISIBLE);
                View listView = getView().findViewById(R.id.section2_listView);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
                layoutParams.addRule(RelativeLayout.BELOW, R.id.section2_button);
                listView.setLayoutParams(layoutParams);
            }
        });

        EditText createItemText = (EditText) getView().findViewById(R.id.item_shoppinglist_createItemText);
        createItemText.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View createItemText, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) createItemText).setText("");
                } else {
                    ((EditText) createItemText).setText(R.string.section2_createItemHint);
                }
            }
        });

        Button saveButton = (Button) addItemLayout.findViewById(R.id.item_shoppinglist_saveButton);
        saveButton.setTypeface(font);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpAsyncTask(HttpTasks.CREATE_SHOPPING_LIST_ITEM, "").execute();
            }
        });

        View listView = getView().findViewById(R.id.section2_listView);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.section2_addItemLayout);
        listView.setLayoutParams(layoutParams);
    }

    public void createShoppingListItem() {
        System.out.println("DEBUG: Trying to create shoppinglist item");
        final EditText createItemText  = (EditText) getView().findViewById(R.id.item_shoppinglist_createItemText);
        String newItem = createItemText.getEditableText().toString();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(createItemUrl);
        try{
            httppost.setEntity(new StringEntity(newItem));
            httpclient.execute(httppost);
        }catch(Exception e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void deleteShoppingListItem(String itemName) {
        System.out.println("DEBUG: Trying to delete shoppinglist item");

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(deleteItemUrl);
        try{
            httppost.setEntity(new StringEntity(itemName));
            httpclient.execute(httppost);
        }catch(Exception e){
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void fillList(ShoppingList inputShoppingList) {
        System.out.println("DEBUG: Trying to fill shoppinglist");
        View view = getView();
        if(view == null) { //Function is called with null all the time, why?
            return;
        }
        ListView myList = (ListView) getView().findViewById(R.id.section2_listView);

        ArrayList<ShoppingListItem> shoppingListItems = new ArrayList<>();
        for(String shoppingListItem : inputShoppingList.getItems()) {
            shoppingListItems.add(new ShoppingListItem(shoppingListItem));
        }

        shoppingListAdapter = new ShoppingListAdapter(getActivity(), shoppingListItems);
        myList.setAdapter(shoppingListAdapter);
    }

    public String getShoppingLists() {
        System.out.println("DEBUG: Trying to retrieve shopping lists via HTTP");
        String endResult = "";

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(shoppingListUrl);
            InputStream inputStream = httpclient.execute(httppost).getEntity().getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8); // "UTF-8" / "iso-8859-1"
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            endResult = sb.toString();
        }catch(Exception e){
            System.out.println("ERROR: " + e.getMessage());
        }
        return endResult;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        HttpTasks whatToDo;
        String param;

        public HttpAsyncTask(HttpTasks whatToDo, String param) {
            this.whatToDo = whatToDo;
            this.param = param;
        }

        @Override
        protected String doInBackground(String... urls) {
            switch(whatToDo) {
                case GET_SHOPPING_LIST:
                    return getShoppingLists();
                case CREATE_SHOPPING_LIST_ITEM:
                    createShoppingListItem();
                    break;
                case DELETE_SHOPPING_LIST_ITEM:
                    deleteShoppingListItem(param);
                    break;
                default:
            }
            return "";
        }
        @Override
        protected void onPostExecute(String result) {
            switch(whatToDo) {
                case GET_SHOPPING_LIST:
                    ShoppingList shoppingList = null;
                    try {
                        shoppingList = jsonToShoppingList(result);
                    } catch (IOException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                    fillList(shoppingList);
                default:
            }
        }
    }

    public static ShoppingList jsonToShoppingList(String jsonString) throws IOException {
        return new ObjectMapper().readValue(jsonString, ShoppingList.class);
    }

    public static String shoppingListToJson(ShoppingList shoppingList) throws IOException {
        return new ObjectMapper().writeValueAsString(shoppingList);
    }

    public class ShoppingListItem {
        String text;

        public ShoppingListItem(String text) {
            this.text = text;
        }
    }

    public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItem> {

        public ShoppingListAdapter(Context context, ArrayList<ShoppingListItem> shoppingList) {
            super(context, 0, shoppingList);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ShoppingListItem shoppingListItem = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shoppinglist, parent, false);
            }

            final TextView itemText = (TextView) convertView.findViewById(R.id.item_shoppinglist_text);
            itemText.setText(shoppingListItem.text);

            Button itemEditButton = (Button) convertView.findViewById(R.id.item_shoppinglist_editButton);
            itemEditButton.setTypeface(font);
            itemEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new HttpAsyncTask(HttpTasks.DELETE_SHOPPING_LIST_ITEM, itemText.getText().toString()).execute();
                }
            });

            Button itemCheckButton = (Button) convertView.findViewById(R.id.item_shoppinglist_checkButton);
            itemCheckButton.setTypeface(font);

            return convertView;
        }
    }
}
