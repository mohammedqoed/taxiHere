package com.bedetaxi.bedetaxi;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegistertionFragment extends Fragment {

    private EditText nameText;
    private EditText phoneText;
    private EditText emailText;
    Context context;
    SharedPreferencesManager sharedPreferencesManager;
    View rootView;

    public RegistertionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_registertion, container, false);



        nameText = (EditText) rootView.findViewById(R.id.nameTextFields);
        phoneText = (EditText) rootView.findViewById(R.id.phoneTextField);
        emailText = (EditText) rootView.findViewById(R.id.emailTextField);

        context = rootView.getContext();

        sharedPreferencesManager = new SharedPreferencesManager(context);

        Button goButton = (Button) rootView.findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               GOButton();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    public void GOButton (){

        if (!checkIfFieldsIsEmpty()) {
            Toast.makeText(context, "Some fields are empty!", Toast.LENGTH_LONG).show();
            return;
        }
        if (!checkEmailPattren()) {
            Toast.makeText(context, "This is not valid email", Toast.LENGTH_LONG).show();
            return;
        }

        if (!checkPhoneNumberPattren()) {
            Toast.makeText(context, "This is not valid phone", Toast.LENGTH_LONG).show();
            return;

        }
        if (!checkNetworkConnection()) {
            Toast.makeText(context, "please check your internet connection", Toast.LENGTH_LONG).show();
            return;
        }
        alertDialog();

    }

    public boolean checkIfFieldsIsEmpty() {
        if (nameText.getText().toString().trim().isEmpty() ||
                phoneText.getText().toString().trim().isEmpty() ||
                emailText.getText().toString().trim().isEmpty()) {
            return false;

        }
        return true;
    }

    public boolean checkEmailPattren() {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailText.getText().toString());
        if (matcher.find()) {
            return true;
        }
        return false;

    }

    public boolean checkPhoneNumberPattren() {
        Pattern VALID_PHONE_NUMBER = Pattern.compile("^05[0-9]{8}$");
        Matcher matcher = VALID_PHONE_NUMBER.matcher(phoneText.getText().toString());
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;


    }

    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
// Add the buttons
        builder.setMessage("Are you sure this is your Phone number " + phoneText.getText());
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    ConfirmClicked();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void ConfirmClicked () throws JSONException {

        List<PropertyInfo> prop = getPropertyInfo();
        WebAPI api = new WebAPI(context, "RegisterUser", prop);
        String output = api.call();
        JSONArray data = new JSONArray(output);

        String status = data.getJSONObject(0).getString("status");

        if (status.equals("success")) {
            String id = data.getJSONObject(0).getString("userid");
            sharedPreferencesManager.insertUserID(id);
            sharedPreferencesManager.setUserName(nameText.getText().toString());
            sharedPreferencesManager.setUserEmail(emailText.getText().toString());
            sharedPreferencesManager.setUserPhone(phoneText.getText().toString());
            add();
        }else {

            Toast.makeText(context,data.getJSONObject(0).getString("message"),Toast.LENGTH_LONG).show();

        }



//        Toast.makeText(getApplicationContext(),UserInformation.userID + "    "+status, Toast.LENGTH_LONG).show();
    }

    public List<PropertyInfo> getPropertyInfo(){
        List<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();

        PropertyInfo name = new PropertyInfo();
        name.setName("name");
        name.setValue(nameText.getText().toString());// Generally array index starts from 0 not 1
        name.setType(String.class);
        propertyInfos.add(name);
        PropertyInfo phone = new PropertyInfo();
        phone.setName("phone");
        phone.setValue(phoneText.getText().toString());// Generally array index starts from 0 not 1
        phone.setType(String.class);
        propertyInfos.add(phone);
        PropertyInfo email = new PropertyInfo();
        email.setName("email");
        email.setValue(emailText.getText().toString());// Generally array index starts from 0 not 1
        email.setType(String.class);
        propertyInfos.add(email);
        PropertyInfo role = new PropertyInfo();
        role.setName("role");
        role.setValue(1);// Generally array index starts from 0 not 1
        role.setType(int.class);
        propertyInfos.add(role);

        return propertyInfos;
    }


    public void add (){
        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        VerificationFragment f = new VerificationFragment();
        fragmentTransaction.replace(R.id.fragment, f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
