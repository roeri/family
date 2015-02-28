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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.robert.family.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by robert on 2015-02-23.
 */
public class Section1 extends Fragment {

    private Typeface font;
    ShoppingListAdapter shoppingListAdapter;

    private final String shoppingListUrl = "http://roberteriksson.no-ip.org/family/shoppinglist.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_section1, container, false);

        Button button = (Button) view.findViewById(R.id.section1_button);
        button.setTypeface(font);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                createShoppingListItem();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        new HttpAsyncTask().execute();
    }

    public void createShoppingListItem() {
        System.out.println("DEBUG: Trying to create shoppinglist item");
        final View addItemLayout = getView().findViewById(R.id.section1_addItemLayout);
        addItemLayout.setVisibility(View.VISIBLE);

        ((Button) addItemLayout.findViewById(R.id.item_shoppinglist_saveButton)).setTypeface(font);
        Button cancelButton = ((Button) addItemLayout.findViewById(R.id.item_shoppinglist_cancelButton));
        cancelButton.setTypeface(font);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemLayout.setVisibility(View.INVISIBLE);
                View listView = getView().findViewById(R.id.section1_listView);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
                layoutParams.addRule(RelativeLayout.BELOW, R.id.section1_button);
                listView.setLayoutParams(layoutParams);
            }
        });

        View listView = getView().findViewById(R.id.section1_listView);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.section1_addItemLayout);
        listView.setLayoutParams(layoutParams);
    }

    public void fillList(ShoppingList inputShoppingList) {
        System.out.println("DEBUG: Trying to fill shoppinglist");
        View view = getView();
        if(view == null) { //Function is called with null all the time, why?
            return;
        }
        ListView myList = (ListView) getView().findViewById(R.id.section1_listView);

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
        InputStream inputStream;

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(shoppingListUrl);
            inputStream = httpclient.execute(httppost).getEntity().getContent();

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
        @Override
        protected String doInBackground(String... urls) {
            return getShoppingLists();
        }
        @Override
        protected void onPostExecute(String result) {
            ShoppingList shoppingList = null;
            try {
                shoppingList = jsonToShoppingList(result);
            } catch (IOException e) {
                System.out.println("ERROR3: " + e.getMessage());
            }
            fillList(shoppingList);
        }
    }

    public static ShoppingList jsonToShoppingList(String jsonString) throws IOException {
        return new ObjectMapper().readValue(jsonString, ShoppingList.class);
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

            //Detta kan ge performance hit, kolla isf https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
            Button itemEditButton = (Button) convertView.findViewById(R.id.item_shoppinglist_editButton);
            itemEditButton.setTypeface(font);
            //Binda clicklistener till knapp?

            TextView itemText = (TextView) convertView.findViewById(R.id.item_shoppinglist_text);
            itemText.setText(shoppingListItem.text);

            Button itemCheckButton = (Button) convertView.findViewById(R.id.item_shoppinglist_checkButton);
            itemCheckButton.setTypeface(font);
            //Binda clicklistener till knapp?

            return convertView;
       }
    }
}
