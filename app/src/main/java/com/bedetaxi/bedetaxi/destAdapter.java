package com.bedetaxi.bedetaxi;

/**
 * Created by LENOVO on 12/26/2016.
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

public class destAdapter extends BaseAdapter {

    private Context context;
    private List<destinationList> MList;




    public destAdapter(Context mContext, List<destinationList> mList1) {
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
            vi = LayoutInflater.from(context).inflate(R.layout.dest_layout, null);

        TextView title = (TextView)vi.findViewById(R.id.title2); // title
        TextView duration = (TextView)vi.findViewById(R.id.duration2); // duration


        title.setText(MList.get(i).getName());
        duration.setText(MList.get(i).getDistance()+"KM");


        return vi;
    }
}