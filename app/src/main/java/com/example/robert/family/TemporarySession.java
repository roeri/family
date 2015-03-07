package com.example.robert.family;

/**
 * Created by robert on 2015-03-07.
 */
public class TemporarySession {
    private static int userId;
    private static String userEmail;
    private static TemporarySession me = null;

    public static TemporarySession getInstance() {
        if(me == null) {
            me = new TemporarySession();
        }
        return me;
    }

    private TemporarySession() {
        //Avoiding multiple instances.
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    public String getUserEmail() {
        return this.userEmail;
    }
}
