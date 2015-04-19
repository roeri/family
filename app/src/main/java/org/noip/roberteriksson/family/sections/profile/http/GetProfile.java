package org.noip.roberteriksson.family.sections.profile.http;

import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;
import org.noip.roberteriksson.family.sections.profile.ProfileFragment;
import org.noip.roberteriksson.family.util.HttpPoster;
import org.noip.roberteriksson.family.util.Url;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class GetProfile extends AsyncTask<String, Void, String> {

    private final ProfileFragment profileFragment;
    private final String email;

    public GetProfile(ProfileFragment profileFragment, String email) {
        this.profileFragment = profileFragment;
        this.email = email;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            StringEntity entityToSend = new StringEntity(email);
            return HttpPoster.doHttpPost(Url.PROFILE_GET_PROFILE, entityToSend);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "FAILURE";
    }

    @Override
    protected void onPostExecute(String profileJson) {
        if(!profileJson.equals("FAILURE")) {
            try {
                ProfileFragment.ProfileJson profile = new ObjectMapper().readValue(profileJson, ProfileFragment.ProfileJson.class);
                profileFragment.populateProfile(profile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(profileFragment.getActivity(), "ERROR in GetProfile", Toast.LENGTH_SHORT).show();
        }
    }
}
