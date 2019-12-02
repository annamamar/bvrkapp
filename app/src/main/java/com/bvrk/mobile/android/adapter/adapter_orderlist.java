package com.bvrk.mobile.android.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bvrk.mobile.android.R;
import com.bvrk.mobile.android.config.BaseURL;
import com.bvrk.mobile.android.pojo.orderlist_pojo;
import com.bvrk.mobile.android.util.db.PocDbhelpher;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.bvrk.mobile.android.util.db.PocDbhelpher.*;

public class adapter_orderlist extends RecyclerView.Adapter<adapter_orderlist.orderlistViewHolder> {

    public static final String TAG = "amarr";
    private Context context;
    private List<orderlist_pojo> ordermodellist;
    private String utype;
    Map<String, String> vendorHashMap = new LinkedHashMap<String, String>();
    Map<String, String> clientHashMap = new LinkedHashMap<String, String>();
    PocDbhelpher pocDbhelpher;

    orderlist_pojo mainpojo=null;

    //String live_orderid,live_ordertype,live_ordervendoramt,live_orderclientref,live_ordervendorref;

    private String live_paidorderamt,live_paidvendoramt;

    public orderlist_pojo getdata(int pos){
        return ordermodellist.get(pos);
    }

    public adapter_orderlist(List<orderlist_pojo> modelList, Context pocActivity) {
        this.ordermodellist=modelList;
        this.context=pocActivity;
        pocDbhelpher = new PocDbhelpher(context);
        setupusers();
    }

    public adapter_orderlist(String utype,List<orderlist_pojo> modelList, Context pocActivity) {
        this.utype = utype;
        this.ordermodellist=modelList;
        this.context=pocActivity;
        pocDbhelpher = new PocDbhelpher(context);
        setupusers();
    }

