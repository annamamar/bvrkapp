package com.bvrk.mobile.android.fragment;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bvrk.mobile.android.MainActivity;
import com.bvrk.mobile.android.R;
import com.bvrk.mobile.android.adapter.adapter_orderlist;
import com.bvrk.mobile.android.pojo.orderlist_pojo;
import com.bvrk.mobile.android.pojo.userinfo;
import com.bvrk.mobile.android.util.db.PocDbhelpher;
import com.bvrk.mobile.android.util.EnglishNumberToWords;
import com.bvrk.mobile.android.util.RecyclerTouchListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.bvrk.mobile.android.config.BaseURL.ORDER_USER_ADMIN;
import static com.bvrk.mobile.android.util.db.PocDbhelpher.*;

public class OrderFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final String TAG = "amarr";

    // DatabaseClient.getInstance(getContext()).getDb();
    //DatabaseClient databaseClient;

    View parentview;

    LinearLayout bulk_order_lyt,order_create;

    TextView order_date_text,bulk_order_caption,showorder ,orders_count,order_credit;

    Spinner order_type_spinner,client_sel_spinner,vendor_sel_spinner;

    TextInputLayout tars_txt_lyt,weight_txt_lyt,vendor_amt_txt_lyt;

    TextInputLayout labourchrg_txt_lyt,commisn_txt_lyt;

    TextInputEditText tars_txt,weight_txt,vendor_amt_txt,labourchrg_txt,commisn_txt;

    Button order_add_btn , orders_filter ,orders_reload;

    RecyclerView order_list_rv;

    adapter_orderlist adapterOrderlist;
    private List<orderlist_pojo> ordermodellist= new ArrayList<>();

    String order_type;

    Integer[] vendors_id,clients_id;

    Integer vendor_index,client_index;

    String[] vendors_name,clients_name,location;

    PocDbhelpher  pocDbhelpher ;

    String live_orderid,live_ordertype,live_ordervendoramt,live_orderclientref,live_ordervendorref;

    public static final int DIALOG_QUEST_CODE = 300;
    private String live_orderamt;

    public OrderFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pocDbhelpher = new PocDbhelpher(getContext());
    }

    private void closeapp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        System.exit(0);
    }


    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    private void initlyts(){
        bulk_order_lyt = (LinearLayout) parentview.findViewById(R.id.bulk_order_lyt);
        order_create   = (LinearLayout) parentview.findViewById(R.id.order_create);

        showorder =(TextView) parentview.findViewById(R.id.showorder);

        order_list_rv = parentview.findViewById(R.id.order_list);
        order_list_rv.setHasFixedSize(true);
        order_list_rv.setNestedScrollingEnabled(true);
        order_list_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        order_date_text    = (TextView) parentview.findViewById(R.id.order_date_text);
        bulk_order_caption = (TextView) parentview.findViewById(R.id.bulk_order_caption);
        order_credit = (TextView) parentview.findViewById(R.id.order_credit);

        order_type_spinner = (Spinner) parentview.findViewById(R.id.order_type_spinner);
        client_sel_spinner = (Spinner) parentview.findViewById(R.id.client_sel_spinner);
        vendor_sel_spinner = (Spinner) parentview.findViewById(R.id.vendor_sel_spinner);

        tars_txt_lyt   = (TextInputLayout) parentview.findViewById(R.id.tars_txt_lyt);
        weight_txt_lyt = (TextInputLayout) parentview.findViewById(R.id.weight_txt_lyt);
        vendor_amt_txt_lyt = (TextInputLayout) parentview.findViewById(R.id.tars_txt_lyt);

        labourchrg_txt_lyt = (TextInputLayout) parentview.findViewById(R.id.labourchrg_txt_lyt);
        commisn_txt_lyt    = (TextInputLayout) parentview.findViewById(R.id.commisn_txt_lyt);

        tars_txt   = (TextInputEditText) parentview.findViewById(R.id.tars_txt);
        weight_txt = (TextInputEditText) parentview.findViewById(R.id.weight_txt);
        vendor_amt_txt = (TextInputEditText) parentview.findViewById(R.id.vendor_amt_txt);

        tars_txt.setText("0");weight_txt.setText("0");vendor_amt_txt.setText("0");

        labourchrg_txt = (TextInputEditText) parentview.findViewById(R.id.labourchrg_txt);
        commisn_txt    = (TextInputEditText) parentview.findViewById(R.id.commisn_txt);

        order_add_btn = (Button) parentview.findViewById(R.id.order_add_btn);

        orders_count = (TextView) parentview.findViewById(R.id.orders_count);
        orders_filter = (Button) parentview.findViewById(R.id.orders_filter);
        orders_reload = (Button) parentview.findViewById(R.id.orders_reload);

        order_create.setVisibility(View.GONE);
        order_date_text.setOnClickListener(this);
        order_add_btn.setOnClickListener(this);
        showorder.setOnClickListener(this);
        orders_filter.setOnClickListener(this);
        orders_reload.setOnClickListener(this);
    }

    private void setupclients(){
        int curcount=0;
        Cursor clientcur = pocDbhelpher.getAlldata("Select * from "+TABLE_CLIENT_INFO+" ;");
        if(clientcur!=null) {
            clientcur.moveToFirst();
            curcount = clientcur.getCount();
            if(curcount > 0) {
                clients_id = new Integer[curcount+2];
                clients_name = new String[curcount + 2];
                clients_name[0] = "Select Client";
                clients_id[0] =-1;
                for (int i = 0; i < curcount; i++) {
                    clients_id[i+1] = clientcur.getInt(clientcur.getColumnIndex(KEY_ID));
                    clients_name[i + 1] = clientcur.getString(clientcur.getColumnIndex(KEY_U_UNAME));
                    clientcur.moveToNext();
                }
                clients_name[curcount+1]="Add New Client";
                clients_id[curcount+1] =-1;
            } else {
                clients_name = new String[2];
                clients_id = new Integer[2];
                clients_name[0] = "Select Client";
                clients_name[1] = "Add New Client";
                clients_id[0] =-1;
                clients_id[1] =-1;
            }
        } else {
            clients_name = new String[2];
            clients_id = new Integer[2];
            clients_name[0] = "Select Client";
            clients_name[1] = "Add New Client";
            clients_id[0] =-1;
            clients_id[1] =-1;
        }

        ArrayAdapter<String> array = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, clients_name);
        array.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        client_sel_spinner.setAdapter(array);
        client_sel_spinner.setSelection(0);
    }

    private void setupvendors(){
        int curcount=0;
        Cursor vendorcur = pocDbhelpher.getAlldata("Select * from "+TABLE_VENDOR_INFO+" ;");
        if(vendorcur!=null){
            vendorcur.moveToFirst();
            curcount=vendorcur.getCount();
            if(curcount > 0){
            vendors_id   = new Integer[curcount+2];
            vendors_name = new String[curcount+2];
            vendors_name[0]="Select Vendor";
            vendors_id[0] =-1;
            for (int i = 0; i < curcount; i++) {
                vendors_id[i+1]=vendorcur.getInt(vendorcur.getColumnIndex(KEY_ID));
                vendors_name[i+1]=vendorcur.getString(vendorcur.getColumnIndex(KEY_U_UNAME));
                vendorcur.moveToNext();
            }
                vendors_name[curcount+1]="Add New Vendor";
                vendors_id[curcount+1] =-1;
            } else {
                vendors_name = new String[2];
                vendors_id   = new Integer[2];
                vendors_name[0]="Select Vendor";
                vendors_name[1]="Add New Vendor";
                vendors_id[0] =-1;
                vendors_id[1] =-1;
            }
        } else {
            vendors_name = new String[2];
            vendors_id   = new Integer[2];
            vendors_name[0]="Select Vendor";
            vendors_name[1]="Add New Vendor";
            vendors_id[0] =-1;
            vendors_id[1] =-1;
        }

        ArrayAdapter<String>  array = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, vendors_name);
        array.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        vendor_sel_spinner.setAdapter(array);
        vendor_sel_spinner.setSelection(0);
    }

    private void setuplocation(){
        int curcount=0;
        Cursor locationcur = pocDbhelpher.getAlldata("Select * from "+TABLE_LOCATION+" ;");
        if(locationcur!=null){
            locationcur.moveToFirst();
            curcount=locationcur.getCount();
            location = new String[curcount];
            for (int i = 0; i < curcount; i++) {
                location[i]=locationcur.getString(locationcur.getColumnIndex(KEY_TAG_NAME));
                locationcur.moveToNext();
            }
        }
    }

    private void startapp(){
            setuplocation();
            setupclients();
            setupvendors();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentview = inflater.inflate(R.layout.fragment_order, container, false);
        initlyts();
        //String tempfile=new SimpleDateFormat("E, dd MMM yyyy").format(new Date());
        String tempfile=new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        order_date_text.setText(tempfile);
        setorders();
        setuplocation();
        setupclients();
        setupvendors();

        order_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                order_type = parent.getItemAtPosition(position).toString();
                //String Text = order_type_spinner.getSelectedItem().toString();
                Log.d(TAG, "onItemSelected: "+order_type);
                if(order_type.equalsIgnoreCase("Bulk")){
                    Log.d(TAG, "onItemSelected: why its not working");
                    bulk_order_lyt.setVisibility(View.VISIBLE);
                    vendor_sel_spinner.setVisibility(View.VISIBLE);
                    bulk_order_caption.setVisibility(View.VISIBLE);
                } else {
                    bulk_order_caption.setVisibility(View.GONE);
                    vendor_sel_spinner.setVisibility(View.GONE);
                    bulk_order_lyt.setVisibility(View.GONE);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        client_sel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String  order_type = parent.getItemAtPosition(position).toString();
                vendor_index=position;
                //String Text = order_type_spinner.getSelectedItem().toString();
                order_credit.setText("Rs.0");
                if( order_type.equalsIgnoreCase("Add New Client")){
                    showDialogFullscreen("Add New Client",clients_name);
                } else if(order_type.equalsIgnoreCase("Select Client")) {

                } else {
                    order_credit.setText(getclientcredit(2));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        vendor_sel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String order_type = parent.getItemAtPosition(position).toString();
                client_index=position;
                //String Text = order_type_spinner.getSelectedItem().toString();
                if( order_type.equalsIgnoreCase("Add New Vendor")){
                    showDialogFullscreen("Add New Vendor",vendors_name);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        order_list_rv.addOnItemTouchListener(new RecyclerTouchListener(getContext(), order_list_rv, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               //  MakeToast("Long Press to update order");
            }
            @Override
            public void onLongItemClick(View view, int position) {
               /*
                orderlist_pojo pojo = adapterOrderlist.getdata(position);
                if(pojo.getOrder_status().equalsIgnoreCase("pending")) {
                    live_orderid = pojo.getOrder_id();
                    live_orderclientref = pojo.getOrder_client();
                    live_ordervendorref = pojo.getOrder_vendor();
                    live_ordervendoramt = pojo.getVendor_amount();
                    live_orderamt = pojo.getOrder_amount();
                    updateorder_dialog(pojo.getVendor_amount(), pojo.getOrder_amount());
                } else {
                    MakeToast("Order closed");
                }
               */
            }
        }));
        return parentview;
    }

    private void updateorder_dialog(String order_vendor_amt,String order_amt){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Order Update ");
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        //View dialogView = inflater.inflate(R.layout. dialog_order_status_update, null);

        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.dialog_order_status_update, null);

        dialogBuilder.setView(promptsView);

        TextView orderstatusupdate_title = (TextView) promptsView.findViewById(R.id.orderstatusupdate_title);
        orderstatusupdate_title.setText("Order #"+live_orderid);

        TextView orderstatusupdate_amt = (TextView) promptsView.findViewById(R.id.orderstatusupdate_title);
        orderstatusupdate_title.setText("Actual :"+order_amt+"\n(vendor amt: "+order_vendor_amt+" order credit : "+order_credit.getText()+" )");

        final TextInputEditText ordervamount = (TextInputEditText) promptsView.findViewById(R.id.orderstatusupdate_vamt_txt);
        ordervamount.setText(""+order_vendor_amt);

        final TextInputEditText orderamount = (TextInputEditText) promptsView.findViewById(R.id.orderstatusupdate_amt_txt);
        orderamount.setText("0");

        dialogBuilder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
              String order_amt  = orderamount.getText().toString();
              String order_vamt = ordervamount.getText().toString();
              Double order_amt_d = ParseDouble(order_amt);
              Double order_vamt_d = ParseDouble(order_vamt);
              //Log.d(TAG, "onClick: dialog builder " + order_amt + " " + order_vamt);
              if(order_amt_d > 0  && order_vamt_d > 0){
                 updateorder_data(order_amt,order_vamt);
              } else {
                  MakeToast("Invalid amount");
              }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MakeToast(live_orderid+" not updated");
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void updateorder_data(String orderpaidamt, String ordervamt){

        BigDecimal finalclientcreditamt,finalvendorcreditamt;// = vendor_Double+commision_Double+laborchrgs_Double;

        BigDecimal orderamt_Double   = BigDecimal.valueOf(ParseDouble(live_orderamt));

        BigDecimal order_paid_Double = BigDecimal.valueOf(ParseDouble(orderpaidamt));

        finalclientcreditamt = orderamt_Double.subtract(order_paid_Double);
        MakeToast(""+finalclientcreditamt);
        Log.d(TAG, "updateorder_data: credit "+finalclientcreditamt.toPlainString());


        BigDecimal orderactvendoramt_Double   = BigDecimal.valueOf(ParseDouble(live_ordervendoramt));

        BigDecimal order_paidvendoramt_Double = BigDecimal.valueOf(ParseDouble(ordervamt));

        finalvendorcreditamt = orderactvendoramt_Double.subtract(order_paidvendoramt_Double);
        MakeToast(""+finalvendorcreditamt);
        Log.d(TAG, "updateorder_data: credit "+finalvendorcreditamt.toPlainString());

        ContentValues contentValues=new ContentValues();
        String wherestring ;
        contentValues.put(KEY_ORDER_PAID, order_paid_Double.toPlainString());
        //contentValues.put(KEY_ORDER_VENDORAMT, ordervamt);
        contentValues.put(KEY_STATUS, "2");
        wherestring = KEY_ID + " = ?";
        String[] selectionArgs = {live_orderid};
        Boolean cursor = pocDbhelpher.updateData(TABLE_ORDERS,contentValues,wherestring,selectionArgs);
        if(cursor) {

            ContentValues initialValues = new ContentValues();
            String[] selectionArgs_v = {live_orderid};

            if(!live_ordervendorref.equalsIgnoreCase("0")) {
                //initialValues.put(KEY_VS_ORDERREF,live_orderid);
                //initialValues.put(KEY_ORDER_VENDORREF,live_ordervendorref);
                initialValues.put(KEY_VS_AMOUNT, ordervamt);
                initialValues.put(KEY_STATUS, "2");
                wherestring = KEY_VS_ORDERREF + " = ?";
                cursor = pocDbhelpher.updateData(TABLE_VENDOR_SETTLEMENT, initialValues, wherestring, selectionArgs);
            }

            initialValues = new ContentValues();
            //initialValues.put(KEY_VS_ORDERREF,live_orderid);
            //initialValues.put(KEY_ORDER_VENDORREF,live_orderclientref);
            initialValues.put(KEY_VS_AMOUNT,orderpaidamt);
            initialValues.put(KEY_STATUS,"2");
            cursor = pocDbhelpher.updateData(TABLE_CLIENT_SETTLEMENT,initialValues,wherestring,selectionArgs_v);

            // wallet add
            initialValues=new ContentValues();
            //initialValues.put(KEY_VS_ORDERREF,live_orderid);
            //initialValues.put(KEY_ORDER_VENDORREF,live_ordervendorref);
            //initialValues.put(KEY_CWALLET_CREF,live_ordervendorref);
            initialValues.put(KEY_CWALLET_BAL,finalvendorcreditamt.toPlainString());
            wherestring = KEY_CWALLET_CREF + " = ?";
            String[] selectionArgs_cw = {live_ordervendorref};
            cursor = pocDbhelpher.updateData(TABLE_VENDOR_WALLET,initialValues,wherestring,selectionArgs_cw);

            initialValues = new ContentValues();
            //initialValues.put(KEY_VS_ORDERREF,live_orderid);
            //initialValues.put(KEY_ORDER_VENDORREF,live_orderclientref);
            //initialValues.put(KEY_CWALLET_CREF,live_orderclientref);
            initialValues.put(KEY_CWALLET_BAL,finalclientcreditamt.toPlainString());
            String[] selectionArgs_vw = {live_orderclientref};
            cursor = pocDbhelpher.updateData(TABLE_CLIENT_WALLET,initialValues,wherestring,selectionArgs_vw);

            setorders();
        }
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    private void  setorders(){
        if(ordermodellist!=null && ordermodellist.size()>0){
            ordermodellist.clear();
        }
        Cursor cursor = pocDbhelpher.getAlldata("Select * from "+TABLE_ORDERS+" order by id desc;");
        try {
            if(cursor!=null) {
                cursor.moveToFirst();
                String ID = "", ORDER_TYPE = "", ORDER_CLIENTREF = "", ORDER_VENDORREF = "";
                String ORDER_DATA = "", ORDER_VENDORAMT = "", ORDER_AMOUNT = "",ORDER_CREDIT="";
                String ORDER_PAID = "",CREATED_AT="", UPDATED_AT = "",STATUS_TEMP="";
                Integer STATUS = 0;
                String order_text[]={"","pending","closed"};
                HashMap<String, String> map
                        = new HashMap<>();
                map.put("", "");
                map.put("1", "pending");
                map.put("2", "closed");
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

                    CREATED_AT = "" + cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT));
                    UPDATED_AT = "" + cursor.getString(cursor.getColumnIndex(KEY_UPDATED_AT));

                    STATUS_TEMP =""+ cursor.getString(cursor.getColumnIndex(KEY_STATUS));
                    Log.d(TAG, "setorders: org status "+STATUS_TEMP);
                   // STATUS = Integer.getInteger(STATUS_TEMP,0);
                   // Log.d(TAG, "setorders: order status "+STATUS);

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
                                MainActivity.setprojectdate(CREATED_AT),
                            ORDER_AMOUNT,
                            ORDER_VENDORREF,
                            ORDER_VENDORAMT,
                            ORDER_CLIENTREF,
                            ORDER_TYPE,
                            map.get(STATUS_TEMP),
                            "Vendor Amt: "+ EnglishNumberToWords.convertToIndianCurrency(ORDER_VENDORAMT) + " paid : " + ORDER_PAID + " status:" + order_text[STATUS] + " Extra Data : " + ordermaindata
                            ,ORDER_CREDIT,
                                MainActivity.setprojectdate(UPDATED_AT)

                        );
                     ordermodellist.add(pojo);


                    cursor.moveToNext();
                }
            }
            if (ordermodellist!=null && ordermodellist.size()>0){
                orders_count.setText("Total Orders : "+ordermodellist.size());
                adapterOrderlist = new adapter_orderlist(ORDER_USER_ADMIN,ordermodellist,getContext());
                order_list_rv.setAdapter(adapterOrderlist);
                adapterOrderlist.notifyDataSetChanged();
            } else {
                orders_count.setText("Total Orders : 0");
                order_create.setVisibility(View.VISIBLE);
            }

        } catch (Exception e){
            Log.d(TAG, "setorders: "+Log.getStackTraceString(e));
        }
    }

    private void addorder() {

        BigDecimal vendor_Double  = BigDecimal.valueOf(ParseDouble(""+vendor_amt_txt.getText().toString()));

        BigDecimal commision_Double=new BigDecimal(0);
        BigDecimal laborchrgs_Double=new BigDecimal(0);

        BigDecimal finalamt; // = vendor_Double+commision_Double+laborchrgs_Double;

        Integer clientref = clients_id[client_sel_spinner.getSelectedItemPosition()];
        Integer vendorref=0;

        if(order_type.equalsIgnoreCase("Bulk")) {
             commision_Double = BigDecimal.valueOf(ParseDouble("" + commisn_txt.getText().toString()));
             laborchrgs_Double = BigDecimal.valueOf(ParseDouble("" + labourchrg_txt.getText().toString()));
             finalamt = laborchrgs_Double.add(commision_Double);
            Log.d(TAG, "addorder: vendor info "+vendors_name[vendor_sel_spinner.getSelectedItemPosition()]);
            vendorref=vendors_id[vendor_sel_spinner.getSelectedItemPosition()];
        }

        BigDecimal clientcredt = BigDecimal.valueOf(ParseDouble(getclientcredit(1)));

        Log.d(TAG, "addorder: com. charges "+laborchrgs_Double +" "+commision_Double);

        finalamt = vendor_Double.add(clientcredt);

        JSONArray passArray = new JSONArray();
        JSONObject jObjP    = new JSONObject();
        try {
                String str_tars_txt   = "" + tars_txt.getText().toString();
                String str_weight_txt = "" + weight_txt.getText().toString();
                jObjP.put("tars", str_tars_txt);
                jObjP.put("weight", str_weight_txt);
            if(order_type.equalsIgnoreCase("Bulk")) {
                jObjP.put("labourchrgs", laborchrgs_Double.toPlainString());
                jObjP.put("commision", commision_Double.toPlainString());
            }
            passArray.put(jObjP);
        } catch (JSONException e) {
            MakeToast("Try again");
            Log.d(TAG, "attemptOrder: exception"+Log.getStackTraceString(e));
        }

        Log.d(TAG, "addorder: "+finalamt.toString());

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_STATUS,"1");
        initialValues.put(KEY_APPUSERREF, "1");
        initialValues.put(KEY_ORDER_TYPE, order_type);
        initialValues.put(KEY_ORDER_CLIENTREF, clientref);
        initialValues.put(KEY_ORDER_VENDORREF, vendorref);
        initialValues.put(KEY_ORDER_DATA, passArray.toString());
        initialValues.put(KEY_ORDER_VENDORAMT, vendor_Double.toPlainString());
        initialValues.put(KEY_ORDER_AMOUNT, finalamt.toPlainString());
        initialValues.put(KEY_ORDER_CREDIT, clientcredt.toPlainString());
        initialValues.put(KEY_ORDER_PAID, 0);

        long orderidref= pocDbhelpher.addData(TABLE_ORDERS,initialValues);

        if(vendorref>0) {
            initialValues = new ContentValues();
            initialValues.put(KEY_APPUSERREF, "1");
            initialValues.put(KEY_STATUS, "1");
            initialValues.put(KEY_VS_ORDERREF, orderidref);
            initialValues.put(KEY_ORDER_VENDORREF, vendorref);
            initialValues.put(KEY_VS_AMOUNT, vendor_Double.toPlainString());

            pocDbhelpher.addData(TABLE_VENDOR_SETTLEMENT, initialValues);
        }

        initialValues = new ContentValues();
        initialValues.put(KEY_VS_ORDERREF,orderidref);
        initialValues.put(KEY_ORDER_VENDORREF,clientref);
        initialValues.put(KEY_VS_AMOUNT,finalamt.toPlainString());
        initialValues.put(KEY_APPUSERREF, "1");
        initialValues.put(KEY_STATUS,"1");
        pocDbhelpher.addData(TABLE_CLIENT_SETTLEMENT,initialValues);
        order_create.setVisibility(View.GONE);
        order_add_btn.setEnabled(true);

        setorders();
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

    private void showDialogFullscreen(String usertype,String[] usernames) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AdduserDialogFragment newFragment = new AdduserDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString( "addusertype" , usertype);
        arguments.putStringArray( "usernames" , usernames);
        arguments.putStringArray("locationdata",location);
        newFragment.setArguments(arguments);
        newFragment.setRequestCode(DIALOG_QUEST_CODE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        newFragment.setOnCallbackResult(new AdduserDialogFragment.CallbackResult() {
            @Override
            public void sendResult(int requestCode, Object obj) {
                if (requestCode == DIALOG_QUEST_CODE) {
                    displayDataResult((userinfo) obj);
                }
            }
        });
    }

    private void displayDataResult(userinfo event) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_U_NAME, event.name);
        contentValues.put(KEY_U_UNAME, event.username);
        contentValues.put(KEY_U_PHONE, event.mobile);
        contentValues.put(KEY_U_ADDRESS, event.address);
        contentValues.put(KEY_U_LOCATION, event.location);

        if (event.usertype.equalsIgnoreCase("Add New Client")) {
            long res=  pocDbhelpher.addData(TABLE_CLIENT_INFO,contentValues);
            if(res>0)
                MakeToast("Client Data ");
            else
                MakeToast("Client Data not update ");
            setupclients();
        } else if (event.usertype.equalsIgnoreCase("Add New Vendor")) {
            long res=  pocDbhelpher.addData(TABLE_VENDOR_INFO,contentValues);
            if(res>0)
                MakeToast("Vendor Data ");
            else
                MakeToast("Vendor Data not update ");
            setupvendors();
        }
        else
            MakeToast("Please add location ");
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();

        if(vid==R.id.showorder){
            startapp();
            if(order_create.getVisibility() == View.VISIBLE){
                order_create.setVisibility(View.GONE);
            } else if(order_create.getVisibility() == View.GONE){
                order_create.setVisibility(View.VISIBLE);
            }

        } else if(vid == R.id.order_add_btn){
            String  vendor_type = vendor_sel_spinner.getSelectedItem().toString();
            String  client_type = client_sel_spinner.getSelectedItem().toString();
            if(order_type.equalsIgnoreCase("Bulk")){
                if (vendor_type.equalsIgnoreCase("Select Client") || vendor_type.equalsIgnoreCase("Add New Client")) {

                } else {
                    order_add_btn.setEnabled(false);
                    addorder();
                }
            } else {
                if (vendor_type.equalsIgnoreCase("Select Client") || vendor_type.equalsIgnoreCase("Add New Client")) {

                } else if (client_type.equalsIgnoreCase("Select Vendor") || client_type.equalsIgnoreCase("Add New Vendor")) {

                } else {
                    order_add_btn.setEnabled(false);
                    addorder();
                }
            }
        } else if(vid == R.id.orders_reload){
            setorders();
            MakeToast("Reload complete");
        } else if(vid == R.id.orders_filter){
            MakeToast("Filter module coming soon");
        } else if(vid==R.id.order_date_text){
            showDatePickerDialog();
        }
    }

     private String getclientcredit(Integer ptype){
        String clientcredt="";
         Integer clientref = clients_id[client_sel_spinner.getSelectedItemPosition()];
         BigDecimal clientbigDecimal = null;
         String[] cdata= new String[] { KEY_CWALLET_BAL };
         String[] refname= new String[] { String.valueOf(clientref) };
         String wherestring = KEY_ID + " = ?";
         Cursor cursor = pocDbhelpher.SearchData(TABLE_CLIENT_WALLET,cdata,refname,wherestring,"id DESC");
         try {
             if (cursor != null) {
                 cursor.moveToFirst();
                 for (int i = 0; i < cursor.getCount(); i++) {
                     clientbigDecimal= BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(KEY_CWALLET_BAL)));
                     Log.d(TAG, "getclientcredit: "+clientbigDecimal.toPlainString());
                     if(ptype==1)
                      clientcredt=clientbigDecimal.toPlainString();
                     else if(ptype==2)
                         clientcredt="Rs."+clientbigDecimal.toPlainString();
                 }
             }
         } catch (Exception e){
             Log.d(TAG, "addorder: "+Log.getStackTraceString(e));
         }
         return clientcredt.trim();
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String tempdate= dayOfMonth+"-"+month+"-"+ year;
        order_date_text.setText(tempdate);
    }
}
