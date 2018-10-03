package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.AlbumPhotoAdapter;
import com.SampleApp.row.Data.AlbumData;
import com.SampleApp.row.Data.AlbumPhotoData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.TBPrefixes;
import com.SampleApp.row.sql.AlbumPhotosMasterModel;
import com.SampleApp.row.sql.GalleryMasterModel;
import com.github.clans.fab.FloatingActionMenu;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GalleryDescription extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final int STATE_UP = 1, STATE_DOWN = 2;
    int buttonState = STATE_DOWN;

    View descriptionWrapper;

    String fabOpen = "close";
/*    AppBarLayout layout;
    CollapsingToolbarLayout toolbarLayout;*/

    TextView tv_title, tv_minititle, tv_description, tv_dop, tv_cop, tv_beneficiary, tv_manPower, tv_noOfRotarians;
    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton addPhoto, deletePhoto, editAlbum;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

    ArrayList<String> selectedImageList;

    public static final int SELECT_PICTURE = 2;
    static Bitmap bitmap;
    String updatedOn = "";
    String albumId = "";
    //    ImageView ivUpDown;
    FloatingActionButton ivUpDown;
    AlbumPhotosMasterModel albumPhotosMasterModel;
    private long grpId;
    ArrayList<AlbumPhotoData> albumPhotolist = new ArrayList<AlbumPhotoData>();
    AlbumPhotoAdapter adapter;
    GridView gv;
    String title;
    String description;
    String albumimage;

    int mode = 1;
    boolean isinUpdatemode = false;
    static final int EDITALBUM = 1;
    GalleryMasterModel albumModel;
    String moduleId = "", fromShowcase = "1";
    LinearLayout ll_desc;
    AlbumData data = new AlbumData();
    ArrayList<AlbumPhotoData> newAlbums;
    String currencyType = "";
    LinearLayout ll_noOfRotarians, ll_timespent, ll_bene, ll_cop, ll_dop, ll_details;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
    ImageView iv_actionbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gallery_description_new);
        Log.e("Album", "Inside album now");

        if (PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID) != null) {
            grpId = Long.parseLong(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID));
        }

        albumPhotosMasterModel = new AlbumPhotosMasterModel(this);
        descriptionWrapper = findViewById(R.id.descriptionWrapper);

        gv = (GridView) findViewById(R.id.gv);
        ll_desc = (LinearLayout) findViewById(R.id.ll);
        Intent i = getIntent();
        title = i.getStringExtra("albumname");
        description = i.getStringExtra("albumDescription");
        albumId = i.getStringExtra("albumId");
        albumimage = i.getStringExtra("albumImage");
        fromShowcase = i.getExtras().getString("fromShowcase");

        data = (AlbumData) i.getSerializableExtra("albumData");
        title = data.getTitle().toString();
        description = data.getDescription().toString();
        albumId = data.getAlbumId();
        albumimage = data.getImage();

        selectedImageList = new ArrayList<String>();

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_minititle = (TextView) findViewById(R.id.tv_minititle);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setImageResource(R.drawable.edit);
        iv_actionbtn.setVisibility(View.VISIBLE);

        ll_dop = (LinearLayout) findViewById(R.id.ll_dop);
        ll_cop = (LinearLayout) findViewById(R.id.ll_cop);
        ll_bene = (LinearLayout) findViewById(R.id.ll_bene);
        ll_timespent = (LinearLayout) findViewById(R.id.ll_timespent);
        ll_noOfRotarians = (LinearLayout) findViewById(R.id.ll_noOfRotarians);
        ll_details = (LinearLayout) findViewById(R.id.ll_details);

        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_dop = (TextView) findViewById(R.id.tv_dop);
        if (data.getProject_date().toString().isEmpty()) {
            ll_dop.setVisibility(View.GONE);
        }

        if (data.getShareType().equalsIgnoreCase("0")) {
            ll_details.setVisibility(View.GONE);
        } else {
            ll_details.setVisibility(View.VISIBLE);
        }

        String date = data.getProject_date().toString();
        if (date != null && !date.isEmpty()) {
            try {
                Date copDate = sdf.parse(date);
                tv_dop.setText(sdf1.format(copDate));
            } catch (ParseException e) {
                tv_dop.setText(data.getProject_date().toString());
                e.printStackTrace();
            }
        }

        tv_cop = (TextView) findViewById(R.id.tv_cop);

        if (data.getCost_of_project_type().equalsIgnoreCase("1")) {
            currencyType = "\u20B9";
        } else if (data.getCost_of_project_type().equalsIgnoreCase("2")) {
            currencyType = "$";
        } else {
            currencyType = "";
        }
        if (data.getProject_cost().toString().isEmpty()) {
            ll_cop.setVisibility(View.GONE);
        }

        if (data.getBeneficiary().toString().isEmpty()) {
            ll_bene.setVisibility(View.GONE);
        }
        if (data.getWorking_hour().toString().isEmpty()) {
            ll_timespent.setVisibility(View.GONE);
        }

        if (data.getNoOfRotarians().toString().isEmpty()) {
            ll_noOfRotarians.setVisibility(View.GONE);
        }

