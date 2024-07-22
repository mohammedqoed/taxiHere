package com.bedetaxi.bedetaxi;

/**
 * Created by LENOVO on 12/25/2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

    private Context context;
    private List<pickUp> MList;




    public ListAdapter(Context mContext, List<pickUp> mList1) {
        this.context = mContext;
        this.MList = mList1;
    }


    @Override
    public int getCount() {
        return MList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int i, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = LayoutInflater.from(context).inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration


            title.setText(MList.get(i).getName());
            duration.setText(MList.get(i).getDistance()+"KM");


        return vi;
    }
}