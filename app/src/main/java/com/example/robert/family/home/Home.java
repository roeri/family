package com.example.robert.family.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.robert.family.R;
import com.example.robert.family.util.RefreshableFragment;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by robert on 2015-02-23.
 */
@Slf4j
public class Home extends Fragment implements RefreshableFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section1, container, false);
        return view;
    }

    @Override
    public void refresh() {

    }
}
