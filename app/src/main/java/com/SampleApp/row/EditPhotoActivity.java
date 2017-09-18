package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by user on 28-11-2016.
 */
public class EditPhotoActivity extends Activity {
    ImageView iv_image, iv_selectImage;
    EditText edt_description;
    TextView btn_done, tv_title;
    String photoId = "";
    String description = "";
    String albumId = "";
    String groupId = "";
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(EditPhotoActivity.this);
    String imagepath = "";
    String pathtoDisplay = "";
    String isUploaded = "failure";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_editphoto);
        Intent i = getIntent();
        photoId = i.getStringExtra("photoId");
        description = i.getStringExtra("description");
        albumId = i.getStringExtra("albumId");
        groupId = i.getStringExtra("groupId");
        pathtoDisplay = i.getStringExtra("image");
        iv_image = (ImageView) findViewById(R.id.iv_image);
        edt_description = (EditText) findViewById(R.id.et_evetDesc);
        btn_done = (TextView) findViewById(R.id.btnDone);
        iv_selectImage = (ImageView) findViewById(R.id.selectimage);
        edt_description.setText(description);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Edit Photo");

        if (pathtoDisplay != "" && pathtoDisplay != null) {
            Picasso.with(EditPhotoActivity.this).load(pathtoDisplay)
                    .placeholder(R.drawable.dashboardplaceholder)
                    .into(iv_image);
        }

        iv_selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        selectImage();
                    }
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = URLEncoder.encode(edt_description.getText().toString());
                String createdBy = PreferenceManager.getPreference(EditPhotoActivity.this, PreferenceManager.GRP_PROFILE_ID);
                if (InternetConnection.checkConnection(EditPhotoActivity.this)) {

                    String url = Constant.AddUpdateAlbumPhoto;
                    url = url + "?photoId=" + photoId + "&desc=" + description + "&albumId=" + albumId + "&groupId=" + groupId + "&createdBy=" + createdBy;
                    Log.d("Touchbase", "♦♦♦♦" + url);
                    Log.d("Touchbase", "♦♦♦♦" + imagepath);
                    UploadPhotoAsyncTask task = new UploadPhotoAsyncTask(url, imagepath);
                    task.execute();


                } else {
                    Utils.showToastWithTitleAndContext(EditPhotoActivity.this, "No Internet Connection!");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Utils.popupback(EditPhotoActivity.this);
    }

    public class UploadPhotoAsyncTask extends AsyncTask<String, Object, Object> {

        String url;
        String path;
        final ProgressDialog progressDialog = new ProgressDialog(EditPhotoActivity.this, R.style.TBProgressBar);


        public UploadPhotoAsyncTask(String url, String photopath) {
            this.url = url;
            this.path = photopath;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {


            isUploaded = UploadPhotoToServer(new File(path), (url));
            return isUploaded;

        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.toString().equalsIgnoreCase("success")) {
                Intent i = new Intent();
                i.putExtra("resultforEditPhoto", result.toString());
                setResult(1, i);
                finish();
                Toast.makeText(EditPhotoActivity.this, "Photo Updated Sucessfully", Toast.LENGTH_SHORT).show();

//                Intent i = new Intent(EditPhotoActivity.this,GalleryDescription.class);
//                startActivity(i);
            } else {
                Toast.makeText(EditPhotoActivity.this, "Photo Updation Failed", Toast.LENGTH_SHORT).show();
                Log.d("TouchBase", "♦♦♦♦RESPONSE" + result);
            }
        }
    }

    public String UploadPhotoToServer(File file_path, String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("Uri", "Do file path" + file_path);

        try {

            HttpClient client = new DefaultHttpClient();
            //use your server path of php file
            org.apache.http.entity.mime.MultipartEntity reqEntity = new org.apache.http.entity.mime.MultipartEntity();
            HttpPost post = new HttpPost(url);
            if (file_path.exists()) {
                FileBody bin1 = new FileBody(file_path);
                //  Log.d("Enter", "Filebody complete " + bin1);
                reqEntity.addPart("uploaded_file", bin1);
                //reqEntity.addPart("email", new StringBody(useremail));
            }
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


    private void selectImage() {

        final CharSequence[] options;
        options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditPhotoActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 4);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /***************Image Capture***********/
        if (requestCode == 4) {


            if (resultCode == Activity.RESULT_OK) {

                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    Bitmap bt = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                    iv_image.setImageBitmap(bt);

                    imagepath = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    OutputStream outFile = null;
                    File file = new File(imagepath, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    imagepath = f.toString();
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 2) {

            if (resultCode == Activity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                final String picturePath = c.getString(columnIndex);
                imagepath = picturePath;
                c.close();
                Bitmap thumbnail = null;
                try {
                    thumbnail = (BitmapFactory.decodeFile(picturePath));
                    iv_image.setImageBitmap(thumbnail);
                } catch (Exception e) {
                    Log.d("Touchbase", "♦♦♦♦" + e.getMessage());
                    e.printStackTrace();
                }


            }
        }
    }
}
