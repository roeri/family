package org.noip.roberteriksson.family.main;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.example.robert.family.R;
import org.noip.roberteriksson.family.session.Session;
import org.noip.roberteriksson.family.main.home.HomeFragment;
import org.noip.roberteriksson.family.main.profile.ProfileFragment;
import org.noip.roberteriksson.family.main.shoppinglist.ShoppingListFragment;
import org.noip.roberteriksson.family.main.shoppinglist.ListOfShoppingListsFragment;
import org.noip.roberteriksson.family.main.shoppinglist.ListOfShoppingListsItemJson;
import org.noip.roberteriksson.family.util.FragmentNumbers;
import org.noip.roberteriksson.family.main.navigation.NavigationDrawer;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements NavigationDrawer.NavigationDrawerCallbacks {
    private NavigationDrawer navigationDrawer;
    private CharSequence title;
    private Map<Integer, RefreshableFragment> currentlyActiveFragments;
    private RefreshableFragment currentlyLiveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Session.getInstance().initiateUserAccount(this);
        currentlyActiveFragments = new HashMap<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawer = (NavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        navigationDrawer.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    public RefreshableFragment getCurrentlyLiveFragment() {
        return this.currentlyLiveFragment; //TODO: Risk for NPE, fix!
    }

    @Override
    public void onNavigationDrawerItemSelected(int number) { //TODO: Maybe alias this to something for external use?
        RefreshableFragment fragment;

        if(currentlyActiveFragments.containsKey(number)) {
            fragment = currentlyActiveFragments.get(number);
        } else {
            fragment = numberToFragment(number);
            currentlyActiveFragments.put(number, fragment);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, (Fragment) fragment)
                .commit();

        currentlyLiveFragment = fragment;
        onSectionAttached(number);
    }

    private RefreshableFragment numberToFragment(int number) {
        RefreshableFragment fragment;
        switch (number) {
            default:
            case FragmentNumbers.HOME:
                fragment = new HomeFragment();
                break;
            case FragmentNumbers.LIST_OF_SHOPPING_LISTS:
                fragment = new ListOfShoppingListsFragment();
                break;
            //Below items are not in the navigation drawer.
            case FragmentNumbers.PROFILE:
                fragment = new ProfileFragment();
                break;
        }
        return fragment;
    }

    private void onSectionAttached(int number) {
        switch (number) {
            case FragmentNumbers.HOME:
                title = getString(R.string.title_home);
                break;
            case FragmentNumbers.LIST_OF_SHOPPING_LISTS:
                title = getString(R.string.title_shoppingLists);
                break;
            case FragmentNumbers.PROFILE:
                title = getString(R.string.fragment_profile);
                break;
        }
        restoreActionBar();
    }

    public void onShoppingListSelected(ListOfShoppingListsItemJson listOfShoppingListsItemJson) {
        ShoppingListFragment shoppingList = new ShoppingListFragment();
        shoppingList.setId(listOfShoppingListsItemJson.getId());
        shoppingList.refresh();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, shoppingList)
                .commit();

        currentlyActiveFragments.put(FragmentNumbers.SHOPPING_LIST, shoppingList);
        currentlyLiveFragment = shoppingList;

        title = listOfShoppingListsItemJson.getName();
        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawer.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "WAOAOAOAOOO", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}