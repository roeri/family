package com.example.robert.family;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.example.robert.family.home.Home;
import com.example.robert.family.shoppinglist.ShoppingList;
import com.example.robert.family.util.FragmentId;
import com.example.robert.family.util.NavigationDrawer;
import com.example.robert.family.util.RefreshableFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements NavigationDrawer.NavigationDrawerCallbacks {
    private NavigationDrawer navigationDrawer;
    private CharSequence title;
    private Map<Integer, Fragment> currentlyActiveFragments;
    private RefreshableFragment currentlyLiveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentlyActiveFragments = new HashMap<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawer = (NavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        navigationDrawer.setUp(
            R.id.navigation_drawer,
            (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    public RefreshableFragment getCurrentlyLiveFragment() {
        return this.currentlyLiveFragment; //TODO: Risk for NPE, fix!
    }

    @Override
    public void onNavigationDrawerItemSelected(int number) {
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

    public Fragment numberToFragment(int number) {
        Fragment fragment = new Home(); //number == 0
        switch (number) {
            case 1:
                fragment = new ShoppingList();
                break;
            case 2:

                break;
            default:
        }
        return fragment;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                title = getString(R.string.section1);
                break;
            case 1:
                title = getString(R.string.section2);
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
