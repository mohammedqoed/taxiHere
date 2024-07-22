package com.bedetaxi.bedetaxi;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static  com.bedetaxi.bedetaxi.PickUpList.From;

/**
 * Created by LENOVO on 12/26/2016.
 */

public class pickAdapter extends BaseAdapter{

    private Context context;
    private List<pickUp> MList;




    public pickAdapter(Context mContext, List<pickUp> mList1) {
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public View getView(int i, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = LayoutInflater.from(context).inflate(R.layout.pick_layout, null);

        final TextView title = (TextView)vi.findViewById(R.id.title1); // title
        TextView art = (TextView)vi.findViewById(R.id.artist1);
        ImageView imageView = (ImageView) vi.findViewById(R.id.list_image);
        title.setText(MList.get(i).getBName());
        art.setText(MList.get(i).getSecound());
        String name = MList.get(i).getBName();
        String req = "Destination";

           if (req.equalsIgnoreCase(name)){
               imageView.setImageResource(R.drawable.dest);

           }


        return vi;
    }
}
