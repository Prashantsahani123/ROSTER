package com.SampleApp.row.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;

import com.SampleApp.row.Data.CountryData;
import com.SampleApp.row.Data.ProfileData;
import com.SampleApp.row.R;


/**
 * Created by USER on 23-02-2016.
 */
public class Utils {

    //---------------- Variable
    public static ArrayList<ProfileData> profilearraylist = new ArrayList<ProfileData>();
    public static ArrayList<CountryData> countryarraylist = new ArrayList<CountryData>();
    public static String id = "0";
    public static String flag_dashboardWebCall = "0";
    public static String smsCount = "0";



    //---------------- Variable
    public static String doFileUpload(File file_path, String type) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("Uri", "Do file path" + file_path);

        try {

            HttpClient client = new DefaultHttpClient();
            //use your server path of php file
            HttpPost post = new HttpPost(Constant.UploadImage + type);

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
                    id = ActivityResult.getString("ImageID");
                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            Log.e("Upload Exception", "");
            e.printStackTrace();
        }
        Log.d("TOUCHBASE", "ID IN FILE UPLOAD CALL --" + id);
        return id;
    }


    public static void showToastWithTitleAndContext(Context context, String title) {
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }

    //http://192.168.2.224:8065/api/Member/UploadProfilePhoto?ProfileID=36&GrpID=1
    public static String doFileUploadForProfilePic(File file_path, String ProfileID, String GrpID,String type) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("Uri", "Do file path" + file_path);

        try {

            HttpClient client = new DefaultHttpClient();
            //use your server path of php file
            HttpPost post = new HttpPost(Constant.UploadProfilePhoto + "ProfileID=" + ProfileID + "&GrpID=" + GrpID + "&Type=" + type);

            //    Log.d("ServerPath", "Path" + Constant.UploadProfilePhoto + "ProfileID=" + ProfileID + "&GrpID=" + GrpID);
            //  Log.d("ServerPath", "Path" + Constant.UploadImage+"MemberProfile");

            FileBody bin1 = new FileBody(file_path);
            //   Log.d("Enter", "Filebody complete " + bin1);

            org.apache.http.entity.mime.MultipartEntity reqEntity = new org.apache.http.entity.mime.MultipartEntity();
            reqEntity.addPart("uploaded_file", bin1);
            //reqEntity.addPart("email", new StringBody(useremail));

            post.setEntity(reqEntity);
            //   Log.d("Enter", "Image send complete");

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            //    Log.d("Enter", "Get Response");
            try {

                final String response_str = EntityUtils.toString(resEntity);
                Log.d("Enter", "Get Response VALUE :- " + response_str);
                JSONObject jsonObj = new JSONObject(response_str);
                JSONObject ActivityResult = jsonObj.getJSONObject("UploadImageResult");
                final String status = ActivityResult.getString("status");
                if (status.equals("0")) {
                    id = "0"; // Sucess

                } else {
                    id = "1"; // Fail
                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
                id = "1"; // Fail
            }
        } catch (Exception e) {
            Log.e("Upload Exception", "" + e.toString());
            e.printStackTrace();
            id = "1"; // Fail
        }
        Log.d("TOUCHBASE", "ID IN FILE UPLOAD CALL --" + id);
        return id;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static String doPdfFileUpload(File file_path, String type) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("Uri", "Do file path------" + file_path);

        try {

            HttpClient client = new DefaultHttpClient();
            //use your server path of php file


            //HttpPost post = new HttpPost(Constant.UploadImage+type);
            // Log.d("ServerPath", "Path" + Constant.UploadImage+type);

            HttpPost post = new HttpPost(Constant.UploadAllDocs + type);


            //  Log.d("ServerPath", "Path" + Constant.UploadImage+"MemberProfile");

            FileBody bin1 = new FileBody(file_path);
            Log.d("Enter", "Filebody complete " + bin1);

           /* org.apache.http.entity.mime.MultipartEntity reqEntity = new org.apache.http.entity.mime.MultipartEntity();
            reqEntity.addPart("uploaded_file", bin1);*/
            //reqEntity.addPart("email", new StringBody(useremail));

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("file", file_path, ContentType.create("*/*"), file_path.getName())
                    .build();

            //    Log.d("Enter-----------", "Get Response----------" + reqEntity);

            post.setEntity(reqEntity);
            //     Log.d("Enter", "Image send complete");

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            //     Log.d("Enter", "Get Response");
            try {

                final String response_str = EntityUtils.toString(resEntity);
                Log.d("Enter", "Get Response : - " + response_str);
                JSONObject jsonObj = new JSONObject(response_str);
                JSONObject ActivityResult = jsonObj.getJSONObject("LoadImageResult");
                final String status = ActivityResult.getString("status");
                if (status.equals("0")) {
                    id = ActivityResult.getString("ImageID");

                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            Log.e("Exception", "" + e.toString());
            e.printStackTrace();
        }
        Log.d("TOUCHBASE", "ID IN FILE UPLOAD CALL --" + id);
        return id;
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////


    public static String getPath(final Context context, final Uri uri) {

        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /////////////----------------UDIU
    public static String getAndroidSecureId(Context context) {

        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        return android_id;
    }
    /////////////----------------UDIU

    //// -----------------POPUP BACK
    public static void popupback(final Context ctx) {
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_confrm_delete);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
        tv_line1.setText("Are you sure you want to go back? All your data will be lost.");
        tv_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((Activity) ctx).finish();


            }
        });

        dialog.show();
    }



    public static Dialog loadingDialog(final Context ctx) {
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_progress);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);
        tvMessage.setText(R.string.loadingMessage);
        return dialog;
    }

    public static Dialog loadingDialog(final Context ctx, String msg) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_progress);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);
        tvMessage.setText(msg);
        dialog.setCancelable(false);
        return dialog;
    }

    public static void simpleMessage(final Context ctx, String msg, String btnLabel, View.OnClickListener listener) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_simple_message);
        //TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
        tv_line1.setText(msg);
        tv_yes.setText(btnLabel);
        tv_yes.setOnClickListener(listener);
        dialog.show();
    }

    //// -----------------POPUP BACK


    /*********************************
     * Crop Image
     ****************************************/


    private Bitmap imageOrientationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }

        return bitmap;
    }

    public static Uri getImageUri(Bitmap image, Context ctx) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), image, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Uri contentUri, Context ctx) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};

            Cursor cursor = ctx.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    public static String getMonth(String month) {
        Hashtable<String,  String> months = new Hashtable<String, String>();

        months.put("01", "Jan");
        months.put("02", "Feb");
        months.put("03", "Mar");
        months.put("04", "Apr");
        months.put("05", "May");
        months.put("06", "Jun");
        months.put("07", "Jul");
        months.put("08", "Aug");
        months.put("09", "Sep");
        months.put("10", "Oct");
        months.put("11", "Nov");
        months.put("12", "Dec");
        return months.get(month);
    }

    public static void log(String message) {
        Log.e("Touchbase", "♦♦♦♦"+message);
    }

    final public static String implode(String glue, List<String> array)
    {
        boolean first = true;
        StringBuilder str = new StringBuilder();
        for (String s : array) {
            if (! s.trim().equals("")) {
                if (!first) str.append(glue);
                str.append(s);
                first = false;
            }
        }
        return str.toString();
    }

    public static ArrayList<CountryData> getCountryList(Context context) {
        ArrayList<CountryData> list = new ArrayList<>();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.countries);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer buffer = new StringBuffer();
            String line = new String("");
            while( (line = br.readLine()) != null ) {
                buffer.append(line);
            }
            br.close();
            is.close();
            String val = new String(buffer);
            try {
                //Utils.log("Country list : "+val);
                JSONObject jsonObj = new JSONObject(val);
                Utils.log(jsonObj.toString());
                JSONObject ActivityResult = jsonObj.getJSONObject("TBCountryResult");
                final String status = ActivityResult.getString("status");
                if (status.equals("0")) {

                    JSONArray grpsarray = ActivityResult.getJSONArray("CountryLists");
                    for (int i = 0; i < grpsarray.length(); i++) {
                        JSONObject object = grpsarray.getJSONObject(i);
                        JSONObject objects = object.getJSONObject("GrpCountryList");

                        CountryData gd = new CountryData();
                        gd.setCountryId(objects.getString("countryId").toString());
                        gd.setCountryCode(objects.getString("countryCode").toString());
                        gd.setCountryName(objects.getString("countryName").toString());
                        list.add(gd);
                    }
                    //adapter_countryData = new SelectCountryAdapter(SelectCountry.this, R.layout.select_country_item, list_countryData);
                    //listview.setAdapter(adapter_countryData);
                }
            } catch (Exception e) {
                Utils.log("Error is : "+e);
                e.printStackTrace();
            }
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }

        return list;
    }

    public static String getCountryCode(Context context, String countryID) {
        ArrayList<CountryData> list = getCountryList(context);
        String countryCode = "";
        for (CountryData cd:list) {
            if ( cd.getCountryId().equals(countryID)) {
                return cd.getCountryCode();
            }
        }
        return countryCode;
    }

    final static Pattern EMAIL_PATTERN = Pattern.compile( "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+");


    public static boolean isValidEmailId(String emailId) {
        return EMAIL_PATTERN.matcher(emailId).matches();
    }

}
