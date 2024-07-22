package com.bedetaxi.bedetaxi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    Button confirm;
    EditText name;
    EditText phone;
    EditText email;
    String nameText;
    String phoneText;
    String emailText;
    ImageView userImage;
    Bitmap bitmap;
    Bitmap resized;
    SharedPreferencesManager sharedPreferencesManager;
    private int PICK_IMAGE_REQUEST = 1;
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = (EditText) findViewById(R.id.UserName);
        phone =(EditText) findViewById(R.id.PhoneNumber);
        email =(EditText) findViewById(R.id.Email);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PropertyInfo> prop = getProperty();
                WebAPI webApi = new WebAPI(EditProfile.this,"updateUserInfo",prop);
                String result = webApi.call();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Update_Profile(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


               // Intent i = new Intent(EditProfile.this,MainActivity.class);
              //  startActivity(i);
            }
        });

            userImage = (ImageView) findViewById(R.id.UserImage);
            getPic();
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    // Show only images, no videos or anything else
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    // Always show the chooser (if there are multiple options available)
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                }
            });



        Initialization();



    }


    public void Update_Profile(JSONArray jsonArray)throws JSONException {
        JSONArray json= jsonArray;
        if (json.getJSONObject(0).getString("status").equalsIgnoreCase("Success")) {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            super.onBackPressed();

        }

    }

    public List<PropertyInfo> getProperty() {

        List<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();

        nameText = name.getText().toString();
        phoneText = phone.getText().toString();
        emailText = email.getText().toString();


        PropertyInfo UserID = new PropertyInfo();
        UserID.setName("UserID");
        UserID.setValue(sharedPreferencesManager.getUserID());// Generally array index starts from 0 not 1
        UserID.setType(String.class);
        propertyInfos.add(UserID);

        sharedPreferencesManager.setUserName(nameText);

        PropertyInfo nameInfo = new PropertyInfo();
        nameInfo.setName("name");
        nameInfo.setValue(nameText);// Generally array index starts from 0 not 1
        nameInfo.setType(String.class);
        propertyInfos.add(nameInfo);

        sharedPreferencesManager.setUserEmail(emailText);

        PropertyInfo emailInfo = new PropertyInfo();
        emailInfo.setName("email");
        emailInfo.setValue(emailText);// Generally array index starts from 0 not 1
        emailInfo.setType(String.class);
        propertyInfos.add(emailInfo);

        sharedPreferencesManager.setUserPhone(phoneText);

        PropertyInfo phoneInfo = new PropertyInfo();
        phoneInfo.setName("phone");
        phoneInfo.setValue(phoneText);// Generally array index starts from 0 not 1
        phoneInfo.setType(String.class);
        propertyInfos.add(phoneInfo);

        PropertyInfo Image = new PropertyInfo();
        Image.setName("img");
        Image.setValue(sharedPreferencesManager.getImage());// Generally array index starts from 0 not 1
        Image.setType(String.class);
        propertyInfos.add(Image);

        return propertyInfos;
    }





@RequiresApi(api = Build.VERSION_CODES.FROYO)
public void getPic(){
    if(sharedPreferencesManager.getImage().equalsIgnoreCase("")){

        userImage.setImageResource(R.drawable.drawerlogo);
    }else {
        String image = sharedPreferencesManager.getImage();

            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap1= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        userImage.setImageBitmap(bitmap1);
    }
}

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                 bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                resized = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.10), (int)(bitmap.getHeight()*0.10), true);



                resized.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                String temp= Base64.encodeToString(b, Base64.DEFAULT);
                // Log.d(TAG, String.valueOf(bitmap));

                userImage.setImageBitmap(bitmap);
                sharedPreferencesManager.setUserImage(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

public void Initialization(){

    name.setText(sharedPreferencesManager.getUserName());
    phone.setText(sharedPreferencesManager.getUserphone());
    email.setText(sharedPreferencesManager.getEmail());
}



}
