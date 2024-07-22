package com.bedetaxi.bedetaxi;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class VerificationFragment extends Fragment {
    public static boolean IsVerify = false;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    SharedPreferencesManager sharedPreferencesManager;
    Context context;
    View rootView;
    public VerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_verification, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editText1 =(EditText)getView().findViewById(R.id.first);
        editText2 = (EditText)getView().findViewById(R.id.second);
        editText3 = (EditText)getView().findViewById(R.id.third);
        editText4 =(EditText)getView().findViewById(R.id.fourth);
        context = getActivity().getApplicationContext();
        sharedPreferencesManager = new SharedPreferencesManager(context);
        editText1.requestFocus();


        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = editText1.getText().length();

                if (textlength1 >= 1) {
                    editText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = editText1.getText().length();

                if (textlength1 >= 1) {
                    editText3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textlength1 = editText1.getText().length();

                if (textlength1 >= 1) {
                    editText4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(checkFieldsEmpty() && checkNumbers()) {
                    String code = getCode();

                    List<PropertyInfo> prop = getPropertyInfo(code);

                    WebAPI api = new WebAPI(context, "VerifyCode", prop);
                    String output = api.call();
                    JSONArray data = null;
                    try {
                        data = new JSONArray(output);


                        String status = data.getJSONObject(0).getString("status");

                        if (status.equals("success")) {
                            sharedPreferencesManager.Verify();
                            Toast.makeText(context, "Phone number was successfuly", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            IsVerify = true;
                            getActivity().overridePendingTransition(R.animator.slide_in_right, R.animator.slide_in_left);



                        } else {
                            Toast.makeText(context, data.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                    }
                }}
        });









    }

    public boolean checkFieldsEmpty(){
        if (editText1.getText().toString().isEmpty() ||
                editText2.getText().toString().isEmpty() ||
                editText3.getText().toString().isEmpty() ||
                editText4.getText().toString().isEmpty()
                ){
            return false;
        }
        return true;
    }
    public boolean checkNumbers(){
        if (editText1.getText().toString().matches("[0-9]") &&
                editText2.getText().toString().matches("[0-9]") &&
                editText3.getText().toString().matches("[0-9]") &&
                editText4.getText().toString().matches("[0-9]")){
            return true;
        }
        return false;
    }

    public String getCode(){
       return editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString();

    }
    public List<PropertyInfo> getPropertyInfo(String code){
        List<PropertyInfo> props = new ArrayList<PropertyInfo>();

        PropertyInfo userid = new PropertyInfo();
        userid.setName("UserID");
        userid.setValue(sharedPreferencesManager.getUserID());
        userid.setType(String.class);
        props.add(userid);
        PropertyInfo codeVer = new PropertyInfo();
        codeVer.setName("Code");
        codeVer.setValue(code);
        codeVer.setType(String.class);
        props.add(codeVer);

        return props;

    }
}
