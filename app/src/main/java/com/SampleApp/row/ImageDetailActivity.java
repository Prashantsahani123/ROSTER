package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.SlidingImageGalleryAdapter;
import com.SampleApp.row.Data.AlbumPhotoData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.TBPrefixes;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.AlbumPhotosMasterModel;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by USER1 on 22-09-2016.
 */
public class ImageDetailActivity extends Activity {
    RelativeLayout imageWrapperLayout;
    ScrollView svDescription;
    //ImageView ivUpDown;
    ImageView iv_actionbtn;
    ViewPager photoPager;
    TextView tvDescription;
    FrameLayout divider;
    final int STATE_UP = 1, STATE_DOWN = 2;
    int buttonState = STATE_UP;
    String photoid = "";
    TextView tv_title;
    Context context;
    String fromMain = "no", isAdmin = "no";
    ArrayList<AlbumPhotoData> photoList;

    SlidingImageGalleryAdapter adapter;
    boolean isFirst = true;
    View overlay;
    ImageView btnPrevious, btnNext;

    AlbumPhotosMasterModel model;
    int position = 0;
    //Handler overlayHandler;
    int size;
    private ImageView ivTemp;
    static final int EDITPHOTO = 1;
    public String albumId = "";
    private long grpId;
    String updatedOn = "";
    AlbumPhotosMasterModel albumPhotosMasterModel;
    String url,fromShowcase;

    //private SlidingUpPanelLayout slidingLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        if(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID)!=null) {
            grpId = Long.parseLong(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID));
        }
        albumPhotosMasterModel = new AlbumPhotosMasterModel(this);
        ivTemp = (ImageView) findViewById(R.id.ivTemp);
        ivTemp.setDrawingCacheEnabled(true);
        //slidingLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        //some "demo" event
        //slidingLayout.setPanelSlideListener(onSlideListener());

        context = this;

        overlay = findViewById(R.id.overlay);
        btnPrevious = (ImageView) findViewById(R.id.btnPrevious);
        btnNext = (ImageView) findViewById(R.id.btnNext);

        model = new AlbumPhotosMasterModel(this);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);

        if(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN)!=null) {
            if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("Yes")) {
                iv_actionbtn.setVisibility(View.VISIBLE);
            }
        }else {
            iv_actionbtn.setVisibility(View.GONE);
        }
        iv_actionbtn.setImageResource(R.drawable.overflow_btn_blue);
        isAdmin = PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN, "No");
        if (getIntent().hasExtra("fromMain")) {
            fromMain = getIntent().getStringExtra("fromMain");
        }

        fromShowcase = getIntent().getExtras().getString("fromShowcase");
       /*overlayHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                overlay.setVisibility(View.GONE);
            }
        };*/

        //imageWrapperLayout = (RelativeLayout) findViewById(R.id.imageWrapper);
        svDescription = (ScrollView) findViewById(R.id.svDescription);

        tvDescription = (TextView) findViewById(R.id.tv_description);
        //ivUpDown = (ImageView) findViewById(R.id.iv_up_down);
        photoPager = (ViewPager) findViewById(R.id.photoPager);
        divider = (FrameLayout) findViewById(R.id.divider);
        init();
        photoid = getIntent().getExtras().getString("photoid", "-1");
        Log.e("PhotoId", "Photo id is : " + photoid);

        photoList = getIntent().getExtras().getParcelableArrayList("photos");
        albumId = getIntent().getStringExtra("albumId");
        url = getIntent().getStringExtra("imgUrl");
        size = photoList.size();
        adapter = new SlidingImageGalleryAdapter(context, photoList);
        photoPager.setAdapter(adapter);
        position = getIntent().getExtras().getInt("position", 0);
        if (position == 0) {
            btnPrevious.setVisibility(View.GONE);
        }

        ViewPager.OnPageChangeListener listner = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                position = pos;
                //Toast.makeText(ImageDetailActivity.this, "Page is : "+position, Toast.LENGTH_SHORT).show();
                String description = photoList.get(position).getDescription();
