package com.example.robert.family.main;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.example.robert.family.R;
import com.example.robert.family.main.home.HomeFragment;
import com.example.robert.family.main.profile.ProfileFragment;
import com.example.robert.family.main.shoppinglist.ShoppingListFragment;
import com.example.robert.family.util.FragmentNumbers;
import com.example.robert.family.main.navigation.NavigationDrawer;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements NavigationDrawer.NavigationDrawerCallbacks {
    private NavigationDrawer navigationDrawer;
    private CharSequence title;
    private Map<Integer, Fragment> currentlyActiveFragments;
    private RefreshableFragment currentlyLiveFragment;

    public String userEmail = ""; //TODO: Create a session for this!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(currentlyActiveFragments.containsKey(number)) {
            fragment = currentlyActiveFragments.get(number);
        } else {
            fragment = numberToFragment(number);
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        currentlyLiveFragment = (RefreshableFragment) fragment;
        onSectionAttached(number);
    }

    private Fragment numberToFragment(int number) {
        Fragment fragment;
        switch (number) {
            default:
            case FragmentNumbers.HOME:
                fragment = new HomeFragment();
                break;
            case FragmentNumbers.SHOPPING_LIST:
                fragment = new ShoppingListFragment();
                break;
            case FragmentNumbers.PROFILE:
                fragment = new ProfileFragment();
                break;
        }
        return fragment;
    }

    private void onSectionAttached(int number) {
        switch (number) {
            case FragmentNumbers.HOME:
                title = getString(R.string.section1);
                break;
            case FragmentNumbers.SHOPPING_LIST:
                title = getString(R.string.section2);
                break;
            case FragmentNumbers.PROFILE:
                title = getString(R.string.fragment_profile);
                restoreActionBar(); //TODO: Don't do it this way. How does it work for navigation drawer fragments?
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
