package com.NEWROW.row.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.R;

/**
 * Created by USER on 10-03-2016.
 */
public class MarshMallowPermission {

    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 4;
    public static final int CONTACTS_PERMISSION_REQUEST_CODE = 5;
    public static final int READ_SMS_PERMISSION_REQUEST_CODE = 6;
    public static final int RECEIVE_SMS_PERMISSION_REQUEST_CODE = 7;
    Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermissionForRecord(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForExternalStorage(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForCamera(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForLocation(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.d("TOCHBASE","CODE -- "+result);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }
    public boolean checkPermissionForContacts(){
//        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
//        Log.d("TOCHBASE","CODE -- "+result);
//        if (result == PackageManager.PERMISSION_GRANTED){
//            return true;
//        } else {
//            return false;
//        }

        return true;
    }

    public boolean checkPermissionForReadSms(){
//        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS);
//        Log.d("TOCHBASE","CODE -- "+result);
//        if (result == PackageManager.PERMISSION_GRANTED){
//            return true;
//        } else {
//            return false;
//        }

        return false;
    }

    public void requestPermissionForRecord(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)){
            Toast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.RECORD_AUDIO},RECORD_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForExternalStorage(){
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//            Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
//        } else {
//            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
//        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            showPopup(EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForReadSms(){
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)){
//            //Toast.makeText(activity, "Read SMS permission needed to auto detect OTP sent to your registered number. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
//            showPopup(READ_SMS_PERMISSION_REQUEST_CODE);
//        }else {
//            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_REQUEST_CODE);
//
//        }

    }
    public void requestPermissionForCamera(){
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
//            Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
//        } else {
//            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
//        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
            showPopup(CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForLocation(){
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)){
//            Toast.makeText(activity, "Location permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
//        } else {
//            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
//        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)){
            showPopup(LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    public void requestPermissionForContacts(){
//        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)){
//            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_CONTACTS},CONTACTS_PERMISSION_REQUEST_CODE);
//            //Toast.makeText(activity, "Contacts permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
//        }
    }

    private void showPopup(final int module){
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.permission_popup);
        dialog.setTitle("");

        // set the custom dialog components - text, image and button
        final TextView txt_msg = (TextView) dialog.findViewById(R.id.txt_msg);

        TextView txt_yes=(TextView)dialog.findViewById(R.id.tv_yes);
        TextView txt_no=(TextView)dialog.findViewById(R.id.tv_no);

        if(module==READ_SMS_PERMISSION_REQUEST_CODE){
            txt_msg.setText("Read SMS permission needed to auto detect OTP sent to your registered number.");
        }else  if(module==EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE){
            txt_msg.setText("External Storage permission needed to save the file.");
        }else if(module==CAMERA_PERMISSION_REQUEST_CODE){
            txt_msg.setText("Camera permission needed. Please allow the permission for additional functionality.");
        }else if(module==LOCATION_PERMISSION_REQUEST_CODE){
            txt_msg.setText("Location permission needed. Please allow the permission for additional functionality.");
        }



        txt_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if(module==EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE){
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                }else  if(module==CAMERA_PERMISSION_REQUEST_CODE){
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }else  if(module==LOCATION_PERMISSION_REQUEST_CODE){
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                }
            }
        });

        txt_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
