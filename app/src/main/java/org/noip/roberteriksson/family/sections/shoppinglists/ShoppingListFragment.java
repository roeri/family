package org.noip.roberteriksson.family.sections.shoppinglists;

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

import com.example.robert.family.R;
import org.noip.roberteriksson.family.main.MainActivity;
import org.noip.roberteriksson.family.sections.FragmentNumbers;
import org.noip.roberteriksson.family.sections.SectionFragment;
import org.noip.roberteriksson.family.sections.shoppinglists.http.CheckShoppingListItem;
import org.noip.roberteriksson.family.sections.shoppinglists.http.CreateShoppingListItem;
import org.noip.roberteriksson.family.sections.shoppinglists.http.DeleteShoppingListItem;
import org.noip.roberteriksson.family.sections.shoppinglists.http.GetShoppingList;
import org.noip.roberteriksson.family.sections.shoppinglists.http.RearrangeShoppingList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by robert on 2015-02-23.
 */
@Slf4j
public class ShoppingListFragment extends Fragment implements SectionFragment {

    private final ShoppingListFragment theThis = this;
    private Typeface font;
    public boolean editMode = false;
    public int id; //TODO: FIX THIS, also make private?
    public ShoppingListAdapter shoppingListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        Button editButton = (Button) view.findViewById(R.id.shoppingList_editButton);
        editButton.setTypeface(font);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditModeEnabled(editMode = (!editMode));
            }
        });

        Button createItemButton = (Button) view.findViewById(R.id.shoppingList_createShoppingListItemButton); //TODO: Make this button toggleable
        createItemButton.setTypeface(font);
        createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            setEditModeEnabled(editMode = false);
            showCreateShoppingListItem();
            }
        });

        initDragSortListView(view);

        return view;
    }

    private void initDragSortListView(View view) {
        final DragSortListView shoppingList = (DragSortListView) view.findViewById(R.id.shoppingList);
        DragSortController shoppingListController = new DragSortController(shoppingList);
        shoppingListController.setDragInitMode(DragSortController.ON_DOWN);
        shoppingListController.setBackgroundColor(Color.TRANSPARENT);

        shoppingList.setFloatViewManager(shoppingListController);
        shoppingList.setOnTouchListener(shoppingListController);

        shoppingList.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int which) {
            shoppingListAdapter.remove(shoppingListAdapter.getItem(which));
            }
        });

        shoppingList.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                if (from != to) {
                    ShoppingListItemJson item = shoppingListAdapter.getItem(from);
                    shoppingListAdapter.remove(item);
                    shoppingListAdapter.insert(item, to);

                    rearrangeShoppingList();
                }
            }
        });
    }

    private void rearrangeShoppingList() {
        ShoppingListJson shoppingList = new ShoppingListJson();
        int numItems = shoppingListAdapter.getCount();
        for(int i = 0; i < numItems; i++) {
            ShoppingListItemJson item = shoppingListAdapter.getItem(i);
            item.sequence = i + 1;
            shoppingList.getItems().add(item);
        }
        new RearrangeShoppingList(theThis, shoppingList).execute();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void showCreateShoppingListItem() {
        View view = getView();
        if(view == null) {
            log.warn("view == null in showCreateShoppingListItem()");
            return;
        }
        final View addItemLayout = view.findViewById(R.id.shoppingList_createShoppingListItemLayout);
        addItemLayout.setVisibility(View.VISIBLE);

        final EditText createItemText = (EditText) view.findViewById(R.id.shoppingList_createShoppingListItemLayout_createItemText);
        final Button cancelButton = (Button) addItemLayout.findViewById(R.id.shoppingList_createShoppingListItemLayout_cancelButton);
        final Button saveButton = (Button) addItemLayout.findViewById(R.id.shoppingList_createShoppingListItemLayout_saveButton);
        final View listView = view.findViewById(R.id.shoppingList);

        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
        final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        cancelButton.setTypeface(font);
        saveButton.setTypeface(font);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemLayout.setVisibility(View.INVISIBLE);
                layoutParams.addRule(RelativeLayout.BELOW, R.id.shoppingList_createShoppingListItemButton);
                inputMethodManager.hideSoftInputFromWindow(createItemText.getWindowToken(), 0);
            }
        });

        createItemText.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View createItemText, boolean hasFocus) {
            if (hasFocus) {
                ((EditText) createItemText).setText("");
            }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new CreateShoppingListItem(theThis, createItemText.getText().toString()).execute();
            inputMethodManager.hideSoftInputFromWindow(createItemText.getWindowToken(), 0);
            final View addItemLayout = getView().findViewById(R.id.shoppingList_createShoppingListItemLayout);
            addItemLayout.setVisibility(View.INVISIBLE);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.shoppingList_createShoppingListItemButton);
            }
        });

        layoutParams.addRule(RelativeLayout.BELOW, R.id.shoppingList_createShoppingListItemLayout);
        createItemText.requestFocus();
        inputMethodManager.showSoftInput(createItemText, 0);
    }

    public void fillShoppingList(String shoppingListJson) {
        View view = getView();
        if(view == null) {
            return;
        }
        ListView shoppingList = (ListView) getView().findViewById(R.id.shoppingList);

        try {
            ShoppingListJson inputShoppingListJsonItems = new ObjectMapper().readValue(shoppingListJson, ShoppingListJson.class);
            this.shoppingListAdapter = new ShoppingListAdapter(getActivity(), inputShoppingListJsonItems.getItems());
            shoppingList.setAdapter(this.shoppingListAdapter);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void setEditModeEnabled(boolean editMode) {
        View view = getView();
        if(view == null) {
            log.warn("view == null in setEditModeEnabled()");
            return;
        }
        DragSortListView shoppingList = (DragSortListView) view.findViewById(R.id.shoppingList);
        shoppingList.setDragEnabled(editMode);
        toggleEditButton(editMode);
    }

    public void toggleEditButton(boolean enable) {
        View view = getView();
        if(view == null) {
            log.warn("view == null in toggleEditButton()");
            return;
        }
        Button editButton = (Button) view.findViewById(R.id.shoppingList_editButton);
        if(enable) {
            editButton.setTypeface(font, Typeface.BOLD);
            editButton.setTextColor(Color.rgb(51, 102, 153));
        } else {
            editButton.setTypeface(font, Typeface.NORMAL);
            editButton.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void refresh() {
        toggleEditButton(editMode = false);
        new GetShoppingList(this).execute();
    }

    @Override
    public void goBack() {
        ((MainActivity) getActivity()).setCurrentlyLiveFragment(FragmentNumbers.LIST_OF_SHOPPING_LISTS);
    }

    public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItemJson> {

        public ShoppingListAdapter(Context context, List<ShoppingListItemJson> shoppingList) {
            super(context, 0, shoppingList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shopping_list, parent, false);
            }
            final ShoppingListItemJson shoppingListItemJson = getItem(position);

            final TextView itemText = (TextView) convertView.findViewById(R.id.item_shoppinglist_text);
            final Button itemCheckButton = (Button) convertView.findViewById(R.id.item_shoppinglist_checkButton);

            itemCheckButton.setTypeface(font);
            itemText.setText(shoppingListItemJson.text);

            final View.OnClickListener itemDeleteListener = new View.OnClickListener() {
                @Override
                public void onClick(View thisButton) {
                    new DeleteShoppingListItem(theThis, shoppingListItemJson.id).execute();
                }
            };

            final View.OnClickListener itemCheckListener = new View.OnClickListener() {
                @Override
                public void onClick(View thisButton) {
                    Button button = (Button) thisButton;
                    new CheckShoppingListItem(theThis, shoppingListItemJson.id).execute();
                    button.setText(getString(R.string.icon_checkboxChecked));
                    button.setOnClickListener(itemDeleteListener);
                }
            };

            if(shoppingListItemJson.checked) {
                itemCheckButton.setText(getString(R.string.icon_checkboxChecked));
                itemCheckButton.setOnClickListener(itemDeleteListener);
            } else {
                itemCheckButton.setText(getString(R.string.icon_checkboxUnchecked));
                itemCheckButton.setOnClickListener(itemCheckListener);
            }

            return convertView;
        }
    }
}
