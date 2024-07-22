package com.bedetaxi.bedetaxi;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Registiration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registiration);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        String isVeify = sharedPreferencesManager.getVerify();
        if (isVeify.equals("true")){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_in_left);
        }
        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        OptionActivity fragment = new OptionActivity();
        fragmentTransaction.add(R.id.fragment, fragment);
        fragmentTransaction.commit();

    }


}
