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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.NotificationDataBase.DatabaseHelper;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.ScreenshotUtils;
import com.NEWROW.row.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.NEWROW.row.Utils.PreferenceManager.isRIadminModule;


/**
 * Created by USER on 21-12-2015.
 */
public class Announcement_details extends Activity implements  View.OnClickListener {

    TextView tv_title;
    ImageView iv_backbutton, iv_announcementimg, iv_actionbtn, iv_actionbtn2, iv_share;
    EditText et_serach_announcement;
    TextView announce_title, tv_announDAte, tv_announTime, announce_desc, txt_link;
    String announcment_id;
    private ProgressBar progressbar;
    LinearLayout linear_image, ll_link;
    private String grpID = "0";
    private String memberProfileID = "0";
    private String flag_webservice = "1";
    private String isAdmin = "No";
    String moduleName = "", title = "";
    LinearLayout ll_root;
    String imageurl = "";
    boolean isFromNotification = false;
    String groupCategory = "";
    Context context;
    // variable to track event time
    private long mLastClickTime = 0;
    private String messageId_temp;
    private DatabaseHelper databaseHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.announcement_details);
        context = Announcement_details.this;

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn2 = (ImageView) findViewById(R.id.iv_actionbtn2);

        ll_root = (LinearLayout) findViewById(R.id.root);
        iv_share = (ImageView) findViewById(R.id.iv_share);

        iv_share.setVisibility(View.VISIBLE);
        //this is added by Gaurav
        iv_share.setOnClickListener(this);

        iv_actionbtn.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
        iv_actionbtn.setImageResource(R.drawable.edit); // EDIT ANNOUNCEMEBT

        iv_actionbtn.setVisibility(View.VISIBLE); // Delete ANNOUNCEMEBT
        iv_actionbtn2.setImageResource(R.drawable.delete); // Delete ANNOUNCEMEBT
        //moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Announcement");

        moduleName = getIntent().getExtras().getString("moduleName", "Announcement");

