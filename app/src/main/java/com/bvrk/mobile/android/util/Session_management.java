package com.bvrk.mobile.android.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bvrk.mobile.android.SplashActivity;

import java.util.HashMap;

public class Session_management {

    private static final String PACKAGE_NAME="com.techkrishna.maxseat.";

    private static final String PREFS_SETUP = PACKAGE_NAME+"SETUP.PREFS";
    private static final String KEY_SETUP = PACKAGE_NAME+"SETUP.KEY";

    private static final String PREFS_USER = PACKAGE_NAME+"USER.PREFS";

    public static final String KEY_SETUP_WMARK = PACKAGE_NAME+"USER.KEY.WMARK";
    public static final String KEY_SETUP_STATUS = PACKAGE_NAME+"USER.KEY.STATUS";
    public static final String KEY_SETUP_ID = PACKAGE_NAME+"USER.KEY.ID";
    public static final String KEY_SETUP_NAME = PACKAGE_NAME+"USER.KEY.NAME";
    public static final String KEY_SETUP_MOBILE = PACKAGE_NAME+"USER.KEY.MOBILE";
    public static final String KEY_SETUP_EMAIL = PACKAGE_NAME+"USER.KEY.EMAIL";
    public static final String KEY_SETUP_PINCODE = PACKAGE_NAME+"USER.KEY.PINCODE";
    public static final String KEY_SETUP_ADDRESS = PACKAGE_NAME+"USER.KEY.ADDRESS";
    public static final String KEY_SETUP_DEVICE_INFO = PACKAGE_NAME+"USER.KEY.DEVICEINFO";
    public static final String KEY_SETUP_DEVICE_KEY = PACKAGE_NAME+"USER.KEY.DEVICEKEY";
    public static final String KEY_SETUP_USER_ACTIVE = PACKAGE_NAME+"USER.KEY.ACCOUNTSTATUS";

    SharedPreferences setup_prefs,user_prefs;
    SharedPreferences.Editor setup_prefs_editor,user_prefs_editor;
    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {
        this.context = context;
        setup_prefs = context.getSharedPreferences(PREFS_SETUP, PRIVATE_MODE);
        setup_prefs_editor=setup_prefs.edit();

        user_prefs = context.getSharedPreferences(PREFS_USER, PRIVATE_MODE);
        user_prefs_editor=user_prefs.edit();
    }

    // setup prefs
    public void addsetup() {
        setup_prefs_editor.putBoolean(KEY_SETUP,true);
        setup_prefs_editor.apply();
    }

    public Boolean getsetup() {
        return setup_prefs.getBoolean(KEY_SETUP,false);
    }

    public void setwmark(String wmark){
        setup_prefs_editor.putString(KEY_SETUP_WMARK,wmark);
        setup_prefs_editor.apply();
    }

    public String getwmark(){
        return setup_prefs.getString(KEY_SETUP_WMARK,"techkrishna");
    }


    public void clearSetup() {
        setup_prefs_editor.clear();
        setup_prefs_editor.commit();
    }

    public Boolean hasuserdata(){
        return user_prefs.getBoolean(KEY_SETUP_STATUS,false);
    }

    public Boolean hasuseractive(){
        return user_prefs.getBoolean(KEY_SETUP_USER_ACTIVE,false);
    }

    public void updateuseractive(Boolean userstate){
        user_prefs_editor.putBoolean(KEY_SETUP_USER_ACTIVE, userstate);
    }

    public void createLoginSession(String userid, String name, String mobile,
                                   String email, String pincode, String address, String device_info, String device_key) {
        user_prefs_editor.putString(KEY_SETUP_ID, userid);
        user_prefs_editor.putString(KEY_SETUP_NAME, name);
        user_prefs_editor.putString(KEY_SETUP_MOBILE, mobile);
        user_prefs_editor.putString(KEY_SETUP_EMAIL, email);
        user_prefs_editor.putString(KEY_SETUP_PINCODE, pincode);
        user_prefs_editor.putString(KEY_SETUP_ADDRESS, address);
        user_prefs_editor.putString(KEY_SETUP_DEVICE_INFO, device_info);
        user_prefs_editor.putString(KEY_SETUP_DEVICE_KEY, device_key);
        user_prefs_editor.putBoolean(KEY_SETUP_USER_ACTIVE, false);
        user_prefs_editor.putBoolean(KEY_SETUP_STATUS, true);
        user_prefs_editor.apply();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_SETUP_NAME, user_prefs.getString(KEY_SETUP_NAME, null));
        user.put(KEY_SETUP_ID, user_prefs.getString(KEY_SETUP_ID, null));
        user.put(KEY_SETUP_EMAIL, user_prefs.getString(KEY_SETUP_EMAIL, null));
        user.put(KEY_SETUP_MOBILE, user_prefs.getString(KEY_SETUP_MOBILE, null));
        user.put(KEY_SETUP_PINCODE, user_prefs.getString(KEY_SETUP_PINCODE, null));
        user.put(KEY_SETUP_ADDRESS, user_prefs.getString(KEY_SETUP_ADDRESS, null));
        user.put(KEY_SETUP_DEVICE_INFO, user_prefs.getString(KEY_SETUP_DEVICE_INFO, null));
        user.put(KEY_SETUP_DEVICE_KEY, user_prefs.getString(KEY_SETUP_DEVICE_KEY, null));
        return user;
    }

    public void logoutSession() {
        clearSetup();
        Intent logout = new Intent(context, SplashActivity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(logout);
    }



}