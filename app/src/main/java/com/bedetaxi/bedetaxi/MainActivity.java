package com.bedetaxi.bedetaxi;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;

//import com.facebook.login.LoginResult;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.models.DirectionsRoute;

//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;

//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("WrongConstant")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private MapView mapView;
    static Context mcontext;
    private MapboxMap map;
    private DirectionsRoute currentRoute;
    public static boolean IsInRequest =false;
    public static boolean HaveRequest = false;

    static Context context;
    private boolean isInFront;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;

    LocationRequest mLocationRequest;
    Marker mCurrLocation;
    SharedPreferencesManager sharedPreferencesManager;

    pickAdapter adapter;
   // CallbackManager callbackManager;
    private static final String TAG = "MainActivity";
    static List<pickUp> My_List = new ArrayList<>();
    static ListView list;
    static  Button bidditaxi;
    static  Button cancel;
    static  Button call;
    static LinearLayout mainlinear;
    static String from;
    static String to;
    static boolean isfrom ;
    Firebase tracking;
    Fragment MyFragment = new Fragment();
    Menu My_Menu;
    //LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        context = MainActivity.this;
        sharedPreferencesManager = new SharedPreferencesManager(context);

        //setContentView(R.layout.content_main);
// handle the map
        PutMap(savedInstanceState);

       // set Action Bar
        setActionBar();
// fill the main list
        FullMainList();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pickUp selected = My_List.get(position);
                String selectedName = selected.getBName();
                if (selectedName.equalsIgnoreCase(My_List.get(0).getBName())) {
                    if (latLng !=null) {
                        Intent i = new Intent(getApplicationContext(), PickUpList.class);
                        Bundle b = new Bundle();
                        b.putDouble("lat", latLng.getLatitude());
                        b.putDouble("lng", latLng.getLongitude());
                        i.putExtras(b);
                        startActivity(i);
                        My_List.clear();
                    }else if (!checkNetworkConnection()){
                            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_LONG).show();
                        }else if (!checkGPSEnabled()){
                            GPSAlert();
                    }else{
                        Toast.makeText(getApplicationContext(), "Unable to get location", Toast.LENGTH_LONG).show();
                    }


                } else if (selectedName.equalsIgnoreCase("Destination")) {
                    //  Intent i = new Intent(getApplicationContext(),Destination.class);
                    //  startActivity(i);
                }

            }
        });

        bidditaxi = (Button) findViewById(R.id.bidditaxi);
        bidditaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // dialog after press biddi taxi
                dialog();

            }
        });



    }





    public HashMap<String,String> getTaxiButtonClicked (){

        if (!checkNetworkConnection()){
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_LONG).show();
            return null;
        }

        if (!checkGPSEnabled()){
            GPSAlert();
            return null;
        }


        if (latLng == null){
            Toast.makeText(getApplicationContext(),"Unable to get location",Toast.LENGTH_LONG).show();
            return null;
        }
        HashMap<String,String> hashMap = null;
