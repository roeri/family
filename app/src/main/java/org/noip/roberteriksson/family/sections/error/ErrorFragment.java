package org.noip.roberteriksson.family.sections.error;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import org.noip.roberteriksson.family.sections.SectionFragment;

import static java.lang.Thread.sleep;

public class ErrorFragment extends Fragment implements SectionFragment {

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);
        this.view = view;

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        showErrorText(((MainActivity) getActivity()).getErrorText());
        //TODO: Disable the navigation drawer, only allow the user to read the error and then restart the application.
    }

    private void showErrorText(String errorText) {
        TextView errorTextView = (TextView) view.findViewById(R.id.error_text);
        errorTextView.setText(errorText);
        if (BuildConfig.DEBUG) {
            //TODO: Present more error information in debug mode.
        }
    }

    @Override
    public void refresh() {
        //TODO: Implement this.
    }

    @Override
    public void goBack() {
        //TODO: Tell the user that they should press a second time to restart the application.
    }
}