//        if (getIntent().getExtras().containsKey("moduleName")) {
//            moduleName = getIntent().getExtras().getString("moduleName");
//        }

        tv_title.setText(moduleName);

        announce_title = (TextView) findViewById(R.id.announce_title);
        txt_link = (TextView) findViewById(R.id.txt_reglink);
        ll_link = findViewById(R.id.ll_link);
        tv_announDAte = (TextView) findViewById(R.id.tv_announDAte);
        tv_announTime = (TextView) findViewById(R.id.tv_announTime);
        announce_desc = (TextView) findViewById(R.id.announce_desc);
        iv_announcementimg = (ImageView) findViewById(R.id.iv_announcementimg);
        et_serach_announcement = (EditText) findViewById(R.id.et_serach_announcement);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        linear_image = (LinearLayout) findViewById(R.id.linear_image);

        grpID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);
        memberProfileID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
        isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);

        Log.d("Touchbase", "ID ID ID :- " + grpID + " - " + memberProfileID);

        Intent intent = getIntent();

        announcment_id = intent.getStringExtra("announcemet_id");

        if (intent.hasExtra("memID")) {
            memberProfileID = intent.getStringExtra("memID");
            grpID = intent.getStringExtra("grpID");
            isAdmin = intent.getStringExtra("isAdmin");
        }

        Log.d("Touchbase", "ID ID ID AFTER :- " + grpID + " - " + memberProfileID);

        if (intent.hasExtra("fromNotification")) {
            setAnnouncementData(intent);


        } else {

            if (InternetConnection.checkConnection(getApplicationContext())) {
                // Avaliable
                webservices();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                // Not Available...
            }
        }

        init();

        adminsettings();
    }


    private void setAnnouncementData(Intent objects) {


        isFromNotification = true;

        announce_title.setText(objects.getStringExtra("ann_title"));
        announce_desc.setText(objects.getStringExtra("ann_desc"));
        tv_announDAte.setText(objects.getStringExtra("Ann_date"));

        title = objects.getStringExtra("entity name");

        groupCategory = objects.getStringExtra("group_category");

        String link = objects.getStringExtra("ann_lnk");

        if (link != null && !link.isEmpty()) {
            ll_link.setVisibility(View.VISIBLE);
            txt_link.setText(link);
        } else {
            txt_link.setText("");
            ll_link.setVisibility(View.GONE);
        }


        if (objects.hasExtra("ann_img")) {

            String img = objects.getStringExtra("ann_img");

            if (img.equalsIgnoreCase("")) {

                linear_image.setVisibility(View.GONE);

            } else {

                linear_image.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.VISIBLE);

                imageurl = img;

                Picasso.with(Announcement_details.this).load(imageurl)
                        .placeholder(R.drawable.imageplaceholder)
                        .into(iv_announcementimg, new Callback() {

                            @Override
                            public void onSuccess() {
                                progressbar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Image can't uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

        //Update Records notification count

        //update Data into Database
        messageId_temp=objects.getStringExtra("messageId");
        if (messageId_temp!=null){
            //Create Database Helper Class Object
            databaseHelpers = new DatabaseHelper(this);

            boolean notificationInsert = databaseHelpers.updateData( messageId_temp );
            Log.d("messageId_temp", "messageID ID ID AFTER :- " + messageId_temp);
            Log.d("messageId_temp", "Is Data Updated :- " + notificationInsert);


        }

    }

    private void adminsettings() {

        if (isAdmin.equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
        } else if (PreferenceManager.getPreference(context, isRIadminModule).equals("Yes")) {
            iv_actionbtn.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
        } else {
            iv_actionbtn.setVisibility(View.VISIBLE);
            iv_actionbtn2.setVisibility(View.VISIBLE);
        }

    }

    private void init() {

        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Announcement_details.this);
                View view = getLayoutInflater().inflate(R.layout.popup_confrm_delete, null);
                builder.setView(view);

                TextView tvYes = (TextView) view.findViewById(R.id.tv_yes);
                TextView tvNo = (TextView) view.findViewById(R.id.tv_no);

                final AlertDialog dialog = builder.create();

                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
                            deletewebservices();
                            //Base on event Id delete Data from NotificationList
                            //Added By Gaurav
                            //Create Database Helper Class Object
                            DatabaseHelper databaseHelpers = new DatabaseHelper(context);
                            //Delete Data into Database
                            Integer notificationDelete = databaseHelpers.deleteDataBaseOnEventId(announcment_id);

                            if (notificationDelete == 1) {
                                Log.d("PARAMETERS", "Notification Data Deleted from Annoucement List successfully");
                            }
                        } else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");

                        }
                    }
                });

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

