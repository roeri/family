package com.example.robert.family.util.httptasks;

import android.os.AsyncTask;

import com.example.robert.family.main.profile.ProfileFragment;
import com.example.robert.family.main.profile.ProfileJson;
import com.example.robert.family.util.Url;
import com.example.robert.family.util.HttpPoster;
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
            return HttpPoster.doHttpPost(Url.getProfileUrl, true, entityToSend);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String profileJson) {
        try {
            ProfileJson profile = new ObjectMapper().readValue(profileJson, ProfileJson.class);
            profileFragment.populateProfile(profile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
