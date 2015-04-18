package org.noip.roberteriksson.family.sections.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robert.family.BuildConfig;
import com.example.robert.family.R;

import org.noip.roberteriksson.family.main.MainActivity;
import org.noip.roberteriksson.family.sections.FragmentNumbers;
import org.noip.roberteriksson.family.sections.RefreshableFragment;
import org.noip.roberteriksson.family.sections.SectionFragment;

/**
 * Created by robert on 2015-04-13.
 */
public class AboutFragment extends Fragment implements SectionFragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        this.view = view;

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        showVersion();
    }

    private void showVersion() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            TextView version = (TextView) view.findViewById(R.id.about_version);
            version.setText(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(); //TODO: Handle version problems.
        }
        if(BuildConfig.DEBUG) {
            TextView version = (TextView) view.findViewById(R.id.about_version);
            version.setText(version.getText() + " (debug)");
        }
    }

    @Override
    public void refresh() {
        //TODO: Implement this.
    }

    @Override
    public void goBack() {
        ((MainActivity) getActivity()).setCurrentlyLiveFragment(FragmentNumbers.HOME);
    }
}