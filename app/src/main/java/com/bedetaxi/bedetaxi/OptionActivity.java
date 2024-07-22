package com.bedetaxi.bedetaxi;


import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
*/
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
/*
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
*/

/**
 * A simple {@link Fragment} subclass.
 */
public class OptionActivity extends Fragment {

  // private LoginButton loginButton;
  // private CallbackManager callbackManager;
    View root;
  // LoginResult result;

    public OptionActivity() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


/*
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) root.findViewById(R.id.facebook);

        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                loginButton.setVisibility(View.INVISIBLE);
                add();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(),"cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity().getApplicationContext(), "Errorr", Toast.LENGTH_SHORT).show();
            }
        });
*/
    }

    private void goMainScreen() {
       add();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // callbackManager.onActivityResult(requestCode, resultCode, data);
       // loginButton.setVisibility(View.INVISIBLE);
        add();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      // FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
      //  AppEventsLogger.activateApp(getActivity().getApplication());
        root = inflater.inflate(R.layout.fragment_option, container, false);

/*
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) root.findViewById(R.id.facebook);

        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

add();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(),"cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity().getApplicationContext(), "Errorr", Toast.LENGTH_SHORT).show();
            }
        });
        */
        Button c = (Button) root.findViewById(R.id.phone);



        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add();


            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    public void add (){
        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        RegistertionFragment f = new RegistertionFragment();
        fragmentTransaction.replace(R.id.fragment, f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
