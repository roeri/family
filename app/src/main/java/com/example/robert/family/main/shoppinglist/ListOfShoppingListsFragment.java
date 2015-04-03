package com.example.robert.family.main.shoppinglist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
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
import com.example.robert.family.util.httptasks.CreateShoppingList;
import com.example.robert.family.util.httptasks.DeleteShoppingList;
import com.example.robert.family.util.httptasks.GetListOfShoppingLists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.io.IOException;
import java.util.List;

/**
 * Created by robert on 2015-02-23.
 */
public class ListOfShoppingListsFragment extends Fragment implements RefreshableFragment {

    private final ListOfShoppingListsFragment theThis = this;
    private Typeface font;
    List<ListOfShoppingListsItemJson> listOfShoppingLists;
    private boolean editMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_list_of_shopping_lists, container, false);

        Button editListOfShoppingListsButton = (Button) view.findViewById(R.id.listOfShoppingLists_editListOfShoppingListsButton);
        editListOfShoppingListsButton.setTypeface(font);
        editListOfShoppingListsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMode) {
                    editMode = false;
                    //DragSortListView listOfShoppingLists = (DragSortListView) getView().findViewById(R.id.listOfShoppingLists);
                    //Toast.makeText(getActivity(), "DISABLING - ENABLED?: " + listOfShoppingLists.isDragEnabled(), Toast.LENGTH_SHORT).show();
                    //listOfShoppingLists.setDragEnabled(true);
                } else {
                    editMode = true;
                    //DragSortListView listOfShoppingLists = (DragSortListView) getView().findViewById(R.id.listOfShoppingLists);
                    //Toast.makeText(getActivity(), "ENABLING - ENABLED?: " + listOfShoppingLists.isDragEnabled(), Toast.LENGTH_SHORT).show();
                    //listOfShoppingLists.setDragEnabled(true);
                }
                editMode(editMode);
            }
        });

        Button createShoppingListButton = (Button) view.findViewById(R.id.listOfShoppingLists_createShoppingListButton); //TODO: Make button toggleable
        createShoppingListButton.setTypeface(font);
        createShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateShoppingList();
            }
        });

        DragSortListView listView = (DragSortListView) view.findViewById(R.id.listOfShoppingLists);
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
        new GetListOfShoppingLists(this, this.editMode).execute();
    }

    public void showCreateShoppingList() {
        editMode(false);
        final View addShoppingListLayout = getView().findViewById(R.id.listOfShoppingLists_createShoppingListLayout);
        addShoppingListLayout.setVisibility(View.VISIBLE);

        final EditText createShoppingListText = (EditText) getView().findViewById(R.id.listOfShoppingLists_createShoppingListLayout_createShoppingListText);
        final Button cancelButton = (Button) addShoppingListLayout.findViewById(R.id.listOfShoppingLists_createShoppingListLayout_cancelButton);
        final Button saveButton = (Button) addShoppingListLayout.findViewById(R.id.listOfShoppingLists_createShoppingListLayout_saveButton);
        final View listView = getView().findViewById(R.id.listOfShoppingLists);

        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        cancelButton.setTypeface(font);
        saveButton.setTypeface(font);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //TODO: Refactor to use View v param?
            addShoppingListLayout.setVisibility(View.INVISIBLE);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.listOfShoppingLists_createShoppingListButton);
            inputMethodManager.hideSoftInputFromWindow(createShoppingListText.getWindowToken(), 0);
            }
        });

        createShoppingListText.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View createShoppingListText, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) createShoppingListText).setText("");
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateShoppingList(theThis, createShoppingListText.getText().toString()).execute();
                inputMethodManager.hideSoftInputFromWindow(createShoppingListText.getWindowToken(), 0);
                final View addShoppingListLayout = getView().findViewById(R.id.listOfShoppingLists_createShoppingListLayout);
                addShoppingListLayout.setVisibility(View.INVISIBLE);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.listOfShoppingLists_createShoppingListButton);
            }
        });

        layoutParams.addRule(RelativeLayout.BELOW, R.id.listOfShoppingLists_createShoppingListLayout);
        createShoppingListText.requestFocus();
        inputMethodManager.showSoftInput(createShoppingListText, 0);
    }

    public void fillListOfShoppingLists(String shoppingListsJson, boolean editMode) {
        View view = getView();
        if(view == null) { //TODO: Function is called with null all the time, why?
            return;
        }
        ListView shoppingLists = (ListView) getView().findViewById(R.id.listOfShoppingLists);

        ListOfShoppingListsJson inputShoppingLists;
        try {
            inputShoppingLists = new ObjectMapper().readValue(shoppingListsJson, ListOfShoppingListsJson.class);
            this.listOfShoppingLists = inputShoppingLists.getItems();
            ShoppingListsAdapter shoppingListsAdapter = new ShoppingListsAdapter(getActivity(), this.listOfShoppingLists, false);
            shoppingLists.setAdapter(shoppingListsAdapter);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        editMode(editMode);
    }

    public void editMode(boolean editMode) {
        ListView shoppingLists = (ListView) getView().findViewById(R.id.listOfShoppingLists);
        ShoppingListsAdapter shoppingListsAdapter = new ShoppingListsAdapter(getActivity(), this.listOfShoppingLists, editMode);
        shoppingLists.setAdapter(shoppingListsAdapter);
    }

    @Override
    public void refresh() {
        Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_SHORT).show();
        new GetListOfShoppingLists(this, this.editMode).execute();
    }

    public class ShoppingListsAdapter extends ArrayAdapter<ListOfShoppingListsItemJson> {
        boolean editMode;

        public ShoppingListsAdapter(Context context, List<ListOfShoppingListsItemJson> shoppingLists, boolean editMode) { //TODO: Check List vs ArrayList here.
            super(context, 0, shoppingLists);
            this.editMode = editMode;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_of_shopping_lists, parent, false);
            }

            final ListOfShoppingListsItemJson shoppingList = getItem(position);

            final Button itemDeleteButton = (Button) convertView.findViewById(R.id.item_shoppinglists_deleteButton);
            final TextView itemText = (TextView) convertView.findViewById(R.id.item_shoppinglists_text);

            itemDeleteButton.setTypeface(font);

            itemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisButton) {
                    ((MainActivity) getActivity()).onShoppingListSelected(shoppingList); //TODO: Reference shoppingListItem in a better way.
                }
            });
            itemText.setHeight(140); //TODO: Set height in a better, more dynamic way.
            itemText.setGravity(Gravity.CENTER_VERTICAL);
            itemText.setText(shoppingList.getName());

            itemDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisButton) {
                    new DeleteShoppingList(theThis, shoppingList.id).execute();
                }
            });

            if(editMode) {
                itemDeleteButton.setVisibility(View.VISIBLE);
            } else {
                itemDeleteButton.setVisibility(View.GONE);
            }

            return convertView;
        }
    }
}
