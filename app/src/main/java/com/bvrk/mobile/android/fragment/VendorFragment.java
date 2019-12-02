package com.bvrk.mobile.android.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bvrk.mobile.android.MainActivity;
import com.bvrk.mobile.android.R;
import com.bvrk.mobile.android.adapter.adapter_orderlist;
import com.bvrk.mobile.android.adapter.adapter_userlist;
import com.bvrk.mobile.android.pojo.orderlist_pojo;
import com.bvrk.mobile.android.pojo.userinfo;
import com.bvrk.mobile.android.pojo.userlist_pojo;
import com.bvrk.mobile.android.util.db.PocDbhelpher;
import com.bvrk.mobile.android.util.EnglishNumberToWords;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.bvrk.mobile.android.util.db.PocDbhelpher.*;


public class VendorFragment extends Fragment {
    public static final String TAG = "amarr";

    View parentview;

    TextView vendor_usercount;
    ImageButton vendor_adduser,vendor_reload;

    RecyclerView vendor_rv;

    // add new vendor
    View adduserlayout;
    private Button updateuser;
    private AppCompatSpinner user_location_spinner;
    private TextInputLayout name_txt_lyt,mobile_txt_lyt,username_txt_lyt,address_txt_lyt;
    private TextInputEditText name_txt,mobile_txt,username_txt,address_txt;

    // search vendors
    TextView vendor_list;
    LinearLayout vendor_ordersearch_lyt;
    Spinner vendor_ordersearch_sel;
    RecyclerView vendor_ordersearch_rv;

    adapter_orderlist adapterOrderlist;
    private List<orderlist_pojo> ordermodellist= new ArrayList<>();

    Integer[] vendors_id;
    Integer vendor_index;
    String[] vendors_name;
    //

    Boolean cangoback=true;

    List<String> locationdata= new ArrayList<>();
    List<String> usernames = new ArrayList<>();

    adapter_userlist adapterUserlist;
    List<userlist_pojo> usermodellist= new ArrayList<>();

    //private DBManager dbManager;
    PocDbhelpher pocDbhelpher;

