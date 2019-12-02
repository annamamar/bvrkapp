package com.bvrk.mobile.android.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bvrk.mobile.android.R;
import com.bvrk.mobile.android.pojo.userlist_pojo;
import com.bvrk.mobile.android.util.db.PocDbhelpher;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.bvrk.mobile.android.util.db.PocDbhelpher.*;


public class adapter_userlist extends RecyclerView.Adapter<adapter_userlist.orderlistViewHolder> {

    public static final String TAG = "amarr";
    private Context context;
    private List<userlist_pojo> usermodellist;
    String utype="";
    Map<String, String> walletHashMap = new LinkedHashMap<String, String>();
    PocDbhelpher pocDbhelpher;

    public adapter_userlist(String utype,List<userlist_pojo> modelList, Context pocActivity) {
        this.utype = utype;
        this.usermodellist=modelList;
        this.context=pocActivity;
        pocDbhelpher = new PocDbhelpher(context);
        setupwallet();
    }

    @NonNull
    @Override
    public orderlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_userlist, parent, false);
        context = parent.getContext();
        return new adapter_userlist.orderlistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull orderlistViewHolder holder, int position) {
        userlist_pojo pojo = usermodellist.get(position);
        String ordercount= walletHashMap.get(pojo.getId());
        Log.d(TAG, "onBindViewHolder: "+ordercount);

        if(ordercount==null)
            ordercount="";
        else
            ordercount=" @ "+ordercount;

        holder.list_user_name.setText(pojo.getUsername() + ordercount);
        holder.list_user_mobile.setText(pojo.getMobile());

        BigDecimal wallet_Double = BigDecimal.valueOf(ParseDouble(pojo.getWallet()));

        BigDecimal wallet_Double_abs = wallet_Double.abs();

        String client_wallet="";
        client_wallet = wallet_Double_abs.toPlainString();
        holder.user_wallet.setText(client_wallet);

        if(wallet_Double.signum()>0) {
            //"- "+
            holder.user_wallet.setTextColor(context.getResources().getColor(R.color.red_800,null));
        } else {
            //"+ "+
            holder.user_wallet.setTextColor(context.getResources().getColor(R.color.green_500,null));
        }




        String tempstr="Name: "+pojo.getName()+" palce: " + pojo.getLocation()
                +" Address: "+pojo.getAddress();
        tempstr=tempstr.replace("\n", " ");

        holder.list_user_addr.setText(tempstr);
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

    @Override
    public int getItemCount() {
        return usermodellist.size();
    }

    private void setupwallet(){
        Cursor cursor = null;
        try {
              String awsql="";
             if(utype.equalsIgnoreCase("client"))
                  awsql ="SELECT "+KEY_ORDER_CLIENTREF+" as userref , COUNT(id) as order_tot FROM orders GROUP BY id ;";
             else if(utype.equalsIgnoreCase("vendor"))
                  awsql ="SELECT "+KEY_ORDER_VENDORREF+" as userref , COUNT(id) as order_tot FROM orders GROUP BY id ;";

            cursor =pocDbhelpher.getAlldata(awsql);
            Log.d(TAG, "setupwallet: query "+utype+ " : " +awsql);

            if(cursor!=null) {
                String ID="",UNAME="";
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    ID = "" + cursor.getString(0);
                    UNAME = "" + cursor.getInt(1);
                    walletHashMap.put(ID,UNAME);
                    cursor.moveToNext();
                }
              Log.d(TAG, "setupwallet: "+walletHashMap.toString());
            }
        } catch (Exception e){
            Log.d(TAG, "set users: "+Log.getStackTraceString(e));
        }
    }

    public class orderlistViewHolder extends RecyclerView.ViewHolder {

        public TextView list_user_name,list_user_mobile,user_wallet,list_user_addr;

        public orderlistViewHolder(View view) {
            super(view);

            list_user_name = view.findViewById(R.id.list_user_name);
            list_user_mobile = view.findViewById(R.id.list_user_mobile);

            user_wallet = view.findViewById(R.id.user_wallet);
            list_user_addr = view.findViewById(R.id.list_user_addr);

        }
    }

}
