package com.example.robert.hejjj.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robert.hejjj.R;

/**
 * Created by robert on 2015-02-23.
 */
public class Section1 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_section1, container, false);
            return view;
        }

        public void setText(String item) {
            //TextView view = (TextView) getView().findViewById(R.id.detailsText);
            //view.setText(item);
        }
}
