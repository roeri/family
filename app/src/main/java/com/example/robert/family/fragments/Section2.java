package com.example.robert.family.fragments;

import android.accounts.AccountManager;
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

import com.example.robert.family.HttpAsyncTask;
import com.example.robert.family.HttpTask;
import com.example.robert.family.R;
import com.example.robert.family.Util;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by robert on 2015-02-23.
 */
public class Section2 extends Fragment {

    private final Section2 theThis = this;
    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_section2, container, false);

        Button createItemButton = (Button) view.findViewById(R.id.section2_createItemButton);
        createItemButton.setTypeface(font);
        createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateShoppingListItem();
            }
        });

        DragSortListView listView = (DragSortListView) view.findViewById(R.id.list);
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
        new HttpAsyncTask(null, theThis, AccountManager.get(getActivity()), HttpTask.GET_SHOPPING_LIST, "", "").execute();
    }

    public void showCreateShoppingListItem() {
        final View addItemLayout = getView().findViewById(R.id.section2_addItemLayout);
        addItemLayout.setVisibility(View.VISIBLE);

        final EditText createItemText = (EditText) getView().findViewById(R.id.item_shoppinglist_createItemText);
        final Button cancelButton = (Button) addItemLayout.findViewById(R.id.item_shoppinglist_cancelButton);
        final Button saveButton = (Button) addItemLayout.findViewById(R.id.item_shoppinglist_saveButton);
        final View listView = getView().findViewById(R.id.list);

        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        cancelButton.setTypeface(font);
        saveButton.setTypeface(font);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Refactor to use View v param?
                addItemLayout.setVisibility(View.INVISIBLE);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.section2_createItemButton);
                inputMethodManager.hideSoftInputFromWindow(createItemText.getWindowToken(), 0);
            }
        });

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpAsyncTask(null, theThis, AccountManager.get(getActivity()), HttpTask.CREATE_SHOPPING_LIST_ITEM, createItemText.getText().toString(), "").execute();
                inputMethodManager.hideSoftInputFromWindow(createItemText.getWindowToken(), 0);
                final View addItemLayout = getView().findViewById(R.id.section2_addItemLayout);
                addItemLayout.setVisibility(View.INVISIBLE);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.section2_createItemButton);
            }
        });

        layoutParams.addRule(RelativeLayout.BELOW, R.id.section2_addItemLayout);
        createItemText.requestFocus();
        inputMethodManager.showSoftInput(createItemText, 0);
    }

    public void fillShoppingList(String shoppingListJson) {
        View view = getView();
        if(view == null) { //Function is called with null all the time, why?
            return;
        }
        ListView shoppingList = (ListView) getView().findViewById(R.id.list);

        try {
            ShoppingList inputShoppingListItems = Util.jsonToShoppingList(shoppingListJson);
            ArrayList<ShoppingListItem> shoppingListItems = new ArrayList<>();
            for(ShoppingListItem shoppingListItem : inputShoppingListItems.getItems()) {
                shoppingListItems.add(shoppingListItem);
            }

            ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter(getActivity(), shoppingListItems);
            shoppingList.setAdapter(shoppingListAdapter);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItem> {

        public ShoppingListAdapter(Context context, ArrayList<ShoppingListItem> shoppingList) {
            super(context, 0, shoppingList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) { //TODO: Top item in list is always checked it seems... WHYYY
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shoppinglist, parent, false);
            }
            ShoppingListItem shoppingListItem = getItem(position);

            final TextView itemText = (TextView) convertView.findViewById(R.id.item_shoppinglist_text);
            final Button itemCheckButton = (Button) convertView.findViewById(R.id.item_shoppinglist_checkButton);

            itemCheckButton.setTypeface(font);
            itemText.setText(shoppingListItem.text);

            final View.OnClickListener itemDeleteListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new HttpAsyncTask(null, theThis, AccountManager.get(getActivity()), HttpTask.DELETE_SHOPPING_LIST_ITEM, itemText.getText().toString(), "").execute();
                }
            };
            final View.OnClickListener itemCheckListener = new View.OnClickListener() {
                @Override
                public void onClick(View thisButton) {
                    Button button = (Button) thisButton;
                    new HttpAsyncTask(null, theThis, AccountManager.get(getActivity()), HttpTask.CHECK_SHOPPING_LIST_ITEM, itemText.getText().toString(), "").execute();
                    button.setText(getString(R.string.icon_checkboxChecked));
                    button.setOnClickListener(itemDeleteListener);
                }
            };
            if(shoppingListItem.checked) {
                itemCheckButton.setText(getString(R.string.icon_checkboxChecked));
                itemCheckButton.setOnClickListener(itemDeleteListener);
            } else {
                itemCheckButton.setOnClickListener(itemCheckListener);
            }

            return convertView;
        }
    }
}
