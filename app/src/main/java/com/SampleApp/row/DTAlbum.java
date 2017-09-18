package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DTAlbum extends Activity implements AdapterView.OnItemClickListener {
    private Context context;
    private  final int STATE_UP = 1, STATE_DOWN = 2;
    private int buttonState = STATE_DOWN;

    private View descriptionWrapper;

    private AppBarLayout layout;
    private TextView tv_title, tv_minititle, tv_description;

    private static Bitmap bitmap;
    private String updatedOn = "";
    private String albumId = "";
    private ImageView ivUpDown;
    private long grpId;
    private ArrayList<AlbumPhotoData> albumPhotolist = new ArrayList<AlbumPhotoData>();
    private AlbumPhotoAdapter adapter;
    private GridView gv;
    private String title;
    private String description;
    private String albumimage;
    private String groupId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dt_album);
        context = this;
        Log.e("Album", "Inside album now");
        grpId = Long.parseLong(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID));
        descriptionWrapper = findViewById(R.id.descriptionWrapper);
        gv = (GridView) findViewById(R.id.gv);

        Intent i = getIntent();
        title = i.getStringExtra("albumname");
        description = i.getStringExtra("albumDescription");
        albumId = i.getStringExtra("albumId");
        albumimage = i.getStringExtra("albumImage");
        groupId = i.getStringExtra("groupId");
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_minititle = (TextView) findViewById(R.id.tv_minititle);

        tv_description = (TextView) findViewById(R.id.tv_description);
        //tv_title.setText(Html.fromHtml("<Html><body><center><font size=\"2\">Gallery</font> <br><font size=\"1\">" + title + "</font></center></body></Html>"));
        tv_title.setText(title);

        //tv_minititle.setVisibility(View.VISIBLE);
        tv_minititle.setText(title);
        Log.e("Description", description);
        tv_description.setText(description);
        layout = (AppBarLayout) findViewById(R.id.app_bar);
        ivUpDown = (ImageView) findViewById(R.id.iv_up_down);
        init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (InternetConnection.checkConnection(this)) {
            checkForUpdate();
            Log.d("---------------", "Check for update gets called------");
        } else {
            Toast.makeText(this, "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
        }
    }

    public void init() {
        ivUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonState == STATE_UP) {
                    ivUpDown.setImageDrawable(getResources().getDrawable(R.drawable.g_down));
                    buttonState = STATE_DOWN;
                    LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.75f);
                    LinearLayout.LayoutParams gvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f);
                    descriptionWrapper.setLayoutParams(descParams);
                    gv.setLayoutParams(gvParams);
                    //imageWrapperLayout.setLayoutParams(imageParams);
                    //svDescription.setLayoutParams(textParams);
                } else {
                    ivUpDown.setImageDrawable(getResources().getDrawable(R.drawable.g_up));
                    buttonState = STATE_UP;
                    LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.6f);
                    LinearLayout.LayoutParams gvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.4f);
                    descriptionWrapper.setLayoutParams(descParams);
                    gv.setLayoutParams(gvParams);

                    //imageWrapperLayout.setLayoutParams(imageParams);
                    //svDescription.setLayoutParams(textParams);
                }
            }
        });

        gv.setOnItemClickListener(this);
    }

    public void checkForUpdate() {

        Log.e("Touchbase", "------ checkForUpdate() called for update");
        String url = Constant.GetAlbumPhotoList;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("albumId", albumId));

        updatedOn = "1970/01/01 00:00:00";

        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0
        arrayList.add(new BasicNameValuePair("groupId", groupId));

        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        ;
        Log.e("request", arrayList.toString());

        Log.d("Response", "PARAMETERS " + Constant.GetAlbumPhotoList + " :- " + arrayList.toString());
        GalleryPhotosDataAsyncTask task = new GalleryPhotosDataAsyncTask(url, arrayList, this);
        task.execute();
    }

    public class GalleryPhotosDataAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(DTAlbum.this, R.style.TBProgressBar);
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

    //===================== Get Data ========================================

    public void getGalleryPhotosData(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONObject jsonTBAlbumPhotoListResult = jsonObj.getJSONObject("TBAlbumPhotoListResult");
            final String status = jsonTBAlbumPhotoListResult.getString("status");

            if (status.equals("0")) {

                updatedOn = jsonTBAlbumPhotoListResult.getString("updatedOn");

                albumPhotolist.clear();
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
                    albumPhotolist.add(data);
                }

                adapter = new AlbumPhotoAdapter(context, albumPhotolist, "1");
                gv.setAdapter(adapter);

            } else {
                Toast.makeText(DTAlbum.this, R.string.messageSorry, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(DTAlbum.this, ImageDetailActivity.class);

        intent.putParcelableArrayListExtra("photos", albumPhotolist);
        intent.putExtra("photoid", "" + id);
        intent.putExtra("position", position);
        intent.putExtra("albumName", title);
        intent.putExtra("albumId", albumId);
        intent.putExtra("fromMain", "no");
        startActivity(intent);

    }


}