//        JSONArray data = new sendTaxiRequest().execute().get();
        List<PropertyInfo> prop = getPropertyInfo();

        try{
            WebAPI api = new WebAPI(context,"Request",prop);

        String output = api.call();
            if (output.trim().isEmpty()){
                Toast.makeText(context,"Network connection Error",Toast.LENGTH_LONG).show();
                return  null;
            }

        JSONArray data = new JSONArray(output);
        String status = data.getJSONObject(0).getString("status");
        if (status.equals("success")) {
            HaveRequest = true;
            hashMap = new HashMap<String,String>();
            JSONObject details = data.getJSONObject(0).getJSONObject("details");
            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
            double taxiLat = Double.parseDouble(details.getString("lat"));
            double taxiLng = Double.parseDouble(details.getString("lng"));

            map.addMarker(getMarkerOption(taxiLat, taxiLng, "TAXI"));
            map.animateCamera(getCameraUpdate(taxiLat, taxiLng), 7000);

//            tracking = new Firebase("https://taxihere.firebaseio.com/Drivers/Tracking");
            hashMap.put("driverID",details.getString("DriverID"));
            hashMap.put("DriverName", details.getString("DriverName"));
            hashMap.put("DriverPhone", details.getString("DriverPhone"));
            hashMap.put("VehicleType", details.getString("VehicleType"));
            hashMap.put("Distance", details.getString("Distance"));
            hashMap.put("Duration", details.getString("Duration"));
            hashMap.put("orderID",details.getString("OrderID"));
            return hashMap;

//            try{
////            getRoute(Position.fromCoordinates(taxiLng,taxiLat),Position.fromCoordinates(location.getLongitude(), location.getLatitude()));
//        } catch (ServicesException servicesException) {
//                    servicesException.printStackTrace();
//                }

        }else {
            Toast.makeText(getApplicationContext(),data.getJSONObject(0).getString("details"),Toast.LENGTH_LONG).show();
            return hashMap;
        }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();


        }


        return null;
        }

    public MarkerOptions getMarkerOption (double lat,double lng, String title){
        return  new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(title);

    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;


    }

    public boolean checkGPSEnabled (){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);


        try {
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        return false;

    }

    public List<PropertyInfo> getPropertyInfo(){
        List<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();

        PropertyInfo name = new PropertyInfo();
        name.setName("UserID");
        name.setValue(sharedPreferencesManager.getUserID());// Generally array index starts from 0 not 1
        name.setType(String.class);
        propertyInfos.add(name);
        PropertyInfo lat = new PropertyInfo();
        lat.setName("FromLat");
        lat.setValue(String.valueOf(latLng.getLatitude()));// Generally array index starts from 0 not 1
        lat.setType(String.class);
        propertyInfos.add(lat);
        PropertyInfo lng = new PropertyInfo();
        lng.setName("FromLng");
        lng.setValue(String .valueOf(latLng.getLongitude()));// Generally array index starts from 0 not 1
        lng.setType(String.class);
        propertyInfos.add(lng);
        PropertyInfo ToLat = new PropertyInfo();
        ToLat.setName("ToLat");
        ToLat.setValue(String.valueOf("31.975159"));
        ToLat.setType(String.class);
        propertyInfos.add(ToLat);
        PropertyInfo ToLng = new PropertyInfo();
        ToLng.setName("ToLng");
        ToLng.setValue(String.valueOf("35.196085"));
        ToLng.setType(String.class);
        propertyInfos.add(ToLng);
        return propertyInfos;
    }


    public void GPSAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }



    public void dialog(){
        final Dialog dialog = new Dialog(MainActivity.this);

        dialog.setContentView(R.layout.main_dialog);

        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        title.setText(getString(R.string.DialogText) + " " + PickUpList.From + " " + getString(R.string.To) + " " + Destination.To);
        Button dialog_no = (Button) dialog.findViewById(R.id.dialog_button_No);
        dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button dialog_yes = (Button) dialog.findViewById(R.id.dialog_button_Yes);
        dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final HashMap<String, String> detiles = getTaxiButtonClicked();
                if (detiles != null) {
                    MenuItem item = My_Menu.findItem(R.id.action_Done);
                    item.setVisible(true);
                    tracking = new Firebase("https://taxihere.firebaseio.com/Drivers/" + detiles.get("driverID") + "/Tracking");
                    tracking.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String lat = dataSnapshot.child("lat").getValue(String.class);
                            String lng = dataSnapshot.child("lng").getValue(String.class);
                            if (latLng != null) {
                                map.clear();
                                map.addMarker(getMarkerOption(latLng.getLatitude(), latLng.getLongitude(), "Your Location"));
                                map.addMarker(getMarkerOption(Double.parseDouble(lat), Double.parseDouble(lng), "Taxi Location"));

                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    Toast.makeText(getApplicationContext(), detiles.get("DriverName"), Toast.LENGTH_LONG).show();


                    cancel = (Button) findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            // Setting Dialog Message
                            alertDialog.setMessage("Are you sure you want to cancel the order ? ");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            IsInRequest = false;
                                            List<PropertyInfo> info = getCancelInfo(detiles.get("orderID"));
                                            WebAPI api = new WebAPI(context,"cancelRequest",info);
                                            String output = api.call();

                                            Intent i = new Intent(MainActivity.this, cancel.class);
                                            startActivity(i);
                                        }
                                    });

                            // on pressing cancel button
                            alertDialog.setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            // Showing Alert Message
                            alertDialog.show();

                        }
                    });

                    call = (Button) findViewById(R.id.call);
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+detiles.get("DriverPhone")));
                            startActivity(intent);
                        }
                    });
                    TextView DriverName = (TextView) findViewById(R.id.DriverNmae);
                    DriverName.setText(detiles.get("DriverName"));
                    TextView CarType = (TextView) findViewById(R.id.CarType);
                    CarType.setText(detiles.get("VehicleType"));
                    bidditaxi.setVisibility(View.INVISIBLE);
                    list.setVisibility(View.INVISIBLE);
                    mainlinear = (LinearLayout) findViewById(R.id.mainlinear);
                    mainlinear.setVisibility(View.VISIBLE);
                }
                IsInRequest = true;

            }
        });
        dialog.show();
    }

    public List<PropertyInfo> getCancelInfo (String id){
        List<PropertyInfo> info = new ArrayList<PropertyInfo>();
        PropertyInfo orderID = new PropertyInfo();
        orderID.setName("OrderID");
        orderID.setValue(id);// Generally array index starts from 0 not 1
        orderID.setType(String.class);

        info.add(orderID);
        return info;
    }

    public void FullMainList(){
        Intent intent = getIntent();
        My_List.clear();
        from = intent.getStringExtra("From");

        to = intent.getStringExtra("To");


        list = (ListView) findViewById(R.id.pick);

        if(from!=null){
            pickUp one = new pickUp(from,"");
            My_List.add(one);
            isfrom=true;
        }else {
            pickUp one = new pickUp(getString(R.string.PickChoose),getString(R.string.PickArt));
            My_List.add(one);
        }
        if(to!=null){
            pickUp two = new pickUp(to,"");
            My_List.add(two);
        }else {
            pickUp two = new pickUp(getString(R.string.Dest),getString(R.string.DestArt));
            My_List.add(two);
        }


         adapter = new pickAdapter(getApplicationContext(),My_List);
        notifyAdapter();

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
    public void setActionBar(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
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
    }



    public void PutMap(Bundle savedInstanceState){
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        MapboxAccountManager.start(this, getString(R.string.access_token));

        // Create supportMapFragment
        SupportMapFragment mapFragment;
        if (savedInstanceState == null) {

            // Create fragment

            final android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            LatLng patagonia = new LatLng(31.905011, 35.204488);

            // Build mapboxMap
            MapboxMapOptions options = new MapboxMapOptions();
            options.styleUrl(Style.MAPBOX_STREETS);
            options.camera(new CameraPosition.Builder()
                    .target(patagonia)
                    .zoom(16)
                    .build());

            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);

            // Add map fragment to parent container
            transaction.add(R.id.container, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                map.setMyLocationEnabled(true);
                buildGoogleApiClient();
                mGoogleApiClient.connect();

            }
        });
    }

    public void add (){
        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        HsitoryFragment f = new HsitoryFragment();
        fragmentTransaction.replace(R.id.container, f);
       // fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(MainActivity.this)
                .addApi(LocationServices.API)
                .build();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if(HaveRequest){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to cancel the order ? ");
            // On pressing Settings button
            alertDialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            IsInRequest = false;
                            Intent intent = new Intent(MainActivity.context, com.bedetaxi.bedetaxi.cancel.class);
                            startActivity(intent);
                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();

        }else
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);
            finish();
            System.exit(0);
        }
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);

        ImageView UserImage = (ImageView) findViewById(R.id.imageView);
        String Image="";
        if(sharedPreferencesManager.getImage()!=null) {
            Image = sharedPreferencesManager.getImage();
        }
        byte [] encodeByte=Base64.decode(Image, Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        if (bitmap!=null)
        UserImage.setImageBitmap(bitmap);
        getMenuInflater().inflate(R.menu.main, menu);
        this.My_Menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Done) {
            Toast.makeText(MainActivity.context, "Thank you for using our application ", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, Rating.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.HomeItem) {
            if(isInFront){

            }else {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        }else if (id == R.id.Edit_Profile_item){

            Intent i = new Intent(this,EditProfile.class);
            startActivity(i);
        }else if (id == R.id.History){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(this,My_History.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onConnected(Bundle bundle) {
        try{
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                //place marker at current position
                map.clear();
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            location.setLatitude(latLng.getLatitude());
//            location.setLongitude(latLng.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                mCurrLocation = map.addMarker(markerOptions);
            }

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000); //5 seconds
            mLocationRequest.setFastestInterval(3000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {

        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        FullMainList();
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mCurrLocation != null) {
            mCurrLocation.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        mCurrLocation = map.addMarker(markerOptions);
        map.animateCamera(getCameraUpdate(location.getLatitude(),location.getLongitude()), 7000);
    }

    @Override
    public void onStart() {
        super.onStart();
        isInFront = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isInFront = false;
    }

    public CameraUpdate getCameraUpdate(Double lat, Double lng){

        CameraPosition position =  new CameraPosition.Builder()
                .target(new LatLng(lat, lng)) // Sets the new camera position
                .zoom(15) // Sets the zoom
                .bearing(180) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        return  CameraUpdateFactory.newCameraPosition(position);

    }

}
