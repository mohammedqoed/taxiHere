package com.bedetaxi.bedetaxi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UpdateUserInfo extends AppCompatActivity {
    String encodedImage;
    EditText name ;
    EditText phone;
    EditText Email;
    String userName = "alaa";
    String Phone = "0000";
    String UEmail ="aa@aa.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
//        name = (EditText) findViewById(R.id.Uname);
//        phone = (EditText) findViewById(R.id.Uphone);
//        Email = (EditText) findViewById(R.id.Uemail);
    }

    public void updateUserInfo(View view) throws JSONException {



        List<PropertyInfo> My_prop = getProperty();
        WebAPI WebApi = new WebAPI(UpdateUserInfo.this,"updateUserInfo",My_prop);
        String result = WebApi.call();
        JSONArray jsonArray = new JSONArray(result);
        Update_Profile(jsonArray);
    }


    public void Update_Profile(JSONArray jsonArray)throws JSONException {
        JSONArray json= jsonArray;
        if (json.getJSONObject(0).getString("status").equalsIgnoreCase("Success")) {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

        }

    }

    public List<PropertyInfo> getProperty(){
        List<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();


//        userName = name.getText().toString();
//        Phone = phone.getText().toString();
//        UEmail = Email.getText().toString();

            PropertyInfo UserID = new PropertyInfo();
            UserID.setName("UserID");
            UserID.setValue("1DB9AC77-3AFF-4892-A361-92A4F74978AB");// Generally array index starts from 0 not 1
            UserID.setType(String.class);

            PropertyInfo name = new PropertyInfo();
            name.setName("name");
            name.setValue(userName);// Generally array index starts from 0 not 1
            name.setType(String.class);


            PropertyInfo email = new PropertyInfo();

            email.setName("email");
            email.setValue(UEmail);// Generally array index starts from 0 not 1
            email.setType(String.class);

            PropertyInfo phone = new PropertyInfo();
            phone.setName("phone");
            phone.setValue(Phone);// Generally array index starts from 0 not 1
            phone.setType(String.class);

            PropertyInfo Image = new PropertyInfo();
            Image.setName("img");
            Image.setValue(encodedImage);// Generally array index starts from 0 not 1
            Image.setType(String.class);



        propertyInfos.add(UserID);
        propertyInfos.add(name);
        propertyInfos.add(phone);
        propertyInfos.add(email);
        propertyInfos.add(Image);

        return propertyInfos;
    }

    public void chooseimage(View view){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        if (resultCode == RESULT_OK) {
            final Uri my_image = imageReturnedIntent.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(my_image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Bitmap resized = Bitmap.createScaledBitmap(selectedImage, (int) (selectedImage.getWidth() * 0.25), (int) (selectedImage.getHeight() * 0.25), true);
            encodedImage = encodeImage(resized);
            // Toast.makeText(this,"success     .....   "+encodedImage,Toast.LENGTH_LONG).show();
            Log.i("success", encodedImage);




        }
    }




    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
}
