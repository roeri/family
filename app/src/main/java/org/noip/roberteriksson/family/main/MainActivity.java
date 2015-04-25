package org.noip.roberteriksson.family.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.robert.family.R;

import org.noip.roberteriksson.family.navigation.NavigationDrawerFragment;
import org.noip.roberteriksson.family.sections.FragmentNumbers;
import org.noip.roberteriksson.family.sections.SectionFragment;
import org.noip.roberteriksson.family.sections.about.AboutFragment;
import org.noip.roberteriksson.family.sections.error.ErrorFragment;
import org.noip.roberteriksson.family.sections.home.HomeFragment;
import org.noip.roberteriksson.family.sections.profile.ProfileFragment;
import org.noip.roberteriksson.family.sections.shoppinglists.ShoppingListFragment;
import org.noip.roberteriksson.family.sections.shoppinglists.ShoppingListsFragment;
import org.noip.roberteriksson.family.session.Session;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment navigationDrawer;
    private CharSequence title;
    private Map<Integer, SectionFragment> currentlyActiveFragments;
    private SectionFragment currentlyLiveFragment; //TODO: CHANGE TO SectionFragment
    private String errorText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Session.getInstance().initiateUserAccount(this);
        currentlyActiveFragments = new HashMap<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawer = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        navigationDrawer.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    public SectionFragment getCurrentlyLiveFragment() {
        return this.currentlyLiveFragment; //TODO: Risk for NPE, fix!
    }

    @Override
    public void onNavigationDrawerItemSelected(int number) {
        setCurrentlyLiveFragment(number);
    }

    @Override
    public void onBackPressed() {
        if(currentlyLiveFragment == null) {
            log.debug("currentlyLiveFragment == null in onBackPressed()");
            return;
        }
        currentlyLiveFragment.goBack();
    }

    public void setCurrentlyLiveFragment(int number) {
        SectionFragment fragment;

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

    private SectionFragment numberToFragment(int number) {
        SectionFragment fragment;
        switch (number) {
            default:
            case FragmentNumbers.HOME:
                fragment = new HomeFragment();
                break;
            case FragmentNumbers.SHOPPING_LISTS:
                fragment = new ShoppingListsFragment();
                break;
            //Below items are not in the navigation drawer.
            case FragmentNumbers.ERROR:
                fragment = new ErrorFragment();
                break;
            case FragmentNumbers.PROFILE:
                fragment = new ProfileFragment();
                break;
            case FragmentNumbers.ABOUT:
                fragment = new AboutFragment();
                break;
        }
        return fragment;
    }

    private void onSectionAttached(int number) {
        switch (number) {
            case FragmentNumbers.HOME:
                title = getString(R.string.title_home);
                break;
            case FragmentNumbers.SHOPPING_LISTS:
                title = getString(R.string.title_shoppingLists);
                break;
            case FragmentNumbers.PROFILE:
                title = getString(R.string.fragment_profile);
                break;
            case FragmentNumbers.ABOUT:
                title = getString(R.string.fragment_about);
                break;
        }
        restoreActionBar();
    }

    public void onShoppingListSelected(ShoppingListsFragment.ShoppingListsItemJson shoppingListsItemJson) {
        ShoppingListFragment shoppingList = new ShoppingListFragment();
        shoppingList.setId(shoppingListsItemJson.getId());
        shoppingList.refresh();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, shoppingList)
                .commit();

        currentlyActiveFragments.put(FragmentNumbers.SHOPPING_LIST, shoppingList);
        currentlyLiveFragment = shoppingList;

        title = shoppingListsItemJson.getName();
        restoreActionBar();

        Session.getInstance().setWidgetShoppingListId(shoppingListsItemJson.getId());
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
            Toast.makeText(this, "Settings? Nope.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showError(String errorText) {
        this.errorText = errorText;
        setCurrentlyLiveFragment(FragmentNumbers.ERROR);
    }

    public String getErrorText() {
        if(errorText.equals("")) {
            errorText = "Critical error, please restart the application";
        }
        return errorText;
    }
}