    @NonNull
    @Override
    public orderlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_orderlist, parent, false);
        context = parent.getContext();
        return new adapter_orderlist.orderlistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull orderlistViewHolder holder, int position) {
        orderlist_pojo pojo = ordermodellist.get(position);
        holder.order_cdate.setText(pojo.getOrder_date());
        holder.order_amount.setText(pojo.getOrder_amount());
        String clientname=clientHashMap.get(pojo.getOrder_client());
        holder.order_client.setText("client:"+clientname);
        String vendorname=vendorHashMap.get(pojo.getOrder_vendor());

        holder.order_vendor.setText(vendorname+":"+pojo.getVendor_amount());

        holder.order_type.setText("Order #"+pojo.getOrder_id()+" " + pojo.getOrder_type()
        +" "+pojo.getOrder_status()+" :"+pojo.getOrder_udate());

        holder.order_misc_details.setText(pojo.getOrder_miscdetails());

        if(utype.equalsIgnoreCase(BaseURL.ORDER_USER_ADMIN)){
            if(pojo.getOrder_status().equalsIgnoreCase(BaseURL.ORDER_TYPE_PENDING)) {
                holder.order_accept.setVisibility(View.VISIBLE);
                holder.order_decline.setVisibility(View.VISIBLE);
            } else {
                holder.order_accept.setVisibility(View.GONE);
                holder.order_decline.setVisibility(View.GONE);
            }
        } else {
            holder.order_accept.setVisibility(View.GONE);
            holder.order_decline.setVisibility(View.GONE);
        }
        holder.order_accept.setVisibility(View.VISIBLE);
        holder.order_decline.setVisibility(View.VISIBLE);

    }

    private void setupusers(){
        Cursor cursor = null;
        try {
            cursor =pocDbhelpher.getAlldata("Select * from "+TABLE_CLIENT_INFO+" ;");
            if(cursor!=null) {
                String ID="",UNAME="";
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    ID = "" + cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    UNAME = "" + cursor.getString(cursor.getColumnIndex(KEY_U_UNAME));
                    clientHashMap.put(ID,UNAME);
                    cursor.moveToNext();
                }
                cursor=null;
            }
            cursor =pocDbhelpher.getAlldata("Select * from "+TABLE_VENDOR_INFO+" ;");
            if(cursor!=null) {
                String ID="",UNAME="";
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    ID = "" + cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    UNAME = "" + cursor.getString(cursor.getColumnIndex(KEY_U_UNAME));
                    vendorHashMap.put(ID,UNAME);
                    cursor.moveToNext();
                }
            }

        } catch (Exception e){
            Log.d(TAG, "set users: "+Log.getStackTraceString(e));
        }
    }

    @Override
    public int getItemCount() {
        return ordermodellist.size();
    }

    public class orderlistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout order_list_layout;
        public TextView order_cdate, order_amount, order_client;
        public TextView order_vendor, order_type, order_misc_details;
        public Button orders_delete,order_accept,order_decline;

        public orderlistViewHolder(View view) {
            super(view);
            order_list_layout = view.findViewById(R.id.order_list_layout);
            order_accept  = view.findViewById(R.id.order_accept);
            order_decline = view.findViewById(R.id.order_decline);

            order_cdate  = view.findViewById(R.id.order_cdate);
            order_amount = view.findViewById(R.id.order_amount);

            order_client = view.findViewById(R.id.order_client);
            order_vendor = view.findViewById(R.id.order_vendor);

            order_type = view.findViewById(R.id.order_type);
            order_misc_details = view.findViewById(R.id.order_misc_details);

            order_accept.setOnClickListener(this);
            order_decline.setOnClickListener(this);
            order_list_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            //final orderlist_pojo pojo = ordermodellist.get(position);
            mainpojo = ordermodellist.get(position);
            if (id == R.id.order_decline) {
                String wherestring = PocDbhelpher.KEY_ID + " = ?";
                String[] selectionArgs = {mainpojo.getOrder_id().trim()};
                pocDbhelpher.deletedata(TABLE_ORDERS, wherestring, selectionArgs);
                Log.d(TAG, "order_decline: "+mainpojo.toString());
            } else if (id == R.id.order_accept) {
                MakeToast("This is on the way");
            } else if (id == R.id.order_list_layout) {
                if (mainpojo.getOrder_status().equalsIgnoreCase(BaseURL.ORDER_TYPE_PENDING))
                    if (!utype.equalsIgnoreCase("orders")) {
                        updateorder_dialog();
                    }
            }
        }

    //private void updateorder_dialog(String order_vendor_amt,String order_amt){
        private void updateorder_dialog() {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle("Order Update ");
            dialogBuilder.setCancelable(false);
            View promptsView = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_order_status_update, null, false);
            dialogBuilder.setView(promptsView);

        /*
        LayoutInflater inflater = context.getLayoutInflater();
        //View dialogView = inflater.inflate(R.layout. dialog_order_status_update, null);

        LayoutInflater li = LayoutInflater.from(getContext());

        View promptsView = li.inflate(R.layout.dialog_order_status_update, null);
        */

            /*
            final Dialog dialog = new Dialog(context);
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle("Order Update ");
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_light);
            dialog.setContentView(R.layout.dialog_order_status_update);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.show();
            */

            TextView orderstatusupdate_title = (TextView) promptsView.findViewById(R.id.orderstatusupdate_title);
            orderstatusupdate_title.setText("Order #" + mainpojo.getOrder_id());

            TextView orderstatusupdate_amt = (TextView) promptsView.findViewById(R.id.orderstatusupdate_title);


            orderstatusupdate_title.setText("Actual :" + mainpojo.getOrder_amount()
                    + "\n(vendor amt: " + mainpojo.getVendor_amount() + " order credit : "
                    + mainpojo.getOrder_credit() + " )");

            final TextInputEditText ordervamount = (TextInputEditText) promptsView.findViewById(R.id.orderstatusupdate_vamt_txt);
            ordervamount.setText("" + mainpojo.getVendor_amount());

            final TextInputEditText orderamount = (TextInputEditText) promptsView.findViewById(R.id.orderstatusupdate_amt_txt);
            orderamount.setText("0");

            dialogBuilder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //do something with edt.getText().toString();
                    String order_amt = orderamount.getText().toString();
                    String order_vamt = ordervamount.getText().toString();
                    Double order_amt_d = ParseDouble(order_amt);
                    Double order_vamt_d = ParseDouble(order_vamt);
                    //Log.d(TAG, "onClick: dialog builder " + order_amt + " " + order_vamt);
                    if (order_amt_d > 0 && order_vamt_d > 0) {
                        updateorder_data(order_amt, order_vamt);
                    } else {
                        MakeToast("Invalid amount");
                    }
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    MakeToast(mainpojo.getOrder_id() + " not updated");
                }
            });

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_light);
            alertDialog.show();
        }

        private void updateorder_data(String orderpaidamt, String ordervamt) {

            String live_orderid = mainpojo.getOrder_id();
            String live_ordervendorref = mainpojo.getOrder_vendor();
            String live_orderclientref = mainpojo.getOrder_client();

            BigDecimal finalclientcreditamt, finalvendorcreditamt;// = vendor_Double+commision_Double+laborchrgs_Double;

            BigDecimal orderamt_Double = BigDecimal.valueOf(ParseDouble(mainpojo.getOrder_amount()));

            BigDecimal order_paid_Double = BigDecimal.valueOf(ParseDouble(orderpaidamt));

            finalclientcreditamt = orderamt_Double.subtract(order_paid_Double);

            MakeToast("" + finalclientcreditamt);

            Log.d(TAG, "updateorder_data: credit " + finalclientcreditamt.toPlainString());

            BigDecimal orderactvendoramt_Double = BigDecimal.valueOf(ParseDouble(mainpojo.getOrder_vendor()));

            BigDecimal order_paidvendoramt_Double = BigDecimal.valueOf(ParseDouble(ordervamt));

            finalvendorcreditamt = orderactvendoramt_Double.subtract(order_paidvendoramt_Double);
            MakeToast("" + finalvendorcreditamt);
            Log.d(TAG, "updateorder_data: credit " + finalvendorcreditamt.toPlainString());

            ContentValues contentValues = new ContentValues();
            String wherestring;
            contentValues.put(PocDbhelpher.KEY_ORDER_PAID, order_paid_Double.toPlainString());
            //contentValues.put(KEY_ORDER_VENDORAMT, ordervamt);
            contentValues.put(PocDbhelpher.KEY_STATUS, "2");
            wherestring = PocDbhelpher.KEY_ID + " = ?";
            String[] selectionArgs = {mainpojo.getOrder_id()};
            Boolean cursor = pocDbhelpher.updateData(PocDbhelpher.TABLE_ORDERS, contentValues, wherestring, selectionArgs);
            if (cursor) {

                ContentValues initialValues = new ContentValues();
                String[] selectionArgs_v = {live_orderid};
                if (!live_ordervendorref.equalsIgnoreCase("0")) {
                    //initialValues.put(KEY_VS_ORDERREF,live_orderid);
                    //initialValues.put(KEY_ORDER_VENDORREF,live_ordervendorref);
                    initialValues.put(KEY_VS_AMOUNT, ordervamt);
                    initialValues.put(PocDbhelpher.KEY_STATUS, "2");
                    wherestring = KEY_VS_ORDERREF + " = ?";
                    cursor = pocDbhelpher.updateData(TABLE_VENDOR_SETTLEMENT, initialValues, wherestring, selectionArgs);
                }

                initialValues = new ContentValues();
                //initialValues.put(KEY_VS_ORDERREF,live_orderid);
                //initialValues.put(KEY_ORDER_VENDORREF,live_orderclientref);
                initialValues.put(KEY_VS_AMOUNT, orderpaidamt);
                initialValues.put(PocDbhelpher.KEY_STATUS, "2");
                cursor = pocDbhelpher.updateData(TABLE_CLIENT_SETTLEMENT, initialValues, wherestring, selectionArgs_v);

                // wallet add
                initialValues = new ContentValues();
                //initialValues.put(KEY_VS_ORDERREF,live_orderid);
                //initialValues.put(KEY_ORDER_VENDORREF,live_ordervendorref);
                //initialValues.put(KEY_CWALLET_CREF,live_ordervendorref);
                initialValues.put(KEY_CWALLET_BAL, finalvendorcreditamt.toPlainString());
                wherestring = KEY_CWALLET_CREF + " = ?";
                String[] selectionArgs_cw = {live_ordervendorref};
                cursor = pocDbhelpher.updateData(TABLE_VENDOR_WALLET, initialValues, wherestring, selectionArgs_cw);

                initialValues = new ContentValues();
                //initialValues.put(KEY_VS_ORDERREF,live_orderid);
                //initialValues.put(KEY_ORDER_VENDORREF,live_orderclientref);
                //initialValues.put(KEY_CWALLET_CREF,live_orderclientref);
                initialValues.put(KEY_CWALLET_BAL, finalclientcreditamt.toPlainString());
                String[] selectionArgs_vw = {live_orderclientref};
                cursor = pocDbhelpher.updateData(TABLE_CLIENT_WALLET, initialValues, wherestring, selectionArgs_vw);
            }
        }

        double ParseDouble(String strNumber) {
            if (strNumber != null && strNumber.length() > 0) {
                try {
                    return Double.parseDouble(strNumber);
                } catch (Exception e) {
                    Log.d(TAG, "ParseDouble: " + Log.getStackTraceString(e));
                    return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
                }
            } else return 0;
        }

        void MakeToast(String data) {
            Toast toast = new Toast(context);
            View view = LayoutInflater.from(context).inflate(R.layout.z_custom_toast, null);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(data);
            toast.setView(view);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
            // Toast.makeText(context,data,Toast.LENGTH_SHORT).show();
        }

    }

}
