package com.example.robert.family.main.shoppinglist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robert.family.R;
import com.example.robert.family.main.MainActivity;
import com.example.robert.family.main.RefreshableFragment;
import com.example.robert.family.util.FragmentId;
import com.example.robert.family.util.FragmentNumbers;
import com.example.robert.family.util.httptasks.GetShoppingLists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 2015-02-23.
 */
public class ShoppingListsFragment extends Fragment implements RefreshableFragment {

    private final ShoppingListsFragment theThis = this;
    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_shoppinglists, container, false);

        Button createShoppingListButton = (Button) view.findViewById(R.id.shoppinglists_createShoppingListButton);
        createShoppingListButton.setTypeface(font);
        createShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateShoppingList();
            }
        });

        DragSortListView listView = (DragSortListView) view.findViewById(R.id.list_2);
        DragSortController dragSortController = new DragSortController(listView);
        dragSortController.setDragInitMode(DragSortController.ON_LONG_PRESS);
        dragSortController.setBackgroundColor(Color.TRANSPARENT);

        listView.setFloatViewManager(dragSortController);
        listView.setOnTouchListener(dragSortController);
        listView.setDragEnabled(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        new GetShoppingLists(this).execute();
    }

    public void showCreateShoppingList() {
        final View addShoppingListLayout = getView().findViewById(R.id.shoppinglists_addShoppingListLayout);
        addShoppingListLayout.setVisibility(View.VISIBLE);

        final EditText createShoppingListText = (EditText) getView().findViewById(R.id.item_shoppinglists_createShoppingListText);
        final Button cancelButton = (Button) addShoppingListLayout.findViewById(R.id.item_shoppinglists_cancelButton);
        final Button saveButton = (Button) addShoppingListLayout.findViewById(R.id.item_shoppinglists_saveButton);
        final View listView = getView().findViewById(R.id.list_2);

        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        cancelButton.setTypeface(font);
        saveButton.setTypeface(font);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //TODO: Refactor to use View v param?
            addShoppingListLayout.setVisibility(View.INVISIBLE);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.shoppinglists_createShoppingListButton);
            inputMethodManager.hideSoftInputFromWindow(createShoppingListText.getWindowToken(), 0);
            }
        });

        createShoppingListText.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View createShoppingListText, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) createShoppingListText).setText("");
                } else {
                    ((EditText) createShoppingListText).setText(R.string.shoppinglists_createShoppingListHint);
                }
            }
        });

        /*saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new CreateShoppingList(theThis, createShoppingListText.getText().toString()).execute();
            inputMethodManager.hideSoftInputFromWindow(createShoppingListText.getWindowToken(), 0);
            final View addShoppingListLayout = getView().findViewById(R.id.shoppinglists_addShoppingListLayout);
            addShoppingListLayout.setVisibility(View.INVISIBLE);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.shoppinglists_createShoppingListButton);
            }
        });*/

        layoutParams.addRule(RelativeLayout.BELOW, R.id.shoppinglists_addShoppingListLayout);
        createShoppingListText.requestFocus();
        inputMethodManager.showSoftInput(createShoppingListText, 0);
    }

    public void fillListOfShoppingLists(String shoppingListsJson) {
        View view = getView();
        if(view == null) { //Function is called with null all the time, why?
            return;
        }
        ListView shoppingLists = (ListView) getView().findViewById(R.id.list_2);

        try {
            ShoppingListsJson inputShoppingLists = new ObjectMapper().readValue(shoppingListsJson, ShoppingListsJson.class);
            ShoppingListsAdapter shoppingListsAdapter = new ShoppingListsAdapter(getActivity(), inputShoppingLists.getItems());
            shoppingLists.setAdapter(shoppingListsAdapter);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void refresh() {
        Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_SHORT).show();
        new GetShoppingLists(this).execute();
    }

    public class ShoppingListsAdapter extends ArrayAdapter<ShoppingListsItemJson> {

        public ShoppingListsAdapter(Context context, List<ShoppingListsItemJson> shoppingLists) { //TODO: Check List vs ArrayList here.
            super(context, 0, shoppingLists);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shoppinglists, parent, false);
            }
            final ShoppingListsItemJson shoppingListItem = getItem(position);

            final Button itemDeleteButton = (Button) convertView.findViewById(R.id.item_shoppinglists_deleteButton);
            final TextView itemText = (TextView) convertView.findViewById(R.id.item_shoppinglists_text);

            itemDeleteButton.setTypeface(font);

            final View.OnClickListener selectShoppingListListener = new View.OnClickListener() {
                @Override
                public void onClick(View thisButton) {
                    Toast.makeText(getActivity(), "USING LIST...", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).onNavigationDrawerItemSelected(FragmentNumbers.SHOPPING_LIST);
                    //TODO: Show selected shopping list
                    //new DeleteShoppingListItem(theThis, shoppingListsJson.id).execute();
                }
            };
            itemText.setOnClickListener(selectShoppingListListener);
            itemText.setText(shoppingListItem.getName());

            /*final View.OnClickListener itemDeleteListener = new View.OnClickListener() {
                @Override
                public void onClick(View thisButton) {
                new DeleteShoppingListItem(theThis, shoppingListsJson.id).execute();
                }
            };*/

            return convertView;
        }
    }
}
