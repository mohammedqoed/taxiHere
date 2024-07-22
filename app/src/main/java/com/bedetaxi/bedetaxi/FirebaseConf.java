package com.bedetaxi.bedetaxi;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Alaa on 12/12/2016.
 */
public class FirebaseConf extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
