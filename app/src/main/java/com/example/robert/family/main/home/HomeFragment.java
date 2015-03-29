package com.example.robert.family.main.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robert.family.R;
import com.example.robert.family.main.RefreshableFragment;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by robert on 2015-02-23.
 */
@Slf4j
public class HomeFragment extends Fragment implements RefreshableFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        refresh();
    }

    @Override
    public void refresh() {
        Toast.makeText(getActivity(), "REFRESHING", Toast.LENGTH_SHORT).show();
        Calendar today = Calendar.getInstance();
        Calendar dateOfEvent = Calendar.getInstance();

        dateOfEvent.set(2015, 5, 17, 15, 0);

        long todayInMillis = today.getTimeInMillis();
        long dateOfEventInMillis = dateOfEvent.getTimeInMillis();

        int daysLeft = (int) (TimeUnit.MILLISECONDS.toDays(dateOfEventInMillis) - TimeUnit.MILLISECONDS.toDays(todayInMillis)) % 365;

        String daysLeftString = "Nu är det " + daysLeft + " dagar kvar tills vi flyttar! yaaay :)";

        ((TextView) getActivity().findViewById(R.id.home_timeLeftToEvent)).setText(daysLeftString);
    }
}
