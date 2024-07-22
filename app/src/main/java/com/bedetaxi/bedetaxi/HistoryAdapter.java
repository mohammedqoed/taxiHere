package com.bedetaxi.bedetaxi;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LENOVO on 1/23/2017.
 */

public class HistoryAdapter extends BaseAdapter {


    private Context context;
    private List<String> MList1;




    public HistoryAdapter(Context mContext, List<String> mList1) {
        this.context = mContext;
        this.MList1 = mList1;
    }








    @Override
    public int getCount() {
        return MList1.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View vi=convertView;
        if(convertView==null)
            vi = LayoutInflater.from(context).inflate(R.layout.history_row, null);



        TextView art = (TextView)vi.findViewById(R.id.artist1h);

        art.setText(MList1.get(position));



        return vi;
    }
}
