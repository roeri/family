package com.example.robert.family.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.robert.family.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by robert on 2015-02-23.
 */
public class Section1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section1, container, false);
        //new HttpAsyncTask().execute();
        Button button = (Button) view.findViewById(R.id.section1_button);
        button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                new HttpAsyncTask().execute();
             }
        });
        return view;
    }

    public void setText(String item) {
        TextView view = (TextView) getView().findViewById(R.id.section1_text);
        view.setText(item);
    }

    public String testDb2() {
        String endResult = "FAILED?";
        InputStream is;

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://roberteriksson.no-ip.org/family/shoppinglist.php");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                endResult = sb.toString();
            }catch(Exception e){
                System.out.println("ERROR1: " + e.getMessage());
            }
        }catch(Exception e){
            System.out.println("ERROR2");
        }
        return endResult;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return testDb2();
        }
        @Override
        protected void onPostExecute(String result) {
            setText(result);
        }
    }
}
