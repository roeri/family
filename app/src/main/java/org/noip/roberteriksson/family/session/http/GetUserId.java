package org.noip.roberteriksson.family.session.http;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.noip.roberteriksson.family.session.Session;
import org.noip.roberteriksson.family.util.HttpPoster;
import org.noip.roberteriksson.family.util.Url;

import java.io.UnsupportedEncodingException;

public class GetUserId extends AsyncTask<String, Void, String> {

    private final String email;

    public GetUserId(String email) {
        this.email = email;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            StringEntity entityToSend = new StringEntity(email);
            return HttpPoster.doHttpPost(Url.SESSION_GET_USER_ID, entityToSend);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "FAILURE";
    }

    @Override
    protected void onPostExecute(String result) {
        Session session = Session.getInstance();
        if(!result.equals("FAILURE")) {
            try {
                int id = Integer.parseInt(result);
                session.setUserId(id);
            } catch (NumberFormatException e) {
                session.showError("Could not connect to server.");
            }
        } else {
            session.showError("User ID does not exist in database.");
        }
    }
}