//        tv_cop.setText(data.getProject_cost().toString());
        tv_cop.setText(data.getProject_cost().toString() + " " + currencyType);
        tv_beneficiary = (TextView) findViewById(R.id.tv_beneficiary);
        tv_beneficiary.setText(data.getBeneficiary().toString());
        tv_manPower = (TextView) findViewById(R.id.tv_manHrSpent);
//        tv_manPower.setText(data.getWorking_hour().toString());
        tv_manPower.setText(data.getWorking_hour().toString() + " " + data.getWorking_hour_type());
        tv_noOfRotarians = (TextView) findViewById(R.id.tv_noOfRotarians);
        tv_noOfRotarians.setText(data.getNoOfRotarians().toString());
        //tv_title.setText(Html.fromHtml("<Html><body><center><font size=\"2\">Gallery</font> <br><font size=\"1\">" + title + "</font></center></body></Html>"));
        tv_title.setText(title);

        //tv_minititle.setVisibility(View.VISIBLE);
        tv_minititle.setText(title);
        Log.e("Description", description);


        tv_description.setText(description);

        /*layout = (AppBarLayout) findViewById(R.id.app_bar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);*/

        ivUpDown = (FloatingActionButton) findViewById(R.id.fab1);

        albumModel = new GalleryMasterModel(this);
        moduleId = PreferenceManager.getPreference(this, PreferenceManager.MODULE_ID);

       // final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (fabOpen.equalsIgnoreCase("close")) {
//                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
//                    layoutParams.height = layoutParams.WRAP_CONTENT;
//                    layout.setLayoutParams(layoutParams);
//                    fabOpen = "open";
//                    fab.setImageResource(R.drawable.f_up);
//                    toolbarLayout.setBackgroundColor(getResources().getColor(R.color.white));
//                } else {
//                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
//                    layoutParams.height = 160;
//                    layout.setLayoutParams(layoutParams);
//                    fabOpen = "close";
//                    fab.show();
//                    fab.setImageResource(R.drawable.f_down);
//                    toolbarLayout.setBackgroundColor(getResources().getColor(R.color.white));
//                }
//            }
//        });

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        addPhoto = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        deletePhoto = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        editAlbum = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);


        if (fromShowcase.equalsIgnoreCase("0")) {
            iv_actionbtn.setVisibility(View.GONE);
            //materialDesignFAM.setVisibility(View.GONE);
        } else {
            iv_actionbtn.setVisibility(View.VISIBLE);
            //materialDesignFAM.setVisibility(View.VISIBLE);
        }


        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (!marshMallowPermission.checkPermissionForCamera()) {
                        marshMallowPermission.requestPermissionForCamera();
                    } else {
                        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                            marshMallowPermission.requestPermissionForExternalStorage();
                        } else {
                            materialDesignFAM.close(false);

//                        Toast.makeText(GalleryDescription.this, "Maximum 5 images are allowed", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent();
//                        intent.setType("image/*");
//                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
//
//
////                      Intent i = new Intent(GalleryDescription.this,AlbumFolderPage.class);
////                      startActivity(i);
                            if (gv.getCount() < 5) {
                                Intent i = new Intent(GalleryDescription.this, AddPhotoActivity.class);
                                i.putExtra("albumId", albumId);
                                i.putExtra("count", gv.getCount());
                                startActivity(i);
                            }else {
                                    Toast.makeText(GalleryDescription.this, "Maximum 5 photos are allowed.", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                }

        });


        checkadminrights();
        //loadFromDB();
