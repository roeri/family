package com.example.robert.family.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.robert.family.R;

/**
 * Created by robert on 2015-02-23.
 */
public class Section2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section2, container, false);
        return view;
    }

    public void setText(String item) {
        //TextView view = (TextView) getView().findViewById(R.id.section2_text);
        //view.setText(item);
    }
}
