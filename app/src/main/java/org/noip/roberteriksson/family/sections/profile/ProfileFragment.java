package org.noip.roberteriksson.family.sections.profile;

import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robert.family.R;

import org.noip.roberteriksson.family.main.MainActivity;
import org.noip.roberteriksson.family.sections.FragmentNumbers;
import org.noip.roberteriksson.family.sections.SectionFragment;
import org.noip.roberteriksson.family.session.Session;
import org.noip.roberteriksson.family.sections.RefreshableFragment;
import org.noip.roberteriksson.family.sections.profile.http.GetProfile;

/**
 * Created by robert on 2015-03-06.
 */
public class ProfileFragment extends Fragment implements SectionFragment {

    private View view;
    private Typeface font;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        this.view = view;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        new GetProfile(this, Session.getInstance().getUserEmail()).execute();
    }

    @Override
    public void refresh() {
        //TODO: Implement this.
    }

    @Override
    public void goBack() {
        ((MainActivity) getActivity()).setCurrentlyLiveFragment(FragmentNumbers.HOME);
    }

    public void populateProfile(ProfileJson profileJson) {
        TextView profileName = (TextView) view.findViewById(R.id.profile_name);
        profileName.setText(profileJson.getName());
    }
}
