package com.SampleApp.row.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;

import com.SampleApp.row.Data.UploadPhotoData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.UploadedPhotoModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by user on 09-11-2016.
 */
public class UploadPhotoService extends Service {
    UploadedPhotoModel model;
    ArrayList<UploadPhotoData> list;
    String isUploaded = "failure";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        list = new ArrayList<>();
        model = new UploadedPhotoModel(getBaseContext());


        list = model.getUploadedPhoto();

        Log.d("Upload Photo data","Values"+ list);
        if(list!= null&& list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                String url = Constant.AddUpdateAlbumPhoto;

                try {
                    url = url+"?photoId="+list.get(i).getPhotoId()+"&desc="+ URLEncoder.encode(list.get(i).getDescription(), "UTF-8")+"&albumId="+list.get(i).getAlbumId()+"&groupId="+list.get(i).getGroupd()+"&createdBy="+list.get(i).getCreatedBy();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                UploadPhotoAsyncTask task = new UploadPhotoAsyncTask(url,list.get(i).getPhotoPath(),list.get(i).getAutoId());
                task.execute();


//                if(isUploaded.equalsIgnoreCase("success")){
//                    boolean updated=  model.UpdateTable(list.get(i).getAutoId());
//
//                    if(list.size()-1 ==i ){
//                        Toast.makeText(this,"Photos Added Sucessfully",Toast.LENGTH_SHORT).show();
//                    }
//                }


            }
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public class UploadPhotoAsyncTask extends AsyncTask<String,Object,Object>{

        String url;
        String path;
        String autoId;



        public UploadPhotoAsyncTask(String url,String photopath,String autoId) {
            this.url = url;
            this.path = photopath;
            this.autoId = autoId;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(String... params) {


            isUploaded= UploadPhotoToServer(new File(path),url);
            return isUploaded;

        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            Utils.log(result.toString());
            if(result.toString().equalsIgnoreCase("success")){
                boolean updated=  model.UpdateTable(autoId);

               // Toast.makeText(getApplicationContext(),"Photos Added Sucessfully",Toast.LENGTH_SHORT).show();
            }
        }


    }


    public  String UploadPhotoToServer(File file_path, String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("Uri", "Do file path" + file_path);
        Utils.log(url);
        try {

            HttpClient client = new DefaultHttpClient();
            //use your server path of php file
            HttpPost post = new HttpPost(url);

            //   Log.d("ServerPath", "Path" + Constant.UploadImage + type);
            //  Log.d("ServerPath", "Path" + Constant.UploadImage+"MemberProfile");

            FileBody bin1 = new FileBody(file_path);
            //  Log.d("Enter", "Filebody complete " + bin1);

            org.apache.http.entity.mime.MultipartEntity reqEntity = new org.apache.http.entity.mime.MultipartEntity();
            reqEntity.addPart("uploaded_file", bin1);
            //reqEntity.addPart("email", new StringBody(useremail));

            post.setEntity(reqEntity);
            //  Log.d("Enter", "Image send complete");

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            //  Log.d("Enter", "Get Response");
            try {

                  final String response_str = EntityUtils.toString(resEntity);
                JSONObject jsonObj = new JSONObject(response_str);
                JSONObject ActivityResult = jsonObj.getJSONObject("LoadImageResult");
                final String status = ActivityResult.getString("status");
                if (status.equals("0")) {
                    isUploaded = ActivityResult.getString("message");
                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            Log.e("Upload Exception", "");
            e.printStackTrace();
        }
        Log.d("TOUCHBASE", "ID IN FILE UPLOAD CALL --" + isUploaded);
        return isUploaded;
    }


}