//                if ( description.equals("")) {
//                    description = "";
//                }
                tvDescription.setText(description);
                if (position == 0) {
                    btnPrevious.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                    overlay.setVisibility(View.VISIBLE);
                } else if (position == size - 1) {
                    btnPrevious.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                    overlay.setVisibility(View.VISIBLE);
                } else {
                    if (!isFirst) {
                        btnPrevious.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                        //overlay.setVisibility(View.GONE);
                    }
                }
                isFirst = false;

                try {

                    Picasso.with(context)
                            .load(photoList.get(position).getUrl())
                            .placeholder(R.drawable.imageplaceholder)
                            .into(ivTemp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };


        photoPager.addOnPageChangeListener(listner);
        //listner.onPageSelected(position);
        photoPager.setCurrentItem(position);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = photoPager.getCurrentItem();
                photoPager.setCurrentItem(position - 1);

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = photoPager.getCurrentItem();
                photoPager.setCurrentItem(position + 1);
            }
        });


        loadImage();
    }

    public void init() {

        /*ivUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonState == STATE_UP) {

                    //slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    ivUpDown.setImageDrawable(getResources().getDrawable(R.drawable.g_down));
                    buttonState = STATE_DOWN;
//                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.6f);
                    //LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.4f);
//                    imageWrapperLayout.setLayoutParams(imageParams);
                    //                 svDescription.setLayoutParams(textParams);
                } else {

                    //slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    ivUpDown.setImageDrawable(getResources().getDrawable(R.drawable.g_up));
                    buttonState = STATE_UP;
//                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.1f);
                    //   LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.9f);
//                    imageWrapperLayout.setLayoutParams(imageParams);
                    //                  svDescription.setLayoutParams(textParams);
                }
            }
        });*/
        tv_title = (TextView) findViewById(R.id.tv_title);
        String title = getIntent().getExtras().getString("albumName", "");
        Log.e("title", title);
        tv_title.setText(title);
        //overlayHandler.sendEmptyMessageDelayed(0, 2000);


        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(ImageDetailActivity.this, iv_actionbtn);
                popup.getMenu().add(1, R.id.tv_share, 3, "Share");

//                if(fromShowcase.equalsIgnoreCase("1")) {
//                    if (fromMain.equalsIgnoreCase("Yes") && !isAdmin.equalsIgnoreCase("no")) {
//                        popup.getMenu().add(1, R.id.edit, 1, "Edit");
//                        popup.getMenu().add(1, R.id.iv_delete, 2, "Delete");
//                    }
//                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent i;
                        switch (item.getItemId()) {
                            case R.id.edit:
                                i = new Intent(ImageDetailActivity.this, EditPhotoActivity.class);
                                i.putExtra("photoId", photoList.get(position).getPhotoId());
                                i.putExtra("description", photoList.get(position).getDescription());
                                i.putExtra("albumId", photoList.get(position).getAlbumId());
                                i.putExtra("groupId", photoList.get(position).getGrpId());
                                i.putExtra("image", photoList.get(position).getUrl());
                                //startActivity(i);
                                startActivityForResult(i, EDITPHOTO);
                                return true;

                            case R.id.iv_delete:
                                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.popup_confrm_delete);
                                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                                tv_line1.setText("Are you sure you want to delete this photo");
                                tv_no.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                tv_yes.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        if (InternetConnection.checkConnection(getBaseContext())) {
                                            deletePhoto(photoList.get(position).getPhotoId(), photoList.get(position).getAlbumId());
                                        } else {
                                            Utils.showToastWithTitleAndContext(getBaseContext(), "No Internet Connection!");
                                        }
                                    }
                                });

                                dialog.show();

                                return true;

                            case R.id.tv_share:

