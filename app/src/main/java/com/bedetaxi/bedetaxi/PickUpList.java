package com.bedetaxi.bedetaxi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PickUpList extends AppCompatActivity {
    ListView list;
    public static String From = "";
    List<pickUp> My_List = new ArrayList<>() ;
    public static Context context;
    ListAdapter adapter;
    String URL ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = PickUpList.this;
        setContentView(R.layout.activity_pick_up_list);
        Bundle b = getIntent().getExtras();
        URL = "https://maps.googleapis.com/maps/api/place/search/json?key=AIzaSyDbQSxXaU3wPX1J-6HZMl4JJn36A_TZvws&location="+b.getDouble("lat")+","+b.getDouble("lng")+"&radius=1000&sensor=false";
        new RetrieveFeedTask().execute();
    }

    private void notifyAdapter()  {
        runOnUiThread(new Runnable()  {
            public void run() {
                list.setAdapter(adapter);
                if(adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<Void,Void,String> {
        ProgressDialog progressDialog;
        String stringData;


        @Override
        protected String doInBackground(Void... params) {
            HttpRequest httpRequest = new HttpRequest();
            stringData = httpRequest.sendGetRequest(URL);


            return stringData;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PickUpList.this);
            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Please Wait ... ");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {

            if (!stringData.trim().isEmpty()) {
                JSONObject data = null;
                try {
                    data = new JSONObject(stringData);
                    String status = data.getString("status");
                    if (status.equals("OK")) {


                        JSONArray array = data.getJSONArray("results");

                        for (int i = 0; i < array.length(); i++) {
                            String name = array.getJSONObject(i).getString("name");
                            My_List.add(new pickUp(name, 0));
                        }
                    } else {
                        Toast.makeText(context, "We were unable to find any nearby places", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(context,"Network connection Error",Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
            list=(ListView)findViewById(R.id.list1);
            adapter = new ListAdapter(context,My_List);
            notifyAdapter();

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    From = My_List.get(position).getName();

                    Intent i = new Intent(PickUpList.this, MainActivity.class);
                    i.putExtra("From", From);
                    startActivity(i);

                }
            });

        }
    }



    }



