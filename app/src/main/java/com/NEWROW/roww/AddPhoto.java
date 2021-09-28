package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by user on 16-09-2016.
 */
public class AddPhoto extends Activity {
    ArrayList<String> listOfSelectedPhotos = new ArrayList<String>();
    ImageView image;

     Bitmap  bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_addphoto);
        image = (ImageView)findViewById(R.id.image);


        Intent i = getIntent();
        listOfSelectedPhotos = i.getStringArrayListExtra("SelectedPhotoList");
        if(listOfSelectedPhotos!= null && listOfSelectedPhotos.size()>0){
            for(int j = 0;j<listOfSelectedPhotos.size();j++){

                try {

                      bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(listOfSelectedPhotos.get(j)));

                }catch(Exception e){

                }
                String picturePath = listOfSelectedPhotos.get(j);
                Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);
                if(j==0){
                    image.setImageBitmap(bitmap);
                }
            }
        }
    }
}
