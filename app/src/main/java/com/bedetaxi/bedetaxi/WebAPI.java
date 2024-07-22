package com.bedetaxi.bedetaxi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlSerializer;

import java.util.List;

/**
 * Created by Alaa on 12/11/2016.
 */
public class WebAPI {
    String METHOD_NAME = "";
    public static String My_Name="";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "http://bedetaxi.cloudapp.net/BedeService/Service1.svc"; // http://192.168.1.73/Service/MunicipalityService.svc
    private static final String TAG = WebAPI.class.getSimpleName();
    public boolean isDone = false;
    public Context context;
    String SOAP_ACTION = "http://tempuri.org/IService1";
    private XmlSerializer writer;
    public boolean isWebServiceSent = false;
    public static SharedPreferences.Editor editor;
    List<PropertyInfo> input;
    public static String output = "";
    /**
     * WCFWebServiceImport constructor taht receives:
     *
     * @param context
     * @param METHOD_NAME
     */
    public WebAPI(Context context, String METHOD_NAME, List<PropertyInfo> input) {

        isWebServiceSent = true;
        this.METHOD_NAME = METHOD_NAME;
        this.context = context;
        this.input = input;
        SOAP_ACTION = SOAP_ACTION + "/" + METHOD_NAME;

    }

    public String call() {

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            for (PropertyInfo p : input) {
                request.addProperty(p);
            }

            try {
               output =  new GetData(request).execute().get();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }





        } catch (Exception e) {

            e.printStackTrace();

        }
        return output;

    }



    class GetData extends AsyncTask<Void, Void, String> {
        String result = "";
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        public GetData (SoapObject soapObject){
            envelope.dotNet = true;
            envelope.setOutputSoapObject(soapObject);

        }
        final HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,120000);
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Please Wait ... ");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            if (Looper.myLooper() == null){
                Looper.prepare();
            }

            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                result = ((SoapPrimitive) envelope.getResponse()).toString();


            } catch (Exception e) {


            }


            return result;
        }


    }




    }