//        if (InternetConnection.checkConnection(this)) {
//            webservices();
//            Log.d("---------------", "Check for update gets called------");
//        } else {
//            Toast.makeText(this, "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
//        }
        init();
    }

    private void checkadminrights() {
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN) != null) {
            if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
                iv_actionbtn.setVisibility(View.GONE);
                //materialDesignFAM.setVisibility(View.GONE);
            }
        } else {
            iv_actionbtn.setVisibility(View.GONE);
            //materialDesignFAM.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (InternetConnection.checkConnection(this)) {
            webservices();
            Log.d("---------------", "Check for update gets called------");
        } else {
            Toast.makeText(this, "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
        }
    }


    public void init() {
        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupType = PreferenceManager.getPreference(GalleryDescription.this, PreferenceManager.MY_CATEGORY, "1");
                if (groupType.equals("" + Constant.GROUP_CATEGORY_DT)) {
                    Intent i = new Intent(GalleryDescription.this, DTEditAlbumActivity.class);
                    i.putExtra("albumId", albumId);
                    i.putExtra("description", description);
                    i.putExtra("albumImage", albumimage);
                    i.putExtra("albumname", title);
                    startActivityForResult(i, EDITALBUM);
                    finish();
                } else {
                    if (InternetConnection.checkConnection(GalleryDescription.this)) {
                        Intent i = new Intent(GalleryDescription.this, EditAlbumActivity.class);
                        i.putExtra("albumId", albumId);
                        i.putExtra("description", description);
                        i.putExtra("albumImage", albumimage);
                        i.putExtra("albumname", title);
                        startActivityForResult(i, EDITALBUM);
                        finish();
                    } else {
                        Toast.makeText(GalleryDescription.this, "No internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        ivUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonState == STATE_UP) {
                    ivUpDown.setImageDrawable(getResources().getDrawable(R.drawable.g_down));
                    buttonState = STATE_DOWN;
                    int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    ll_desc.getLayoutParams().height = dimensionInDp;
                    ll_desc.requestLayout();
//                    LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.75f );
//                    LinearLayout.LayoutParams gvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.25f );
//                    descriptionWrapper.setLayoutParams(descParams);
//                    gv.setLayoutParams(gvParams);
                    //imageWrapperLayout.setLayoutParams(imageParams);
                    //svDescription.setLayoutParams(textParams);
                } else {
                    ivUpDown.setImageDrawable(getResources().getDrawable(R.drawable.g_up));
                    buttonState = STATE_UP;
                    int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                    ll_desc.getLayoutParams().height = dimensionInDp;
                    ll_desc.requestLayout();
//                    LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.6f );
//                    LinearLayout.LayoutParams gvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.4f );
//                    descriptionWrapper.setLayoutParams(descParams);
//                    gv.setLayoutParams(gvParams);

                    //imageWrapperLayout.setLayoutParams(imageParams);
                    //svDescription.setLayoutParams(textParams);
                }
            }
        });

        deletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 0;
                materialDesignFAM.close(false);
                materialDesignFAM.setVisibility(View.GONE);
                //adapter = new AlbumPhotoAdapter(GalleryDescription.this, albumPhotolist, "0");
                adapter = new AlbumPhotoAdapter(GalleryDescription.this, newAlbums, "0");
                gv.setAdapter(adapter);
            }
        });

        editAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDesignFAM.close(false);

                String groupType = PreferenceManager.getPreference(GalleryDescription.this, PreferenceManager.MY_CATEGORY, "1");
                if (groupType.equals("" + Constant.GROUP_CATEGORY_DT)) {
                    Intent i = new Intent(GalleryDescription.this, DTEditAlbumActivity.class);
                    i.putExtra("albumId", albumId);
                    i.putExtra("description", description);
                    i.putExtra("albumImage", albumimage);
                    i.putExtra("albumname", title);
                    startActivityForResult(i, EDITALBUM);
                    finish();
                } else {
                    if (InternetConnection.checkConnection(GalleryDescription.this)) {
                        Intent i = new Intent(GalleryDescription.this, EditAlbumActivity.class);
                        i.putExtra("albumId", albumId);
                        i.putExtra("description", description);
                        i.putExtra("albumImage", albumimage);
                        i.putExtra("albumname", title);
                        startActivityForResult(i, EDITALBUM);
                        finish();
                    } else {
                        Toast.makeText(GalleryDescription.this, "No internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SELECT_PICTURE) {

            if (resultCode == Activity.RESULT_OK) {

                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        String path = uri.getPath();
                        //In case you need image's absolute path
                        // String path = getRealPathFromURI(GalleryDescription.this, uri);

                        if (selectedImageList.size() <= 5) {
                            selectedImageList.add(uri.toString());
                        } else {

                        }
                    }
                }
            }
        } else if (requestCode == EDITALBUM) {
            if (resultCode == 1) {

                String result = data.getStringExtra("resultForEditAlbum");
                if (result != null && result != "") {
                    if (InternetConnection.checkConnection(this)) {
                        //checkForUpdateForAlbums();
                        webservices();
                    } else {
                        Toast.makeText(this, "No internet connection.Cannot fetch Updated Album Records", Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }


//                Uri selectedImage = data.getData();
//                String[] filePath = {MediaStore.Images.Media.DATA};
//                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
//                //if(c!= null&& c.getCount()>0) {
//                    c.moveToFirst();
//                    do {
//                        int columnIndex = c.getColumnIndex(filePath[0]);
//                        String picturePath = c.getString(columnIndex);
//                        if(selectedImageList.size()<=5) {
//                            selectedImageList.add(picturePath);
//                        }else{
//
//                        }
//                    }while (c.moveToNext());
    // }
    // else{
    //   Toast.makeText(GalleryDescription.this,"Unable to get Image and filepath of selected Image"+ c.getCount(),Toast.LENGTH_SHORT).show();
    // }


//        Intent i = new Intent(GalleryDescription.this, AddPhoto.class);
//        i.putStringArrayListExtra("SelectedPhotoList", selectedImageList);
//        startActivity(i);
//
//    }

//    public String getRealPathFromURI(Context context, Uri contentUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = { MediaStore.Images.Media.DATA };
//            cursor = context.getContentResolver().query(contentUri, proj, null,
//                    null, null);
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String str = cursor.getString(column_index);
//            return cursor.getString(column_index);
//
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }


    public void loadFromDB() {

        Log.d("Touchbase", "Trying to load Photos inside album from local db");
        albumPhotolist = albumPhotosMasterModel.getAlbumsPhoto(albumId);

        boolean isDataAvailable = albumPhotosMasterModel.isDataAvailable(albumId);

        Log.e("DataAvailable", "Data available : " + isDataAvailable);

        if (!isDataAvailable) {
            Log.d("Touchbase---@@@@@@@@", "Loading from server");
            if (InternetConnection.checkConnection(this))
                webservices();
            else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        } else {
            adapter = new AlbumPhotoAdapter(this, albumPhotolist, "1");
            gv.setAdapter(adapter);

            if (InternetConnection.checkConnection(this)) {
                checkForUpdate();
                Log.d("---------------", "Check for update gets called------");
            } else {
                Toast.makeText(this, "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
            }
            gv.setOnItemClickListener(this);
        }
    }

    public void checkForUpdate() {
        isinUpdatemode = true;
        Log.e("Touchbase", "------ checkForUpdate() called for update");
        String url = Constant.GetAlbumPhotoList_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("albumId", albumId));
        arrayList.add(new BasicNameValuePair("groupId", data.getGrpId()));

//        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.ALBUM_PHOTO_UPDATED_ON+albumId,"1970/01/01 00:00:00");
//        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
//        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0
//
//        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);;
        Log.e("request", arrayList.toString());

        GalleryPhotosDataAsyncTask task = new GalleryPhotosDataAsyncTask(url, arrayList, this);
        task.execute();
        Log.d("Response", "PARAMETERS " + Constant.GetAlbumPhotoList_New + " :- " + arrayList.toString());
    }

    public void webservices() {

        Log.e("Touchbase", "------ webservices() called for 1st time");
        //String url = Constant.GetAlbumPhotoList;
        String url = Constant.GetAlbumPhotoList_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("albumId", albumId));
        arrayList.add(new BasicNameValuePair("groupId", data.getGrpId()));

//        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.ALBUM_PHOTO_UPDATED_ON+albumId, "1970/01/01 00:00:00");
//        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
//        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0
//        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);


        Log.e("request", arrayList.toString());

        GalleryPhotosDataAsyncTask task = new GalleryPhotosDataAsyncTask(url, arrayList, this);
        task.execute();
        Log.d("Response", "PARAMETERS " + Constant.GetAlbumPhotoList_New + " :- " + arrayList.toString());
    }


    public class GalleryPhotosDataAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(GalleryDescription.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public GalleryPhotosDataAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            if (!isinUpdatemode) {
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData(url, argList);
                val = val.toString();
                Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            isinUpdatemode = false;
            if (result != "" && result != null) {
                Log.d("Response", "calling getAllAlbumList");

                getGalleryPhotosData(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    //===================== Get Data ========================================

    public void getGalleryPhotosData(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONObject jsonTBAlbumPhotoListResult = jsonObj.getJSONObject("TBAlbumPhotoListResult");
            final String status = jsonTBAlbumPhotoListResult.getString("status");

            if (status.equals("0")) {

                // updatedOn = jsonTBAlbumPhotoListResult.getString("updatedOn");
                newAlbums = new ArrayList<AlbumPhotoData>();

                //JSONObject jsonResult = jsonTBAlbumPhotoListResult.getJSONObject("Result");

                JSONArray jsonNewAlbumPhotoList = jsonTBAlbumPhotoListResult.getJSONArray("Result");

                int newAlbumPhotoListCount = jsonNewAlbumPhotoList.length();
                newAlbums.clear();


                for (int i = 0; i < newAlbumPhotoListCount; i++) {

                    AlbumPhotoData data = new AlbumPhotoData();

                    JSONObject result_object = jsonNewAlbumPhotoList.getJSONObject(i);

                    data.setPhotoId(result_object.getString("photoId").toString());

                    data.setDescription(result_object.getString("description").toString());
                    data.setGrpId(String.valueOf(grpId));
                    data.setAlbumId(String.valueOf(albumId));

//                    if (result_object.has("url")) {
//                        data.setUrl(result_object.getString("url").toString());
//                    } else {
//                        data.setUrl("");
//                    }

                    if(!result_object.getString("url").isEmpty()){
                        data.setUrl(result_object.getString("url").toString());
                        newAlbums.add(data);
                    }

                 //   newAlbums.add(data);
                }

                adapter = new AlbumPhotoAdapter(GalleryDescription.this, newAlbums, "1");
                gv.setAdapter(adapter);
                gv.setOnItemClickListener(GalleryDescription.this);


//                final ArrayList<AlbumPhotoData> UpdatedAlbumPhototList = new ArrayList<AlbumPhotoData>();
//                JSONArray jsonUpdatedAlbumPhotoList = jsonResult.getJSONArray("updatedPhotos");
//
//
//                int updateAlbumPhotoListCount = jsonUpdatedAlbumPhotoList.length();
//
//                for (int i = 0; i < updateAlbumPhotoListCount; i++) {
//
//                    AlbumPhotoData data = new AlbumPhotoData();
//
//                    JSONObject result_object = jsonUpdatedAlbumPhotoList.getJSONObject(i);
//
//                    data.setPhotoId(result_object.getString("photoId").toString());
//
//                    data.setDescription(result_object.getString("description").toString());
//                    data.setGrpId(String.valueOf(grpId));
//                    data.setAlbumId(String.valueOf(albumId));
//                    if (result_object.has("url")) {
//                        data.setUrl(result_object.getString("url").toString());
//                    } else {
//                        data.setUrl("");
//                    }
//                    UpdatedAlbumPhototList.add(data);
//
//                }
//
//                final ArrayList<AlbumPhotoData> DeletedAlbumPhotoList = new ArrayList<AlbumPhotoData>();
//                String jsonDeletedAlbumPhotoList = jsonResult.getString("deletedPhotos");
//                int deleteAlbumPhotoListCount = 0;
//                if(!jsonDeletedAlbumPhotoList.equalsIgnoreCase("")){
//
//                    String[]deletedAlbumArray = jsonDeletedAlbumPhotoList.split(",");
//                    deleteAlbumPhotoListCount = deletedAlbumArray.length;
//
//                    for (int i = 0; i < deleteAlbumPhotoListCount; i++) {
//                        AlbumPhotoData data = new AlbumPhotoData();
//                        data.setAlbumId(String.valueOf(albumId));
//                        data.setPhotoId(String.valueOf(deletedAlbumArray[i].toString()));
//                        DeletedAlbumPhotoList.add(data);
//
//                    }
//                }

//                Handler AlbumPhotohandler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//
//                        boolean saved = albumPhotosMasterModel.syncData(grpId,albumId, newAlbums, UpdatedAlbumPhototList, DeletedAlbumPhotoList);
//                        if (!saved) {
//                            Log.e("SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds");
//                            sendEmptyMessageDelayed(0, 2000);
//                        } else {
//                            PreferenceManager.savePreference(GalleryDescription.this, TBPrefixes.ALBUM_PHOTO_UPDATED_ON+albumId, updatedOn);
//
//                           albumPhotolist = new ArrayList<>();
//                            albumPhotolist = albumPhotosMasterModel.getAlbumsPhoto(albumId);
//                            adapter = new AlbumPhotoAdapter(GalleryDescription.this, albumPhotolist, "1");
//                            gv.setAdapter(adapter);
//                            gv.setOnItemClickListener(GalleryDescription.this);
//                        }
//                    }
//                };


//                int overAllCount = newAlbumPhotoListCount + updateAlbumPhotoListCount + deleteAlbumPhotoListCount;
//
//                System.out.println("Number of records received for photos inside albums  : " + overAllCount);
//                if (newAlbumPhotoListCount + updateAlbumPhotoListCount + deleteAlbumPhotoListCount != 0) {
//
//                    AlbumPhotohandler.sendEmptyMessageDelayed(0, 1000);
//                } else {
//                    Log.e("NoUpdate", "No updates found");
//                }
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mode != 0) {
            Intent intent = new Intent(GalleryDescription.this, ImageDetailActivity.class);

            intent.putParcelableArrayListExtra("photos", newAlbums);
            intent.putExtra("photoid", "" + id);
            intent.putExtra("position", position);
            intent.putExtra("albumName", title);
            intent.putExtra("albumId", albumId);
            intent.putExtra("imgUrl", newAlbums.get(position).getUrl());
            intent.putExtra("fromMain", "yes");
            intent.putExtra("fromShowcase", fromShowcase);
            startActivity(intent);
        } else {
        }
    }

    @Override
    public void onBackPressed() {
        adapter = new AlbumPhotoAdapter(GalleryDescription.this, newAlbums, "1");
        if (materialDesignFAM.isOpened()) {
            materialDesignFAM.close(false);
        } else if (mode == 0 && adapter.getIsdelete().equalsIgnoreCase("true")) {
            //checkForUpdate();
            webservices();

        } else if (mode == 0) {
            adapter.notifyDataSetChanged();
            gv.setAdapter(adapter);
            mode = 1;
            materialDesignFAM.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    public void checkForUpdateForAlbums() {
        Log.e("Touchbase", "------ checkForUpdateForAlbums() called");
        String url = Constant.GetAlbumsList_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("profileId", PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleId", PreferenceManager.getPreference(this, PreferenceManager.MODULE_ID)));
        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.GALLERY_PREFIX + moduleId + "_" + grpId);
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        ;
        Log.e("request", arrayList.toString());
        GetUpdatedAlbumRecordAsyncTask task = new GetUpdatedAlbumRecordAsyncTask(url, arrayList, this);
        task.execute();
        Log.d("Response", "PARAMETERS " + Constant.GetAlbumsList_New + " :- " + arrayList.toString());
    }

    public class GetUpdatedAlbumRecordAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(GalleryDescription.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public GetUpdatedAlbumRecordAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            if (!isinUpdatemode) {
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData(url, argList);
                val = val.toString();
                Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (result != "") {
                Log.d("Response", "calling getAllAlbumList");

                getAlbumData(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }


    public void getAlbumData(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONObject jsonTBAlbumsListResult = jsonObj.getJSONObject("TBAlbumsListResult");
            final String status = jsonTBAlbumsListResult.getString("status");

            if (status.equals("0")) {

                updatedOn = jsonTBAlbumsListResult.getString("updatedOn");
                final ArrayList<AlbumData> newAlbums = new ArrayList<AlbumData>();

                JSONObject jsonResult = jsonTBAlbumsListResult.getJSONObject("Result");

                JSONArray jsonNewAlbumList = jsonResult.getJSONArray("newAlbums");

                int newAlbumListCount = jsonNewAlbumList.length();

                for (int i = 0; i < newAlbumListCount; i++) {

                    AlbumData data = new AlbumData();

                    JSONObject result_object = jsonNewAlbumList.getJSONObject(i);

                    data.setAlbumId(result_object.getString("albumId").toString());
                    data.setTitle(result_object.getString("title").toString());
                    data.setDescription(result_object.getString("description").toString());
                    data.setGrpId(result_object.getString("groupId").toString());
                    data.setIsAdmin(result_object.getString("isAdmin").toString());
                    data.setModuleId(result_object.getString("moduleId").toString());

                    if (result_object.has("image")) {
                        data.setImage(result_object.getString("image").toString());
                    } else {
                        data.setImage("");
                    }

                    newAlbums.add(data);

                }

                final ArrayList<AlbumData> UpdatedAlbumList = new ArrayList<AlbumData>();
                JSONArray jsonUpdatedAlbumList = jsonResult.getJSONArray("updatedAlbums");

                int updateAlbumListCount = jsonUpdatedAlbumList.length();

                for (int i = 0; i < updateAlbumListCount; i++) {

                    AlbumData data = new AlbumData();

                    JSONObject result_object = jsonUpdatedAlbumList.getJSONObject(i);

                    data.setAlbumId(result_object.getString("albumId").toString());
                    data.setTitle(result_object.getString("title").toString());
                    data.setDescription(result_object.getString("description").toString());
                    data.setGrpId(result_object.getString("groupId").toString());
                    data.setIsAdmin(result_object.getString("isAdmin").toString());
                    data.setModuleId(result_object.getString("moduleId").toString());

                    if (result_object.has("image")) {
                        data.setImage(result_object.getString("image").toString());
                    } else {
                        data.setImage("");
                    }
                    tv_title.setText(result_object.getString("title").toString());
                    tv_description.setText(result_object.getString("description").toString());
                    UpdatedAlbumList.add(data);

                }

                final ArrayList<AlbumData> DeletedAlbumList = new ArrayList<AlbumData>();
                String jsonDeletedAlbumList = jsonResult.getString("deletedAlbums");
                int deleteAlbumListCount = 0;
                if (!jsonDeletedAlbumList.equalsIgnoreCase("")) {

                    String[] deletedAlbumArray = jsonDeletedAlbumList.split(",");
                    deleteAlbumListCount = deletedAlbumArray.length;

                    for (int i = 0; i < deleteAlbumListCount; i++) {
                        AlbumData data = new AlbumData();
                        data.setAlbumId(String.valueOf(deletedAlbumArray[i].toString()));
                        DeletedAlbumList.add(data);

                    }

                }

                Handler Albumdatahandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        boolean saved = albumModel.syncData(grpId, newAlbums, UpdatedAlbumList, DeletedAlbumList);
                        if (!saved) {
                            Log.e("SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            // PreferenceManager.savePreference(GalleryDescription.this, TBPrefixes.GALLERY_PREFIX+moduleId+"_"+grpId, updatedOn);
                        }
                    }
                };

                int overAllCount = newAlbumListCount + updateAlbumListCount + deleteAlbumListCount;

                System.out.println("Number of records received for albums  : " + overAllCount);
                if (newAlbumListCount + updateAlbumListCount + deleteAlbumListCount != 0) {

                    Albumdatahandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Log.e("NoUpdate", "No updates found");
                }
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }

    }

}
