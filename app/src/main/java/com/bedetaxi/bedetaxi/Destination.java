package com.bedetaxi.bedetaxi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Destination extends AppCompatActivity {
    ListView list;
    public static String To;
    List<destinationList> My_List = new ArrayList<>() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        destinationList one = new destinationList("AbuQash",90);
        My_List.add(one);
        destinationList two = new destinationList("Ramallah",50);
        My_List.add(two);
        destinationList three = new destinationList("Al-Teirah",160);
        My_List.add(three);
        list=(ListView)findViewById(R.id.list);
        destAdapter adapter = new destAdapter(getApplicationContext(),My_List);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                To = My_List.get(position).getName();
                Intent i = new Intent(Destination.this,MainActivity.class);
                i.putExtra("To",To);
                startActivity(i);
            }
        });

    }
}
