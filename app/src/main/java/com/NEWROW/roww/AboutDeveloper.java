package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Data.RotaryLibraryData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.ScreenshotUtils;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 22-05-2017.
 */

public class AboutDeveloper extends Activity {
    private Context context;
    public TextView tv_title,tv_libraryName;
    private ImageView iv_backbutton;
    private WebView webview;
    private LinearLayout parent_layout;
    private  ImageView iv_share;
    private boolean isFromRotaryLib = false;

    //Added By Gaurav
    String titleData = "Developer Info";
    public String grpID, moduleID, moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rotarylibrary_description);

        context = this;

        Bundle i = getIntent().getExtras();

        moduleName = i.getString("moduleName");
        grpID = i.getString("grpID");
        moduleID = i.getString("moduleID");

        actionbarfunction();

        init();

        loadRotaryLibraryFromServer();

      /*  Intent i = getIntent();

        if(i.hasExtra("title")){
            String name = i.getStringExtra("title");
            tv_libraryName.setText(name);
            tv_title.setText(name);
        }

        if(i.hasExtra("from_rotary_lib")){

            isFromRotaryLib = i.getBooleanExtra("from_rotary_lib",false);

            if (isFromRotaryLib) {
                iv_share.setVisibility(View.VISIBLE);
                tv_title.setText("Rotary Library");
            }else{
                iv_share.setVisibility(View.GONE);
            }
        }

        if(i.hasExtra("description")){
            String description = i.getStringExtra("description");
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadData(description,"text/html","utf-8");
        }

        iv_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                 createDynamicLink();
            }
        });

//        if(i.hasExtra("modulename")){
//            String name = i.getStringExtra("modulename");
//
//            tv_title.setText(name);
//        }
*/
    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
        tv_title.setText("About Developer");
    }

    private void init() {
        tv_libraryName = (TextView)findViewById(R.id.tv_libraryname);
        webview = (WebView)findViewById(R.id.webview);
    }

    private void createDynamicLink(){

        Uri shareUri = takeScreenshot();

        if(shareUri != null) {

            Intent shareIntent = new Intent();

            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "");
            shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);

            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            shareIntent.setType("application/pdf");//("image/jpeg");

            startActivity(shareIntent);

        } else {
            Utils.showToastWithTitleAndContext(AboutDeveloper.this,getString(R.string.msgRetry));
        }
    }

    private Uri takeScreenshot() {

        Bitmap screenBitmap = ScreenshotUtils.getScreenShot(parent_layout);

        Bitmap waterMarkPic = ScreenshotUtils.addWaterMarkLib(screenBitmap, AboutDeveloper.this, "");

        File imageFile = createPdf(waterMarkPic);//ScreenshotUtils.store(waterMarkPic,"shared_image.png",ScreenshotUtils.getMainDirectoryName(EventDetails.this));

        // Log.d("sa","file path=>"+imageFile.getPath());
        //return Uri.parse(imageFile.getPath());

        return  FileProvider.getUriForFile(this, "com.SampleApp.row.fileprovider", imageFile);
    }

    private File createPdf(Bitmap b) {

        PdfDocument document = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();
        }
        int height = b.getHeight();
        int totalPage = 1;

       /* if(height>800){
            totalPage = height/800;
        }*/

        Log.d("sa","image height=> "+height+" page="+totalPage);

        PdfDocument.PageInfo pageInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pageInfo = new PdfDocument.PageInfo.Builder(b.getWidth(), height, totalPage).create();
        }
        PdfDocument.Page page = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            page = document.startPage(pageInfo);
        }

        Canvas canvas = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            canvas = page.getCanvas();
        }

        Paint paint = new Paint();

        paint.setColor(Color.parseColor("#ffffff"));

        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(b, b.getWidth(), b.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.finishPage(page);
        }

        //ScreenshotUtils.getMainDirectoryName(EventDetails.this)

        File saveFilePath = ScreenshotUtils.getMainDirectoryName(AboutDeveloper.this);

        File dir = new File(saveFilePath.getAbsolutePath());

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String today = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        String fileName = "Rotary_India_Library_"+today+".pdf";

        File file = new File(saveFilePath.getAbsolutePath(), fileName);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                document.writeTo(new FileOutputStream(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

       /* String extr = Environment.getExternalStorageDirectory() + "/CreatePDF/";
        File file = new File(extr);

        if (!file.exists()) {
            file.mkdir();
        }

        String fileName = "ROW"+System.currentTimeMillis()+".pdf";

       File filePath = new File(extr, fileName);

       try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }*/

        // close the document
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.close();
        }

        return  file;
    }

    public void loadRotaryLibraryFromServer() {
//        Hashtable<String,String> params = new Hashtable<>();
//        params.put("grpId",grpId);
        try {

            final ProgressDialog progressDialog = new ProgressDialog(AboutDeveloper.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();

            requestData.put("SearchText", "");
            requestData.put("moduleID", moduleID);
            requestData.put("grpID", grpID);

            Log.d("this", "parameter " + requestData.toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GetEntityInfo1,
                    requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            try {
                                getResult(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.e("ROW", "♦Error : " + error);
                            error.printStackTrace();
                            Toast.makeText(AboutDeveloper.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(AboutDeveloper.this, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResult(JSONObject response) {

        try {
            Utils.log("Response:" + response);

            JSONObject jsonTBGetRotaryLibraryResult = response.getJSONObject("TBEntityInfoResult");
            final String status = jsonTBGetRotaryLibraryResult.getString("status");
            if (status.equals("0")) {
                JSONArray EntityInfoResultArray = jsonTBGetRotaryLibraryResult.getJSONArray("EntityInfoResult");
                if (EntityInfoResultArray.length() > 0) {
                    for (int i = 0; i < EntityInfoResultArray.length(); i++) {
                        JSONObject entityObject = EntityInfoResultArray.getJSONObject(i);
                        RotaryLibraryData data = new RotaryLibraryData();
                        data.setModuleName(moduleName);
                        data.setTitle(entityObject.get("enname").toString());
                        data.setDescription(entityObject.get("descptn").toString());

                        if (data.getTitle().contains(titleData)) {
                            //This only Developer Info is Added By Gaurav
                            tv_title.setText(data.getTitle());
                            String description=data.getDescription();

                            webview.getSettings().setJavaScriptEnabled(true);
                            webview.loadData(description,"text/html","utf-8");

                            break;
                        }

                    }
                }
            }


        } catch (JSONException e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

    }

}
