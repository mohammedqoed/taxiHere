package com.bedetaxi.bedetaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }



    public void  getHistory(View view) throws JSONException {
        List<PropertyInfo> My_prop = getProperty();
        WebAPI WebApi = new WebAPI(getApplicationContext(),"getHistory",My_prop);
        String result = WebApi.call();
        JSONArray jsonArray = new JSONArray(result);
        getHistory(jsonArray);

    }

//
    public void getHistory(JSONArray jsonArray)throws JSONException {
        JSONArray json = jsonArray;
        if (json.getJSONObject(0).getString("status").equalsIgnoreCase("Success")) {
            JSONArray jsonArray1 = json.getJSONObject(0).getJSONArray("details");
            for (int i = 0; i<jsonArray1.length();i++){
                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                String name = jsonObject.getString("DriverName");
                String phone = jsonObject.getString("DriverPhone");
                String DateTime = jsonObject.getString("DateTime");
                String ID = jsonObject.getString("DriverID");


            }


        }
    }


    public List<PropertyInfo> getProperty(){
        List<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
            PropertyInfo UserID = new PropertyInfo();
            UserID.setName("UserID");
            UserID.setValue("1DB9AC77-3AFF-4892-A361-92A4F74978AB");// Generally array index starts from 0 not 1
            UserID.setType(String.class);
            propertyInfos.add(UserID);

        return propertyInfos;
    }

}
