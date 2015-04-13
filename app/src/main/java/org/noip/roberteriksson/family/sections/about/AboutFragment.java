package org.noip.roberteriksson.family.sections.about;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.robert.family.R;

import org.noip.roberteriksson.family.sections.RefreshableFragment;

/**
 * Created by robert on 2015-04-13.
 */
public class AboutFragment extends Fragment implements RefreshableFragment {

    private View view;
    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        this.view = view;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //new GetProfile(this, Session.getInstance().getUserEmail()).execute();
    }

    @Override
    public void refresh() {
        //TODO: Implement this.
    }
}