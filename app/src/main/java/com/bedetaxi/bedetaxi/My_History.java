package com.bedetaxi.bedetaxi;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;


public class My_History extends Activity {
    ListView HistoryListt;
    List<String> HistoryData;
    View view ;
    List<String> test;
   // ArrayAdapter<String> adapter;
    HistoryAdapter adapter;
    private Handler handler;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__history);

        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(false) ;

        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.main_logo);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        try {
            getHistory();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HistoryListt = (ListView) findViewById(R.id.list_history);
        adapter = new HistoryAdapter(My_History.this,HistoryData);
        HistoryListt.setAdapter(adapter);
    }



    public void  getHistory() throws JSONException {
        List<PropertyInfo> My_prop = getProperty();
        WebAPI WebApi = new WebAPI(My_History.this,"getHistory",My_prop);
        String result = WebApi.call();
        JSONArray jsonArray = new JSONArray(result);
        getHistory(jsonArray);

    }

    //
    public void getHistory(JSONArray jsonArray)throws JSONException {
        JSONArray json = jsonArray;
        HistoryData = new ArrayList<>();
        if (json.getJSONObject(0).getString("status").equalsIgnoreCase("Success")) {
            JSONArray jsonArray1 = json.getJSONObject(0).getJSONArray("details");
            for (int i = 0; i<jsonArray1.length();i++){
                JSONObject jsonObject = jsonArray1.getJSONObject(i);
                String name = jsonObject.getString("DriverName");
                String phone = jsonObject.getString("DriverPhone");
                String DateTime = jsonObject.getString("DateTime");
                String ID = jsonObject.getString("DriverID");

                HistoryData.add(DateTime);
            }




        }
    }


    public List<PropertyInfo> getProperty(){
        List<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
        PropertyInfo UserID = new PropertyInfo();
        UserID.setName("UserID");
        UserID.setValue("78093619-CF9C-49DF-99C9-9DA8AB03013F");// Generally array index starts from 0 not 1
        UserID.setType(String.class);
        propertyInfos.add(UserID);

        return propertyInfos;
    }



}
