package com.bvrk.mobile.android.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import com.bvrk.mobile.android.R;
import com.bvrk.mobile.android.pojo.userinfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AdduserDialogFragment extends DialogFragment {

    public static final String TAG = "amarr";

    public CallbackResult callbackResult;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }
    private int request_code = 0;
    private View root_view;

    private TextView user_title;
    private Button user_btn_close,updateuser;

    private AppCompatSpinner user_location_spinner;

    private TextInputLayout   name_txt_lyt,mobile_txt_lyt,username_txt_lyt,address_txt_lyt;

    private TextInputEditText name_txt,mobile_txt,username_txt,address_txt;

    String usertype;

    String[]usernames,locationdata;

    Boolean cangoback=true;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            usertype = getArguments().getString("addusertype");
            usernames = getArguments().getStringArray("usernames");
            locationdata= getArguments().getStringArray("locationdata");
            Log.d(TAG, "onCreateView: "+usertype+locationdata);
            //MakeToast("af "+usertype);
        }

        if (locationdata == null) {
            usertype="null";
            Log.d(TAG, "this is null: "+usertype);
            sendDataResult();
        }

        root_view = inflater.inflate(R.layout.fragment_adduser, container, false);
        user_title = (TextView) root_view.findViewById(R.id.user_title);
        user_btn_close = (Button ) root_view.findViewById(R.id.user_btn_close);
        updateuser = (Button ) root_view.findViewById(R.id.updateuser);


        name_txt_lyt     = (TextInputLayout) root_view.findViewById(R.id.name_txt_lyt);
        mobile_txt_lyt   = (TextInputLayout) root_view.findViewById(R.id.mobile_txt_lyt);
        username_txt_lyt = (TextInputLayout) root_view.findViewById(R.id.username_txt_lyt);
        address_txt_lyt  = (TextInputLayout) root_view.findViewById(R.id.address_txt_lyt);

        name_txt     = (TextInputEditText) root_view.findViewById(R.id.name_txt);
        mobile_txt   = (TextInputEditText) root_view.findViewById(R.id.mobile_txt);
        username_txt = (TextInputEditText) root_view.findViewById(R.id.username_txt);
        address_txt  = (TextInputEditText) root_view.findViewById(R.id.address_txt);



        user_location_spinner = (AppCompatSpinner) root_view.findViewById(R.id.user_location_spinner);

        user_title.setText(usertype);

        user_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        updateuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String s1:usernames){
                    if(s1.equalsIgnoreCase(username_txt.getText().toString())){
                        cangoback=false;
                    }
                }
                if(cangoback) {
                    sendDataResult();
                    dismiss();
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
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        String[] timezones = getResources().getStringArray(R.array.timezone);
        ArrayAdapter<String> array = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, locationdata);
        array.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        user_location_spinner.setAdapter(array);
        user_location_spinner.setSelection(0);

        return root_view;
    }

    private void sendDataResult() {
        userinfo event = new userinfo();
        event.usertype = usertype;
        if(usertype.equalsIgnoreCase("null")){

        } else {
            event.name = name_txt.getText().toString();
            event.mobile = mobile_txt.getText().toString();
            event.username = username_txt.getText().toString();
            event.address = address_txt.getText().toString();
            event.location = user_location_spinner.getSelectedItem().toString();
        }

        if (callbackResult != null) {
            callbackResult.sendResult(request_code, event);
            Log.d(TAG, "sendDataResult: ");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        return dialog;
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, Object obj);
    }

}