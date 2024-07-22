package com.bedetaxi.bedetaxi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HsitoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HsitoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HsitoryFragment extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<String> HistoryData;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView HistoryListt;
    View view ;
    List<String> test;
    ArrayAdapter<String> adapter;

    private OnFragmentInteractionListener mListener;

    public HsitoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HsitoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HsitoryFragment newInstance(String param1, String param2) {
        HsitoryFragment fragment = new HsitoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hsitory, container, false);

        // Inflate the layout for this fragment
        try {
            getHistory();
             HistoryListt = (ListView) view.findViewById(R.id.list_history);
            test = new ArrayList<>();
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            adapter =
                    new ArrayAdapter<String>(MainActivity.context, android.R.layout.simple_list_item_1, test);
            // HistoryAdapter adapter = new HistoryAdapter(view.getContext(),test);
            HistoryListt.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.fragment_hsitory, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            getHistory();
            HistoryListt = (ListView) view.findViewById(R.id.list_history);
            test = new ArrayList<>();
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            test.add("moadadasd");
            adapter =
                    new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, test);
            // HistoryAdapter adapter = new HistoryAdapter(view.getContext(),test);
            HistoryListt.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void  getHistory() throws JSONException {
        List<PropertyInfo> My_prop = getProperty();
        WebAPI WebApi = new WebAPI(view.getContext(),"getHistory",My_prop);
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
