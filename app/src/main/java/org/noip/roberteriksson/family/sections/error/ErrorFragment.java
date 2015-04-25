package org.noip.roberteriksson.family.sections.error;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robert.family.BuildConfig;
import com.example.robert.family.R;

import org.noip.roberteriksson.family.main.MainActivity;
import org.noip.roberteriksson.family.sections.SectionFragment;

import static java.lang.Thread.sleep;

public class ErrorFragment extends Fragment implements SectionFragment {

    private View view;
    private boolean triedToGoBackAlready = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error, container, false);
        this.view = view;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity mainActivity = ((MainActivity) getActivity());
        mainActivity.disableOptionsMenu();
        showErrorText(mainActivity.getErrorText());
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

    }

    @Override
    public void goBack() {
        if(triedToGoBackAlready) {
            getActivity().finish();
            System.exit(0);
        } else {
            Toast.makeText(getActivity(), "Press back button again to exit the application", Toast.LENGTH_SHORT).show();
            triedToGoBackAlready = true;
        }
    }
}