    public VendorFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pocDbhelpher = new PocDbhelpher(getContext());
    }

    private void inituseradd(){
        adduserlayout = parentview.findViewById(R.id.vendor_adduserlayout);
        updateuser = (Button ) adduserlayout.findViewById(R.id.updateuser);
        name_txt_lyt     = (TextInputLayout) adduserlayout.findViewById(R.id.name_txt_lyt);
        mobile_txt_lyt   = (TextInputLayout) adduserlayout.findViewById(R.id.mobile_txt_lyt);
        username_txt_lyt = (TextInputLayout) adduserlayout.findViewById(R.id.username_txt_lyt);
        address_txt_lyt  = (TextInputLayout) adduserlayout.findViewById(R.id.address_txt_lyt);

        name_txt     = (TextInputEditText) adduserlayout.findViewById(R.id.name_txt);
        mobile_txt   = (TextInputEditText) adduserlayout.findViewById(R.id.mobile_txt);
        username_txt = (TextInputEditText) adduserlayout.findViewById(R.id.username_txt);
        address_txt  = (TextInputEditText) adduserlayout.findViewById(R.id.address_txt);

        user_location_spinner = (AppCompatSpinner) adduserlayout.findViewById(R.id.user_location_spinner);

        updateuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String s1:usernames){
                    if(s1.equalsIgnoreCase(username_txt.getText().toString())){
                        cangoback=false;
                    } else {
                        cangoback=true;
                    }
                }
                if(cangoback) {
                    userinfo event = new userinfo();
                    event.usertype = "Add New Vendor";
                    event.name = name_txt.getText().toString();
                    event.mobile = mobile_txt.getText().toString();
                    event.username = username_txt.getText().toString();
                    event.address = address_txt.getText().toString();
                    event.location = user_location_spinner.getSelectedItem().toString();
                    addDataResult(event);
                } else {

                }
            }
        });

        mobile_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s  = mobile_txt.getText().toString();
                if(s.startsWith("0")){
                    mobile_txt.setText(s.substring(1));
                    cangoback=false;
                } else {
                    cangoback=true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        username_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s  = username_txt.getText().toString();
                for (String s1:usernames){
                    if(s1.equalsIgnoreCase(s)){
                        mobile_txt_lyt.setHint("username already exist");
                        cangoback=false;
                    } else {
                        cangoback=true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentview = inflater.inflate(R.layout.fragment_vendor, container, false);
        inituseradd();
        vendor_usercount = (TextView) parentview.findViewById(R.id.vendor_usercount);
        vendor_adduser = (ImageButton) parentview.findViewById(R.id.vendor_adduser);
        vendor_reload = (ImageButton) parentview.findViewById(R.id.vendor_reload);
        vendor_rv = parentview.findViewById(R.id.vendor_rv);
        vendor_rv.setHasFixedSize(true);
        vendor_rv.setNestedScrollingEnabled(true);
        vendor_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        // search vendor orders
        vendor_list = (TextView) parentview.findViewById(R.id.vendor_list);
        vendor_ordersearch_lyt = (LinearLayout) parentview.findViewById(R.id.vendor_ordersearch_lyt);
        vendor_ordersearch_sel = (Spinner) parentview.findViewById(R.id.vendor_ordersearch_sel);
        vendor_ordersearch_rv  = (RecyclerView) parentview.findViewById(R.id.vendor_ordersearch_rv);
        vendor_ordersearch_rv.setHasFixedSize(true);
        vendor_ordersearch_rv.setNestedScrollingEnabled(true);
        vendor_ordersearch_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        vendor_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendor_rv.setVisibility(View.GONE);
                if(vendor_ordersearch_lyt.getVisibility()==View.VISIBLE){
                    vendor_ordersearch_lyt.setVisibility(View.GONE);
                } else
                    vendor_ordersearch_lyt.setVisibility(View.VISIBLE);
                //MakeToast("work inprogress");
            }
        });

        vendor_ordersearch_sel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vendor_index=position;
                //parent.getItemAtPosition(position).toString();
                if(!vendors_name[position].equalsIgnoreCase("Select Vendor")) {
                    setorders();
                } else {
                    vendor_ordersearch_rv.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        //

        vendor_usercount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendor_ordersearch_lyt.setVisibility(View.GONE);
                adduserlayout.setVisibility(View.GONE);
                if(vendor_rv.getVisibility()==View.VISIBLE){
                    vendor_rv.setVisibility(View.GONE);
                } else if(vendor_rv.getVisibility()==View.GONE){
                    vendor_rv.setVisibility(View.VISIBLE);
                }
            }
        });

        vendor_adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendor_rv.setVisibility(View.GONE);
                //setusers();
                if(adduserlayout.getVisibility()== View.GONE) {
                    adduserlayout.setVisibility(View.VISIBLE);
                    setlocationspinner();
                } else {
                    setlocationspinner();
                    adduserlayout.setVisibility(View.GONE);
                }
            }
        });
        vendor_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vendor_rv.setVisibility(View.GONE);
                MakeToast("data reloaded");
                setusers();
            }
        });
        loadlocation();
        setusers();
        return parentview;
    }

    private void  setorders(){
        if(ordermodellist!=null && ordermodellist.size()>0){
            ordermodellist.clear();
        }
        Integer clientref = vendors_id[vendor_index];//clients_id[client_sel_spinner.getSelectedItemPosition()];
        BigDecimal clientbigDecimal = null;
        String[] cdata= new String[] { KEY_ID,KEY_ORDER_TYPE,KEY_ORDER_CLIENTREF,KEY_ORDER_VENDORREF,KEY_ORDER_DATA,
                KEY_ORDER_VENDORAMT ,KEY_ORDER_AMOUNT,KEY_ORDER_CREDIT,KEY_ORDER_PAID,KEY_UPDATED_AT,KEY_STATUS};
        String[] refname= new String[] { String.valueOf(clientref) };
        String wherestring = KEY_ORDER_VENDORREF + " = ?";
        Cursor cursor = pocDbhelpher.SearchData(TABLE_ORDERS,cdata,refname,wherestring,"id DESC");
        //Cursor cursor = pocDbhelpher.getAlldata("Select * from "+TABLE_ORDERS+" order by id desc;");
        try {
            if(cursor!=null) {
                cursor.moveToFirst();
                String ID = "", ORDER_TYPE = "", ORDER_CLIENTREF = "", ORDER_VENDORREF = "";
                String ORDER_DATA = "", ORDER_VENDORAMT = "", ORDER_AMOUNT = "",ORDER_CREDIT="";
                String ORDER_PAID = "", UPDATED_AT = "",created_date = "";
                Integer STATUS = 0;
                for (int i = 0; i < cursor.getCount(); i++) {
                    ID = "" + cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    ORDER_TYPE = "" + cursor.getString(cursor.getColumnIndex(KEY_ORDER_TYPE));
                    ORDER_CLIENTREF = "" + cursor.getInt(cursor.getColumnIndex(KEY_ORDER_CLIENTREF));
                    ORDER_VENDORREF = "" + cursor.getInt(cursor.getColumnIndex(KEY_ORDER_VENDORREF));
                    ORDER_DATA = "" + cursor.getString(cursor.getColumnIndex(KEY_ORDER_DATA));
                    ORDER_VENDORAMT = "" + cursor.getDouble(cursor.getColumnIndex(KEY_ORDER_VENDORAMT));
                    Double tempamt=Double.parseDouble(String.valueOf(cursor.getDouble(cursor.getColumnIndex(KEY_ORDER_AMOUNT))));
                    ORDER_AMOUNT = "" +tempamt;
                    ORDER_CREDIT = "" + cursor.getDouble(cursor.getColumnIndex(KEY_ORDER_CREDIT));
                    ORDER_PAID = "" + cursor.getDouble(cursor.getColumnIndex(KEY_ORDER_PAID));
                    UPDATED_AT = "" + cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT));
                    created_date = "" + cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT));

                    STATUS = Integer.getInteger(cursor.getString(cursor.getColumnIndex(KEY_STATUS)),1);
                    String order_text[]={"","pending","closed"};
                    Log.d(TAG, "setorders: order status "+STATUS);

                    String ordermaindata="";
                    Log.d(TAG, "setorders: "+ID+" "+ORDER_DATA);

                    try {
                        JSONArray passArray = new JSONArray(ORDER_DATA);
                        //for (int passArrayi = 0; i < passArray.length(); passArrayi++) {
                        JSONObject child = passArray.getJSONObject(0);
                        ordermaindata = ordermaindata+"labourchrgs " + child.optString("labourchrgs","0");
                        ordermaindata = ordermaindata+" , commision "  + child.optString("commision","0");
                        ordermaindata = ordermaindata+"tars " + child.optString("tars","0");
                        ordermaindata = ordermaindata + " , weight " +  child.optString("weight","0") + " ,";
                        // }
                    } catch (Exception e){
                        Log.d(TAG, "setorders: "+Log.getStackTraceString(e));
                        ordermaindata="";
                    }

                    orderlist_pojo pojo = new orderlist_pojo(
                            ID,
                            created_date,
                            ORDER_AMOUNT,
                            ORDER_VENDORREF,
                            ORDER_VENDORAMT,
                            ORDER_CLIENTREF,
                            ORDER_TYPE,
                            order_text[STATUS],
                            "Vendor Amt: "+ EnglishNumberToWords.convertToIndianCurrency(ORDER_VENDORAMT) + " paid : " + ORDER_PAID + " status:" + order_text[STATUS] + " Extra Data : " + ordermaindata
                            ,ORDER_CREDIT,
                            MainActivity.setprojectdate(UPDATED_AT)
                    );
                    ordermodellist.add(pojo);

                    cursor.moveToNext();
                }
            }
            if (ordermodellist!=null && ordermodellist.size()>0){
                adapterOrderlist = new adapter_orderlist(ordermodellist,getContext());
                vendor_ordersearch_rv.setVisibility(View.VISIBLE);
                vendor_ordersearch_rv.setAdapter(adapterOrderlist);
                adapterOrderlist.notifyDataSetChanged();
            } else {
                vendor_ordersearch_rv.setVisibility(View.GONE);
            }

        } catch (Exception e){
            Log.d(TAG, "setorders: "+Log.getStackTraceString(e));
        }
    }

    void setusers(){
        if(usermodellist!=null && usermodellist.size()>0){
            usermodellist.clear();
        }

        if(usernames!=null && usernames.size()>0)
            usernames.clear();

            String rawQuery = "SELECT vendorinfo.*,vendorwallet.cw_bal   FROM " + TABLE_VENDOR_INFO + " INNER JOIN " + TABLE_VENDOR_WALLET
                + " ON " + TABLE_VENDOR_INFO+"."+KEY_ID + " = " + TABLE_VENDOR_WALLET+"."+KEY_CWALLET_CREF + " ;";
                //+ " WHERE " + RefuelTable.ID  + " = " + id;

        Log.d(TAG, "setusers: rawquery "+rawQuery);

        Cursor cursor_client = pocDbhelpher.getAlldata(rawQuery);
        //Cursor cursor_client = pocDbhelpher.getAlldata("Select * from "+TABLE_VENDOR_INFO+" order by id desc;");
        try {
                if (cursor_client != null) {
                    cursor_client.moveToFirst();
                }

            String id = "", client_type = "client", client_name = "", client_phone = "";
            String client_location = "", client_username = "", client_address = "";
            String client_orders = "0", client_wallet = "0";

            vendors_id = new Integer[cursor_client.getCount()+1];
            vendors_name = new String[cursor_client.getCount()+1];
            vendors_name[0] = "Select Vendor";
            vendors_id[0] =-1;

            for (int i = 0; i < cursor_client.getCount(); i++) {
                id = "" + cursor_client.getInt(cursor_client.getColumnIndex(KEY_ID));
                client_username = "" + cursor_client.getString(cursor_client.getColumnIndex(KEY_U_UNAME));
                client_name = "" + cursor_client.getString(cursor_client.getColumnIndex(KEY_U_NAME));
                client_phone = "" + cursor_client.getString(cursor_client.getColumnIndex(KEY_U_PHONE));
                client_location = "" + cursor_client.getString(cursor_client.getColumnIndex(KEY_U_LOCATION));
                client_address = "" + cursor_client.getString(cursor_client.getColumnIndex(KEY_U_ADDRESS));
                client_wallet = ""+cursor_client.getString(cursor_client.getColumnIndex(KEY_CWALLET_BAL));


                usernames.add(client_username);

                vendors_id[i+1] = cursor_client.getInt(cursor_client.getColumnIndex(KEY_ID));
                vendors_name[i+1] = cursor_client.getString(cursor_client.getColumnIndex(KEY_U_UNAME));

                    userlist_pojo pojo = new userlist_pojo(
                            id,
                            client_name,
                            client_phone,
                            client_username,
                            client_location,
                            client_address,
                            client_type,
                            client_wallet,
                            client_orders
                    );
                Log.d(TAG, "setusers: pojo "+pojo.toString());
                usermodellist.add(pojo);
                cursor_client.moveToNext();
            }
            cursor_client.close();

           // cursor_client=pocDbhelpher.getorderbyuser(KEY_ORDER_VENDORREF);

            if (usermodellist!=null && usermodellist.size()>0){
                vendor_rv.setVisibility(View.VISIBLE);
                vendor_usercount.setText("Vendors : "+usermodellist.size());
                adapterUserlist = new adapter_userlist("vendor",usermodellist,getContext());
                vendor_rv.setAdapter(adapterUserlist);
                adapterUserlist.notifyDataSetChanged();
                ArrayAdapter<String> array = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, vendors_name);
                array.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                vendor_ordersearch_sel.setAdapter(array);
                vendor_ordersearch_sel.setSelection(0);
            } else {
                ArrayAdapter<String> array = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, vendors_name);
                array.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                vendor_ordersearch_sel.setAdapter(array);
                vendor_ordersearch_sel.setSelection(0);
                vendor_usercount.setText("Vendors : 0");
                vendor_rv.setVisibility(View.GONE);
            }
        } catch (Exception e){
            Log.d(TAG, "setusers: "+Log.getStackTraceString(e));
        }
    }

    double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {
                Log.d(TAG, "ParseDouble: "+Log.getStackTraceString(e));
                return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        }
        else return 0;
    }

    void loadlocation(){
        Cursor cursor = pocDbhelpher.getAlldata("Select * from "+TABLE_LOCATION+" ;");
        if(locationdata!=null && locationdata.size()>0)
            locationdata.clear();
        try {
            if(cursor!=null) {
                cursor.moveToFirst();
                String maindata = "";
                for (int i = 0; i < cursor.getCount(); i++) {
                    maindata = cursor.getString(cursor.getColumnIndex(KEY_TAG_NAME));
                    locationdata.add(maindata);
                    cursor.moveToNext();
                }

                if (cursor.getCount() == 0) {
                    MakeToast("No data");
                }
            }
        } catch (Exception e){
            Log.d(TAG, "loadlocation: "+Log.getStackTraceString(e));
        }
    }

    void setlocationspinner(){
        if(locationdata!=null && locationdata.size()>0){
            ArrayAdapter<String> array = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, locationdata);
            array.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            user_location_spinner.setAdapter(array);
            user_location_spinner.setSelection(0);
        }
    }

    void addDataResult(userinfo event){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_U_NAME, event.name);
        contentValues.put(KEY_U_UNAME, event.username);
        contentValues.put(KEY_U_PHONE, event.mobile);
        contentValues.put(KEY_U_ADDRESS, event.address);
        contentValues.put(KEY_U_LOCATION, event.location);
        if (event.usertype.equalsIgnoreCase("Add New Client")) {
            long res=  pocDbhelpher.addData(TABLE_CLIENT_INFO,contentValues);
            if(res>0)
                MakeToast("Client Data added");
            else
                MakeToast("Client Data not update ");
            setusers();
        } else if (event.usertype.equalsIgnoreCase("Add New Vendor")) {
            long res=  pocDbhelpher.addData(TABLE_VENDOR_INFO,contentValues);
            if(res>0)
                MakeToast("Vendor Data added");
            else
                MakeToast("Vendor Data not update ");
            setusers();
        }
        else
            MakeToast("Please add location ");
    }

    void MakeToast(String data){
        Toast toast = new Toast(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.z_custom_toast, null);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(data);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    private void closeapp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        System.exit(0);
    }
}
