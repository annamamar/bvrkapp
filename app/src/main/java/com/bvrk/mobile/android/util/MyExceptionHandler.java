package com.bvrk.mobile.android.util;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.bvrk.mobile.android.Appcontroller;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "amarr";
    private Activity activity;

    public MyExceptionHandler(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Exception ex = new Exception(e);
        //Appcontroller.getInstance().trackException(ex);
        Log.d(TAG, "uncaughtException: "+getStackTrace(e));
        /*
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.putExtra("crash", true);
        //intent.putExtra("errordata",ex.toString()+e.getStackTrace());
        intent.putExtra("errorstack",getStackTrace(e));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(Appcontroller.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager mgr = (AlarmManager) Appcontroller.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, pendingIntent);
        activity.finish();
        System.exit(2);
        */

        /*
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.putExtra("crash", true);
        intent.putExtra("errordata",ex.toString()+e.getStackTrace());
        intent.putExtra("errorstack",getStackTrace(e));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(Appcontroller.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) Appcontroller.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, pendingIntent);
        activity.finish();
        System.exit(2);
        */
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        String errordata="\nDeviceInfo\n"
                + Build.MANUFACTURER+":"+ Build.BRAND+":"+ Build.PRODUCT+":"+
                Build.MODEL+
                " : version " + Build.VERSION.BASE_OS;
        String s="Debug-infos:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: "+ Build.VERSION.RELEASE + "("+ Build.VERSION.SDK_INT+")";
        s += "\n Device: " + Build.DEVICE;
        s += "\n Model (and Product): " + Build.MODEL + " ("+ Build.PRODUCT + ")";
        s += "\n MANUFACTURER: " + Build.MANUFACTURER + " Brand ("+ Build.BRAND + ")";
        return sw.getBuffer().toString()+s;
    }

    void MakeToast(String str){
        Toast.makeText(Appcontroller.getInstance().getBaseContext(),str, Toast.LENGTH_LONG).show();
    }
}
