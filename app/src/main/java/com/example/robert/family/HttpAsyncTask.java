package com.example.robert.family;

import android.os.AsyncTask;

import com.example.robert.family.fragments.Section2;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

/**
 * Created by robert on 2015-03-01.
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String> {

    private final String shoppingListUrl = "http://roberteriksson.no-ip.org/family/shoppinglist.php";
    private final String createItemUrl = "http://roberteriksson.no-ip.org/family/createitem.php";
    private final String deleteItemUrl = "http://roberteriksson.no-ip.org/family/deleteitem.php";

    private final String createUserUrl = "http://roberteriksson.no-ip.org/family/createuser.php";

    private final LoginActivity loginActivity;
    private final Section2 section2Fragment;

    private final HttpTask whatToDo;
    private final String param1;
    private final String param2;

    public HttpAsyncTask(LoginActivity loginActivity, Section2 section2Fragment, HttpTask whatToDo, String param1, String param2) {
        this.loginActivity = loginActivity;
        this.section2Fragment = section2Fragment;
        this.whatToDo = whatToDo;
        this.param1 = param1;
        this.param2 = param2;
    }

    @Override
    protected String doInBackground(String... urls) {
        switch(whatToDo) {
            case GET_SHOPPING_LIST:
                return Util.doHttpPost(shoppingListUrl, true, null);
            case CREATE_SHOPPING_LIST_ITEM:
                try {
                    StringEntity entityToSend = new StringEntity(param1);
                    Util.doHttpPost(createItemUrl, false, entityToSend);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case DELETE_SHOPPING_LIST_ITEM:
                try {
                    StringEntity entityToSend = new StringEntity(param1);
                    Util.doHttpPost(deleteItemUrl, false, entityToSend);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case CREATE_USER:
                try {
                    StringEntity entityToSend = new StringEntity(param1 + param2); //TODO: Make json object
                    return Util.doHttpPost(createUserUrl, true, entityToSend);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;
            default:
        }
        return "";
    }
    @Override
    protected void onPostExecute(String result) {
        switch(whatToDo) {
            case GET_SHOPPING_LIST:
                section2Fragment.fillShoppingList(result);
                break;
            case CREATE_SHOPPING_LIST_ITEM:
                new HttpAsyncTask(null, section2Fragment, HttpTask.GET_SHOPPING_LIST, "", "").execute();
                break;
            case DELETE_SHOPPING_LIST_ITEM:
                new HttpAsyncTask(null, section2Fragment, HttpTask.GET_SHOPPING_LIST, "", "").execute();
                break;
            case CREATE_USER:
                System.out.println(result);
                break;
            default:
        }
    }
}