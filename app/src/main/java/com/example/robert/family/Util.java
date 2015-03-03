package com.example.robert.family;

import android.graphics.Typeface;

import com.example.robert.family.fragments.ShoppingList;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by robert on 2015-03-01.
 */
public class Util {

    public static ShoppingList jsonToShoppingList(String jsonString) throws IOException {
        return new ObjectMapper().readValue(jsonString, ShoppingList.class);
    }

    public static String shoppingListToJson(ShoppingList shoppingList) throws IOException {
        return new ObjectMapper().writeValueAsString(shoppingList);
    }

    public static String doHttpPost(String postUrl, boolean wantResult, AbstractHttpEntity entityToSend) {
        String result = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postUrl);
        try{
            if(entityToSend != null) {
                httppost.setEntity(entityToSend);
            }
            HttpResponse httpResponse = httpclient.execute(httppost);
            if(wantResult) {
                InputStream inputStream = httpResponse.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line); //TODO: Figure out if "sb.append(line + '\n')" is better for something, shouldn't be for json.
                }
                inputStream.close();
                result = sb.toString();
            }
        }catch(Exception e){
            System.out.println("ERROR: " + e.getMessage());
        }
        return result;
    }
}
