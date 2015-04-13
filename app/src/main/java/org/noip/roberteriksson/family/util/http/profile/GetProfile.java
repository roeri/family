package org.noip.roberteriksson.family.util.http.profile;

import android.os.AsyncTask;
import android.widget.Toast;

import org.noip.roberteriksson.family.fragments.profile.ProfileFragment;
import org.noip.roberteriksson.family.fragments.profile.ProfileJson;
import org.noip.roberteriksson.family.util.http.Url;
import org.noip.roberteriksson.family.util.http.HttpPoster;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-06.
 */
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
                ProfileJson profile = new ObjectMapper().readValue(profileJson, ProfileJson.class);
                profileFragment.populateProfile(profile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(profileFragment.getActivity(), "ERROR in GetProfile", Toast.LENGTH_SHORT).show();
        }
    }
}
