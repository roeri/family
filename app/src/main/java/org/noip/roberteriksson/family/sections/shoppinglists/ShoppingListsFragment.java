package org.noip.roberteriksson.family.sections.shoppinglists;

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

import com.example.robert.family.R;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import org.noip.roberteriksson.family.main.MainActivity;
import org.noip.roberteriksson.family.sections.FragmentNumbers;
import org.noip.roberteriksson.family.sections.SectionFragment;
import org.noip.roberteriksson.family.sections.shoppinglists.http.CreateShoppingList;
import org.noip.roberteriksson.family.sections.shoppinglists.http.DeleteShoppingList;
import org.noip.roberteriksson.family.sections.shoppinglists.http.GetListOfShoppingLists;
import org.noip.roberteriksson.family.sections.shoppinglists.http.RearrangeListOfShoppingLists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShoppingListsFragment extends Fragment implements SectionFragment {

    private final ShoppingListsFragment theThis = this;
    private Typeface font;
    public boolean editMode = false;
    public ShoppingListsAdapter shoppingListsAdapter;

    @Data
    public static final class ShoppingListsJson {
        @JsonProperty("items")
        private List<ShoppingListsItemJson> items = new ArrayList<>();
    }

    @Data
    public static final class ShoppingListsItemJson {
        @JsonProperty("id")
        int id;
        @JsonProperty("sequence")
        int sequence;
        @JsonProperty("name")
        String name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_list_of_shopping_lists, container, false);

        Button editButton = (Button) view.findViewById(R.id.listOfShoppingLists_editListOfShoppingListsButton);
        editButton.setTypeface(font);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditModeEnabled(editMode = (!editMode));
            }
        });

        Button createButton = (Button) view.findViewById(R.id.listOfShoppingLists_createShoppingListButton); //TODO: Make button toggleable
        createButton.setTypeface(font);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditModeEnabled(editMode = false);
                showCreateShoppingList();
            }
        });

        initDragSortListView(view);

        return view;
    }

    private void initDragSortListView(View view) {
        final DragSortListView listOfShoppingLists = (DragSortListView) view.findViewById(R.id.listOfShoppingLists);
        DragSortController listOfShoppingListsController = new DragSortController(listOfShoppingLists);
        listOfShoppingListsController.setDragInitMode(DragSortController.ON_DOWN);
        listOfShoppingListsController.setBackgroundColor(Color.TRANSPARENT);

        listOfShoppingLists.setFloatViewManager(listOfShoppingListsController);
        listOfShoppingLists.setOnTouchListener(listOfShoppingListsController);

        listOfShoppingLists.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int which) {
                shoppingListsAdapter.remove(shoppingListsAdapter.getItem(which));
            }
        });

        listOfShoppingLists.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                if (from != to) {
                    ShoppingListsItemJson item = shoppingListsAdapter.getItem(from);
                    shoppingListsAdapter.remove(item);
                    shoppingListsAdapter.insert(item, to);

                    rearrangeListOfShoppingLists();
                }
            }
        });
    }

    private void rearrangeListOfShoppingLists() {
        ShoppingListsJson listOfShoppingLists = new ShoppingListsJson();
        int numShoppingLists = shoppingListsAdapter.getCount();
        for(int i = 0; i < numShoppingLists; i++) {
            ShoppingListsItemJson shoppingList = shoppingListsAdapter.getItem(i);
            shoppingList.sequence = i + 1;
            listOfShoppingLists.getItems().add(shoppingList);
        }
        new RearrangeListOfShoppingLists(theThis, listOfShoppingLists).execute();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        refresh();
    }

    public void showCreateShoppingList() {
        View view = getView();
        if(view == null) {
            log.warn("view == null in showCreateShoppingList()");
            return;
        }
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
            public void onClick(View v) {
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

    public void fillListOfShoppingLists(String shoppingListsJson) {
        View view = getView();
        if(view == null) {
            log.warn("view == null in fillListOfShoppingLists()");
            return;
        }
        ListView shoppingLists = (ListView) getView().findViewById(R.id.listOfShoppingLists);

        ShoppingListsJson inputShoppingLists;
        try {
            inputShoppingLists = new ObjectMapper().readValue(shoppingListsJson, ShoppingListsJson.class);
            this.shoppingListsAdapter = new ShoppingListsAdapter(getActivity(), inputShoppingLists.getItems());
            shoppingLists.setAdapter(this.shoppingListsAdapter);
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
        DragSortListView listOfShoppingLists = (DragSortListView) view.findViewById(R.id.listOfShoppingLists);
        if (editMode) {
            int shoppingListsCount = listOfShoppingLists.getCount();
            for (int i = 0; i < shoppingListsCount; i++) {
                listOfShoppingLists.getChildAt(i).findViewById(R.id.item_listOfShoppingLists_deleteButton).setVisibility(View.VISIBLE);
                listOfShoppingLists.getChildAt(i).findViewById(R.id.item_listOfShoppingLists_selectButton).setVisibility(View.GONE);
            }
            listOfShoppingLists.setDragEnabled(true);
        } else {
            int shoppingListsCount = listOfShoppingLists.getCount();
            for (int i = 0; i < shoppingListsCount; i++) {
                listOfShoppingLists.getChildAt(i).findViewById(R.id.item_listOfShoppingLists_deleteButton).setVisibility(View.GONE);
                listOfShoppingLists.getChildAt(i).findViewById(R.id.item_listOfShoppingLists_selectButton).setVisibility(View.VISIBLE);
            }
            listOfShoppingLists.setDragEnabled(false);
        }
        toggleEditButton(editMode);
    }

    public void toggleEditButton(boolean enable) {
        View view = getView();
        if(view == null) {
            log.warn("view == null in toggleEditButton()");
            return;
        }
        Button editButton = (Button) view.findViewById(R.id.listOfShoppingLists_editListOfShoppingListsButton);
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
        new GetListOfShoppingLists(this).execute();
    }

    @Override
    public void goBack() {
        ((MainActivity) getActivity()).setCurrentlyLiveFragment(FragmentNumbers.HOME);
    }

    public class ShoppingListsAdapter extends ArrayAdapter<ShoppingListsItemJson> {

        public ShoppingListsAdapter(Context context, List<ShoppingListsItemJson> shoppingLists) { //TODO: Check List vs ArrayList here.
            super(context, 0, shoppingLists);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_of_shopping_lists, parent, false);
            }

            final ShoppingListsItemJson shoppingListItemJson = getItem(position);

            final Button itemDeleteButton = (Button) convertView.findViewById(R.id.item_listOfShoppingLists_deleteButton);
            final TextView itemText = (TextView) convertView.findViewById(R.id.item_listOfShoppingLists_text);
            final Button itemSelectButton = (Button) convertView.findViewById(R.id.item_listOfShoppingLists_selectButton);

            itemDeleteButton.setTypeface(font);
            itemSelectButton.setTypeface(font);
            itemText.setHeight(140); //TODO: Set height in a better, more dynamic way.
            itemText.setGravity(Gravity.CENTER_VERTICAL);

            itemText.setText(shoppingListItemJson.getName());

            itemDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisButton) {
                    new DeleteShoppingList(theThis, shoppingListItemJson.id).execute();
                    setEditModeEnabled(true);
                }
            });

            itemSelectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View thisButton) {
                    ((MainActivity) getActivity()).onShoppingListSelected(shoppingListItemJson); //TODO: Reference shoppingListItem in a better way.
                }
            });

            if(editMode) {
                itemDeleteButton.setVisibility(View.VISIBLE);
                itemSelectButton.setVisibility(View.GONE);
            } else {
                itemDeleteButton.setVisibility(View.GONE);
                itemSelectButton.setVisibility(View.VISIBLE);
            }

            return convertView;
        }
    }
}