//                final Dialog dialog = new Dialog(Announcement_details.this, android.R.style.Theme_Translucent);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.popup_confrm_delete);
//                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
//                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
//                tv_no.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//
//                    }
//                });
//                tv_yes.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        if (InternetConnection.checkConnection(getApplicationContext())) {
//                            //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
//                            deletewebservices();
//                        } else {
//                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
//
//                        }
//                    }
//                });
//
//                dialog.show();
            }

        });

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddAnnouncement.class);
                i.putExtra("announcemet_id", announcment_id);
                startActivityForResult(i, 1);
            }
        });

        ll_link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String link = txt_link.getText().toString();

                if (link != null && !link.trim().isEmpty()) {
                    Intent intent = new Intent(Announcement_details.this, OpenLinkActivity.class);
                    intent.putExtra("link", link);
                    startActivity(intent);
                }
            }
        });

      /*  iv_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //createDynamicUrl();

                Uri shareUri = takeScreenshot();

                if (shareUri != null) {

                    Intent shareIntent = new Intent();

                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);

                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setType("application/pdf");//("image/jpeg");

                    startActivity(shareIntent);
                    finish();

                } else {
                    Utils.showToastWithTitleAndContext(Announcement_details.this, getString(R.string.msgRetry));
                }
            }
        });
*/
    }

    private void createDynamicUrl() {

        final ProgressDialog progressDialog = new ProgressDialog(Announcement_details.this);
        progressDialog.setMessage("Creating share link....");
        progressDialog.show();

        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")// "http"
                .authority("www.row.com")//("www.rosteronwheels.com")//"365salads.xyz"
                .appendPath("event") // "salads"
                .appendQueryParameter("eventID", announcment_id);

        Uri linkUri = builder.build();

        Utils.log("share link=> " + linkUri.toString());

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(linkUri)
                .setDomainUriPrefix("https://rosteronwheel.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.SampleApp.row")).build())
                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder().setTitle(announce_title.getText().toString()).setDescription(announce_desc.getText().toString()).setImageUrl(Uri.parse(imageurl)).build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {

                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {

                        if (task.isSuccessful()) {

                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();

                            Log.d("demo", shortLink.toString());

                            Uri flowchartLink = task.getResult().getPreviewLink();

                            URL url = null;

                            try {
                                url = new URL(URLDecoder.decode(shortLink.toString(), "UTF-8"));
                            } catch (MalformedURLException e) {
                                progressDialog.dismiss();
                                Utils.showToastWithTitleAndContext(Announcement_details.this, getString(R.string.msgRetry));
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                progressDialog.dismiss();
                                Utils.showToastWithTitleAndContext(Announcement_details.this, getString(R.string.msgRetry));
                                e.printStackTrace();
                            }

                            Uri shareUri = takeScreenshot();

                            if (shareUri != null) {

                                Intent shareIntent = new Intent();

                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "(ROW) Click on link to know more");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, announce_title.getText().toString() + "\n" + url.toString());
                                shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);

                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                shareIntent.setType("application/pdf");//("image/jpeg");

                                progressDialog.dismiss();

                                startActivity(shareIntent);

                            } else {
                                progressDialog.dismiss();
                                Utils.showToastWithTitleAndContext(Announcement_details.this, getString(R.string.msgRetry));
                            }

                        } else {
                            progressDialog.dismiss();
                            Utils.showToastWithTitleAndContext(Announcement_details.this, getString(R.string.msgRetry));
                        }

                    }
                });

    }


    private Uri takeScreenshot() {

        Bitmap screenBitmap = ScreenshotUtils.getScreenShot(ll_root);

        Bitmap waterMarkPic = null;//ScreenshotUtils.addWaterMark(screenBitmap,Announcement_details.this,PreferenceManager.getPreference(Announcement_details.this,PreferenceManager.MY_CATEGORY),title);


        if (PreferenceManager.getPreference(context, isRIadminModule).equals("Yes")) {
            waterMarkPic = ScreenshotUtils.addWaterMark(screenBitmap, Announcement_details.this, "0", "");

        }else {

            if (isFromNotification) {
                waterMarkPic = ScreenshotUtils.addWaterMark(screenBitmap, Announcement_details.this, groupCategory, title);
            } else {
                waterMarkPic = ScreenshotUtils.addWaterMark(screenBitmap, Announcement_details.this, PreferenceManager.getPreference(Announcement_details.this, PreferenceManager.MY_CATEGORY), title);
            }
        }




        File imageFile = createPdf(waterMarkPic);//ScreenshotUtils.store(waterMarkPic,"shared_image.png",ScreenshotUtils.getMainDirectoryName(EventDetails.this));

        // Log.d("sa","file path=>"+imageFile.getPath());
        //return Uri.parse(imageFile.getPath());

        return FileProvider.getUriForFile(this, "com.SampleApp.row.fileprovider", imageFile);
    }


    private File createPdf(Bitmap b) {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(b.getWidth(), b.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();

        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(b, b.getWidth(), b.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        //ScreenshotUtils.getMainDirectoryName(EventDetails.this)

        File saveFilePath = ScreenshotUtils.getMainDirectoryName(Announcement_details.this);

        File dir = new File(saveFilePath.getAbsolutePath());

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String today = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        // String fileName = "ROW_Announcement_"+today+".pdf";

        //---***Add By Nivedita***---//
        String fileName = announce_title.getText().toString() + ".pdf";

        File file = new File(saveFilePath.getAbsolutePath(), fileName);

        try {
            document.writeTo(new FileOutputStream(file));
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
        document.close();

        return file;
    }

    public void finishActivity(View v) {
        finish();
    }


    private void deletewebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", announcment_id));
        arrayList.add(new BasicNameValuePair("type", "Announcement "));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("profileID", memberProfileID));

        flag_webservice = "2";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteByModuleName + " :- " + arrayList.toString());
        new WebConnectionAsyncAnnouncement(Constant.DeleteByModuleName, arrayList, Announcement_details.this).execute();
    }


    private void webservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new BasicNameValuePair("announID", announcment_id));
        arrayList.add(new BasicNameValuePair("grpID", grpID));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("memberProfileID", memberProfileID));
        flag_webservice = "1";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GetAnnouncementDetails + " :- " + arrayList.toString());

        new WebConnectionAsyncAnnouncement(Constant.GetAnnouncementDetails, arrayList, Announcement_details.this).execute();
    }

    @Override
    public void onClick(View v) {


        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        Log.d("Clicked", "LastClickedTime "+mLastClickTime);
        switch (v.getId()) {

            case R.id.iv_share:
                // do your code
                Uri shareUri = takeScreenshot();

                if (shareUri != null) {

                    Intent shareIntent = new Intent();

                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    shareIntent.setType("application/pdf");

                    startActivity(shareIntent);

                } else {
                    Utils.showToastWithTitleAndContext(Announcement_details.this, getString(R.string.msgRetry));
                }

                break;
        }

    }

    public class WebConnectionAsyncAnnouncement extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Announcement_details.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public WebConnectionAsyncAnnouncement(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();

            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {

            try {
                val = HttpConnection.postData(url, argList);
                Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {

                Log.d("Response", "calling getDirectorydetails");

                if (flag_webservice.equals("1")) {
                    getAnnouncementItems(result.toString());
                } else {
                    getdata(result.toString());
                }

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getAnnouncementItems(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("TBAnnounceListResult");

            final String status = EventResult.getString("status");

            if (status.equals("0")) {

                JSONArray EventListResdult = EventResult.getJSONArray("AnnounListResult");

                for (int i = 0; i < EventListResdult.length(); i++) {

                    JSONObject object = EventListResdult.getJSONObject(i);

                    JSONObject objects = object.getJSONObject("AnnounceList");

                    announce_title.setText(objects.getString("announTitle"));
                    announce_desc.setText(objects.getString("announceDEsc"));
                    tv_announDAte.setText(objects.getString("publishDateTime"));

                    title = objects.getString("clubName");

                    String link = objects.getString("reglink");

                    if (link != null && !link.isEmpty()) {
                        ll_link.setVisibility(View.VISIBLE);
                        txt_link.setText(link);
                    } else {
                        txt_link.setText("");
                        ll_link.setVisibility(View.GONE);
                    }

                    if (objects.getString("filterType").equals("3")) {
                        iv_actionbtn.setVisibility(View.GONE);
                    }

                    if (objects.has("announImg")) {

                        if (objects.getString("announImg").equals("") || objects.getString("announImg") == null) {

                            linear_image.setVisibility(View.GONE);

                        } else {

                            linear_image.setVisibility(View.VISIBLE);
                            progressbar.setVisibility(View.VISIBLE);

                            imageurl = objects.getString("announImg");

                            Picasso.with(Announcement_details.this).load(objects.getString("announImg"))
                                    .placeholder(R.drawable.imageplaceholder)
                                    .into(iv_announcementimg, new Callback() {

                                        @Override
                                        public void onSuccess() {
                                            progressbar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    }

                }

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private void getdata(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");
            final String status = EventResult.getString("status");

            if (status.equals("0")) {
                //Intent i = new Intent(Announcement_details.this,Announcement.class);
                //startActivityForResult(i,1);
                Intent intent = new Intent();
                setResult(1, intent);
                finish();//finishing activity
                // finish();
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Deleted Successfully");
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete failed, Please try again!");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            //webservices();
            if (InternetConnection.checkConnection(getApplicationContext())) {
                // Avaliable
                webservices();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                // Not Available...
            }
            //String message=data.getStringExtra("MESSAGE");
            // textView1.setText(message);
        }
    }

}
