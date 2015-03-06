package com.example.robert.family;

import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.robert.family.util.RefreshableFragment;
import com.example.robert.family.util.httptasks.GetShoppingList;

/**
 * Created by robert on 2015-03-06.
 */
public class Profile extends Fragment implements RefreshableFragment {

    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //new GetShoppingList(this).execute();
    }


    @Override
    public void refresh() {

    }
}