//                                i = new Intent(ImageDetailActivity.this, EditPhotoActivity.class);
//                                startActivity(i);
                                //Bitmap bmp =
                                shareImage();
                                return true;

                            default:
                                return false;

                        }
                    }
                });
                popup.show();

            }
        });
    }

    public void shareImage() {
        try {

            Picasso.with(context)
                    .load(photoList.get(position).getUrl())
                    .placeholder(R.drawable.imageplaceholder)
                    .into(ivTemp);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Bitmap bitmap = ivTemp.getDrawingCache();
        Bitmap bitmap = ((BitmapDrawable)ivTemp.getDrawable()).getBitmap();
        File filesDir = getFilesDir();
        File imageDirectory = new File(filesDir, "ROW");
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs();
        }
        Date d = new Date();
        String image = "image_" + d.getTime() + ".jpg";
        File imageFile = new File(imageDirectory, image);
        //imageFile
        try {
            FileOutputStream fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
            fout.close();

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uri = FileProvider.getUriForFile(ImageDetailActivity.this, "com.SampleApp.row.fileprovider", imageFile);
            //shareIntent.putExtra(Intent.EXTRA_STREAM, );
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpg");
            startActivity(shareIntent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException anfe) {
            anfe.printStackTrace();
        }
    }

    public void loadImage() {
        try {
//            GalleryMasterModel model = new GalleryMasterModel(context);
//            AlbumPhotoData data = new AlbumPhotoData();
//            data = model.getPhotoData(photoid);
            Picasso.with(context).load(photoList.get(position).getUrl())
                    .placeholder(R.drawable.dashboardplaceholder)
                    .into(ivTemp);
            tvDescription.setText(photoList.get(position).getDescription());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            //Toast.makeText(ImageDetailActivity.this, "Failed to load the photo", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(ImageDetailActivity.this, "Sorry. Something is wrong.", Toast.LENGTH_LONG).show();
        }
    }

    public void deletePhoto(String photoId, String albumId) {
        List<NameValuePair> arrayList = new ArrayList<>();
        arrayList.add(new BasicNameValuePair("photoId", photoId));
        arrayList.add(new BasicNameValuePair("albumId", albumId));
        arrayList.add(new BasicNameValuePair("deletedBy", PreferenceManager.getPreference(ImageDetailActivity.this, PreferenceManager.GRP_PROFILE_ID)));
        Log.d("TouchBase", "♦♦♦♦" + arrayList.toString());
        new DeletePhotoAsyncTask(Constant.DeletePhoto, arrayList, ImageDetailActivity.this).execute();
    }

    public class DeletePhotoAsyncTask extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(ImageDetailActivity.this, R.style.TBProgressBar);

        public DeletePhotoAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
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
            progressDialog.dismiss();
            if (result != "" || result != null) {
                getresult(result.toString());
                Log.d("Touchbase", "♦♦♦♦" + result);

            } else {
                Log.d("Touchbase", "♦♦♦♦" + result.toString());
            }

        }

        private void getresult(String val) {
            try {
                JSONObject jsonObj = new JSONObject(val);
                JSONObject ActivityResult = jsonObj.getJSONObject("TBDelteAlbumPhoto");
                final String status = ActivityResult.getString("status");
                if (status.equals("0")) {
                    String msg = ActivityResult.getString("message");
                    Toast.makeText(context, "Photo deleted successfully.", Toast.LENGTH_SHORT).show();
                    try {
                        Boolean isDeleted = model.deletePhoto(photoList.get(position).getAlbumId(), photoList.get(position).getPhotoId());
                        if (isDeleted) {
                            finish();
                        }
                    } catch (Exception e) {
                        Log.d("TouchBase", "♦♦♦♦" + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.d("TouchBase", "♦♦♦♦Exception :- " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /*private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                ivUpDown.setImageDrawable(getResources().getDrawable(R.drawable.g_down));
            }

            @Override
            public void onPanelCollapsed(View view) {
                ivUpDown.setImageDrawable(getResources().getDrawable(R.drawable.g_up));
            }

            @Override
            public void onPanelExpanded(View view) {

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        };
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDITPHOTO) {
            if (resultCode == 1) {

                if (InternetConnection.checkConnection(this)) {
                    checkForUpdate();
                } else {
                    Toast.makeText(this, "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public void checkForUpdate() {
        Log.e("Touchbase", "------ checkForUpdate() called for update");
        String url = Constant.GetAlbumPhotoList;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("albumId", albumId));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID)));

        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.ALBUM_PHOTO_UPDATED_ON + albumId, "1970/01/01 00:00:00");
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0

        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        ;
        Log.e("request", arrayList.toString());

        GalleryPhotosDataAsyncTask task = new GalleryPhotosDataAsyncTask(url, arrayList, this);
        task.execute();
        Log.d("Response", "PARAMETERS " + Constant.GetAlbumPhotoList + " :- " + arrayList.toString());
    }


    public class GalleryPhotosDataAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(ImageDetailActivity.this, R.style.TBProgressBar);
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
            progressDialog.show();
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
            if (result != "" && result != null) {
                Log.d("Response", "calling getAllAlbumList");

                getGalleryPhotosData(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }


    public void getGalleryPhotosData(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONObject jsonTBAlbumPhotoListResult = jsonObj.getJSONObject("TBAlbumPhotoListResult");
            final String status = jsonTBAlbumPhotoListResult.getString("status");

            if (status.equals("0")) {

                updatedOn = jsonTBAlbumPhotoListResult.getString("updatedOn");
                final ArrayList<AlbumPhotoData> newAlbums = new ArrayList<AlbumPhotoData>();

                JSONObject jsonResult = jsonTBAlbumPhotoListResult.getJSONObject("Result");

                JSONArray jsonNewAlbumPhotoList = jsonResult.getJSONArray("newPhotos");

                int newAlbumPhotoListCount = jsonNewAlbumPhotoList.length();

                for (int i = 0; i < newAlbumPhotoListCount; i++) {

                    AlbumPhotoData data = new AlbumPhotoData();

                    JSONObject result_object = jsonNewAlbumPhotoList.getJSONObject(i);

                    data.setPhotoId(result_object.getString("photoId").toString());

                    data.setDescription(result_object.getString("description").toString());
                    data.setGrpId(String.valueOf(grpId));
                    data.setAlbumId(String.valueOf(albumId));

                    if (result_object.has("url")) {
                        data.setUrl(result_object.getString("url").toString());
                    } else {
                        data.setUrl("");
                    }
                    newAlbums.add(data);

                }

                final ArrayList<AlbumPhotoData> UpdatedAlbumPhototList = new ArrayList<AlbumPhotoData>();
                JSONArray jsonUpdatedAlbumPhotoList = jsonResult.getJSONArray("updatedPhotos");


                int updateAlbumPhotoListCount = jsonUpdatedAlbumPhotoList.length();

                for (int i = 0; i < updateAlbumPhotoListCount; i++) {

                    AlbumPhotoData data = new AlbumPhotoData();

                    JSONObject result_object = jsonUpdatedAlbumPhotoList.getJSONObject(i);

                    data.setPhotoId(result_object.getString("photoId").toString());

                    data.setDescription(result_object.getString("description").toString());
                    data.setGrpId(String.valueOf(grpId));
                    data.setAlbumId(String.valueOf(albumId));
                    if (result_object.has("url")) {
                        data.setUrl(result_object.getString("url").toString());
                    } else {
                        data.setUrl("");
                    }
                    UpdatedAlbumPhototList.add(data);

                }

                final ArrayList<AlbumPhotoData> DeletedAlbumPhotoList = new ArrayList<AlbumPhotoData>();
                String jsonDeletedAlbumPhotoList = jsonResult.getString("deletedPhotos");
                int deleteAlbumPhotoListCount = 0;
                if (!jsonDeletedAlbumPhotoList.equalsIgnoreCase("")) {

                    String[] deletedAlbumArray = jsonDeletedAlbumPhotoList.split(",");
                    deleteAlbumPhotoListCount = deletedAlbumArray.length;

                    for (int i = 0; i < deleteAlbumPhotoListCount; i++) {
                        AlbumPhotoData data = new AlbumPhotoData();
                        data.setAlbumId(String.valueOf(albumId));
                        data.setPhotoId(String.valueOf(deletedAlbumArray[i].toString()));
                        DeletedAlbumPhotoList.add(data);

                    }
                }

                Handler AlbumPhotohandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        boolean saved = albumPhotosMasterModel.syncData(grpId, albumId, newAlbums, UpdatedAlbumPhototList, DeletedAlbumPhotoList);
                        if (!saved) {
                            Log.e("SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {

                            photoList = new ArrayList<>();
                            photoList = albumPhotosMasterModel.getAlbumsPhoto(albumId);
                            adapter = new SlidingImageGalleryAdapter(context, photoList);
                            photoPager.setAdapter(adapter);
                            photoPager.setCurrentItem(position);
                            String description = photoList.get(position).getDescription();
                            tvDescription.setText(description);
                        }
                    }
                };


                int overAllCount = newAlbumPhotoListCount + updateAlbumPhotoListCount + deleteAlbumPhotoListCount;

                System.out.println("Number of records received for photos inside albums  : " + overAllCount);
                if (newAlbumPhotoListCount + updateAlbumPhotoListCount + deleteAlbumPhotoListCount != 0) {

                    AlbumPhotohandler.sendEmptyMessageDelayed(0, 1000);
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
