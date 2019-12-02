package com.bvrk.mobile.android;

import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bvrk.mobile.android.adapter.TabAdapter;
import com.bvrk.mobile.android.fragment.ClientFragment;
import com.bvrk.mobile.android.fragment.OrderFragment;
import com.bvrk.mobile.android.fragment.VendorFragment;
import com.bvrk.mobile.android.util.db.PocDbhelpher;
import com.bvrk.mobile.android.util.MyExceptionHandler;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.bvrk.mobile.android.util.db.PocDbhelpher.*;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "amarr";

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public int maintab=0;
    private static long back_pressed;
    Toolbar toolbar;
    ImageButton refreshapp,savedata;

    //DatabaseClient databaseClient;
    //private DBManager dbManager;
    PocDbhelpher pocDbhelpher ;

   // public static final String DB_NAME_HD = "com.android.bobby_oct16_db_2" ;

    List<String> locationdata = new ArrayList<>();

    String DirectoryName = "";//Environment.getExternalStorageDirectory() + "/Bobby";
    String outFileName = "";

    private void exportDB(Boolean canshare) {
        try {

            File dbFile = new File(this.getDatabasePath(DB_NAME_HD).getAbsolutePath());
            FileInputStream fis = new FileInputStream(dbFile);

            File direct = new File(DirectoryName);

            if(!direct.exists())
            {
                if(direct.mkdir())
                {
                    //directory is created;
                }
            }

            //String tempfile=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String tempfile=new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            outFileName = DirectoryName + File.separator + "_Bobby"+tempfile;

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            // Close the streams
            output.flush();
            output.close();
            fis.close();
            if(canshare)
               sharedbdata(outFileName);
            MakeToast("Data backup");
        } catch (IOException e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
    }

    public void createBackup(Boolean canshare) {
       SharedPreferences sharedPref = getSharedPreferences("dbBackUp", MODE_PRIVATE);
       SharedPreferences.Editor  editor = sharedPref.edit();

        String dt = sharedPref.getString("dt", new SimpleDateFormat("dd-MM-yy").format(new Date()));

        if (dt != new SimpleDateFormat("dd-MM-yy").format(new Date())) {
            editor.putString("dt", new SimpleDateFormat("dd-MM-yy").format(new Date()));

            editor.commit();
        }

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "BackupDBs");
        boolean success = true;

        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        if (success) {
            DirectoryName = folder.getPath() + File.separator + sharedPref.getString("dt", "");
            folder = new File(DirectoryName);
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                exportDB(canshare);
            }
        } else {
            Toast.makeText(this, "Not create folder", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
         */
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            if (extras.getBoolean("crash", false)) {
                //sendapperror(getIntent().getStringExtra("errorstack"));
                MakeToast("some problem with device");
                Log.d(TAG, "Error info: "+getIntent().getStringExtra("errorstack"));
            }
        }
        //databaseClient = DatabaseClient.getInstance(getApplicationContext());
        //dbManager = new DBManager(this);
        pocDbhelpher = new PocDbhelpher(this);

        /*
        try {
            //if(!dbManager.isDbOpen())
                dbManager.open();
        } catch (SQLException e) {
            Log.d(TAG, "onCreate db erros: "+Log.getStackTraceString(e));
            MakeToast("Unable to open database try again");
            closeapp();
        }
        */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshapp = (ImageButton) findViewById(R.id.refreshapp);
        savedata = (ImageButton) findViewById(R.id.savedata);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        /*
        Cursor cursor = pocDbhelpher.getAlldata("Select * from "+TABLE_ORDERS+" ;");
        if(cursor!=null && cursor.getCount()>0)
            adapter.addFragment(new OrderFragment(), "orders "+cursor.getCount());
        else
        */
        adapter.addFragment(new OrderFragment(), "orders");
        adapter.addFragment(new ClientFragment(), "Client");
        adapter.addFragment(new VendorFragment(), "Vendor");

        //viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i,getApplicationContext()));
        }

        tabLayout.setupWithViewPager(viewPager);

        highLightCurrentTab(0); // for initial selected tab view

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();
                highLightCurrentTab(position); // for tab change
                maintab=position;
                if(position!=0){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        refreshapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addlocationDialog(true);
            }
        });

        savedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBackup(true);
                MakeToast("Data saved");
            }
        });

        loadlocation();
    }

    public static String setprojectdate(String tstampref){
        String maindate="";
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(tstampref);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            maindate = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return maindate;
    }

    public void setordercount(int ordercount){

    }

    private void sharedbdata(String dbfp){

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(dbfp);
        if(fileWithinMyDir.exists()) {
            intentShareFile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            //FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", fileWithinMyDir);

            Log.d(TAG, "sharedbdata: "+URLConnection.guessContentTypeFromName(fileWithinMyDir.getName()));

            intentShareFile.setType("text/plain");
            //intentShareFile.setType("application/db");
            //intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://"+fileWithinMyDir.getAbsolutePath()));

            intentShareFile.putExtra(Intent.EXTRA_STREAM, intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://"+fileWithinMyDir.getAbsolutePath())));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing data...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing dbbck File...");

            startActivity(Intent.createChooser(intentShareFile, "Share db File"));
        }
    }

    void loadlocation(){
        Cursor cursor = pocDbhelpher.getAlldata("Select * from "+TABLE_LOCATION+" ;");
        if(locationdata!=null && locationdata.size()>0)
            locationdata.clear();
        try {
            cursor.moveToFirst();
            String maindata="";
            for (int i = 0; i < cursor.getCount(); i++) {
                maindata=cursor.getString(cursor.getColumnIndex(KEY_TAG_NAME));
                locationdata.add(maindata);
                cursor.moveToNext();
            }
            if(cursor.getCount()==0){
                addlocationDialog(false);
            }
        } catch (Exception e){
            Log.d(TAG, "loadlocation: "+Log.getStackTraceString(e));
        }
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i,getApplicationContext()));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position,getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        if(maintab!=0){
            viewPager.setCurrentItem(0,true);
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                closeapp();
            } else {
                MakeToast("Press once again to exit");
                back_pressed = System.currentTimeMillis();
            }
        }
    }

    private void addlocationDialog(Boolean cstate){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_add_location);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(cstate);
        dialog.show();

        final TextInputLayout   loc_name_lyt = (TextInputLayout)dialog.findViewById(R.id.location_txt_lyt);
        final TextInputEditText loc_name = (TextInputEditText)dialog.findViewById(R.id.location_txt);
        final Button location_add_btn = (Button)dialog.findViewById(R.id.location_add_btn);

        loc_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str= loc_name.getText().toString();
                if(locationdata.contains(str)){
                    loc_name_lyt.setError("already exist");
                } else {
                    loc_name_lyt.setError("");
                }
                Log.d(TAG, "onTextChanged: "+str);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        location_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str= loc_name.getText().toString();
                ContentValues values = new ContentValues();
                values.put(KEY_TAG_NAME,str);
              long res=  pocDbhelpher.addData(TABLE_LOCATION,values);
              if(res>0)
                  MakeToast("Location Data ");
              else
                  MakeToast("Location Data not update ");
                dialog.dismiss();
            }
        });

    }

    void MakeToast(String data){
        Toast toast = new Toast(this);
        View view = LayoutInflater.from(this).inflate(R.layout.z_custom_toast, null);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(data);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    private void closeapp() {
        createBackup(false);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, TAG + " onStart");
    }

    private static final int CODE_AUTHENTICATION_VERIFICATION=198;

    private void checkusersec(){
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        if(km.isKeyguardSecure()) {
            Intent i = km.createConfirmDeviceCredentialIntent("Authentication required", "password");
            startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION);
        }
        else
            MakeToast("No any security setup done by user(pattern or password or pin or fingerprint");
        //Toast.makeText(this, "No any security setup done by user(pattern or password or pin or fingerprint", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==CODE_AUTHENTICATION_VERIFICATION)
        {
            MakeToast("Success: Verified user's identity");
        }
        else
        {
            MakeToast("Failure: Unable to verify user's identity");
            closeapp();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        createBackup(false);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
