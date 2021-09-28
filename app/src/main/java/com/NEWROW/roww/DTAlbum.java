package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.AlbumPhotoAdapter;
import com.NEWROW.row.Data.AlbumPhotoData;
import com.NEWROW.row.Data.ClubGalleryData;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DTAlbum extends Activity implements AdapterView.OnItemClickListener {
    private Context context;
    private  final int STATE_UP = 1, STATE_DOWN = 2;
    private int buttonState = STATE_DOWN;

    private View descriptionWrapper;

    //    private AppBarLayout layout;
    private TextView tv_title, tv_minititle, tv_description, tv_dop;

    private static Bitmap bitmap;
    private String updatedOn = "";
    private String albumId = "";
    private ImageView ivUpDown;
    private long grpId;
    private ArrayList<AlbumPhotoData> albumPhotolist = new ArrayList<AlbumPhotoData>();
    ClubGalleryData data = new ClubGalleryData();
   //AlbumData data = new AlbumData();
    private AlbumPhotoAdapter adapter;
    private GridView gv;
    private String title;
    private String description;
    private String albumimage;
    //added By Gaurav
    private String rotractors;
    //closed
    private String groupId = "0";
    String shareType, attendance,meetType;
    LinearLayout ll_dop, ll_cop, ll_bene, ll_timespent, ll_noOfRotarians, ll_details,ll_rotractors;
    LinearLayout ll_agenda, ll_mom;
    String currencyType = "";
    View view;
    TextView tv_cop, tv_beneficiary, tv_manPower, tv_noOfRotarians, txt_galleryTitle;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");

    //Added By Gaurav
    LinearLayout media_photo_layout,photo_title_layout;
    ImageView iv_printmedia;
    private String mediaPhotoPath="";
    private String media_discription="";
    private String Ismedia="0";
    private TextView tv_rotractors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dt_album_new);
        context = this;
        Log.e("Album", "Inside album now");
        //grpId = Long.parseLong(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID));
        if(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID)!=null) {
            grpId = Long.parseLong(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID));
        }
        System.out.println("grpId_value"+grpId);

        descriptionWrapper = findViewById(R.id.descriptionWrapper);
        gv = (GridView) findViewById(R.id.gv);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_minititle = (TextView) findViewById(R.id.tv_minititle);
        ivUpDown = (ImageView) findViewById(R.id.iv_up_down);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_dop = (TextView) findViewById(R.id.tv_dop);
        ll_dop = (LinearLayout) findViewById(R.id.ll_dop);
        ll_cop = (LinearLayout) findViewById(R.id.ll_cop);
        ll_bene = (LinearLayout) findViewById(R.id.ll_bene);
        ll_timespent = (LinearLayout) findViewById(R.id.ll_timespent);
        ll_noOfRotarians = (LinearLayout) findViewById(R.id.ll_noOfRotarians);
        ll_rotractors = (LinearLayout) findViewById(R.id.ll_rotractors);
        ll_details = (LinearLayout) findViewById(R.id.ll_details);
        tv_beneficiary = (TextView) findViewById(R.id.tv_beneficiary);
        tv_cop = (TextView) findViewById(R.id.tv_cop);
        tv_manPower = (TextView) findViewById(R.id.tv_manHrSpent);
        tv_noOfRotarians = (TextView) findViewById(R.id.tv_noOfRotarians);
        txt_galleryTitle = (TextView) findViewById(R.id.txt_galleryTitle);
        tv_rotractors = (TextView) findViewById(R.id.tv_rotractors);
        ll_agenda = (LinearLayout) findViewById(R.id.ll_agenda);
        ll_mom = (LinearLayout) findViewById(R.id.ll_mom);

        /* Added By Gaurav*/
        media_photo_layout = (LinearLayout) findViewById(R.id.media_photo_layout);
        photo_title_layout = (LinearLayout) findViewById(R.id.photo_title_layout);
        iv_printmedia=findViewById(R.id.iv_printmedia);

        Intent i = getIntent();
        title = i.getStringExtra("albumname");
        description = i.getStringExtra("albumDescription");
        albumId = i.getStringExtra("albumId");
        albumimage = i.getStringExtra("albumImage");
        groupId = i.getStringExtra("groupId");
        rotractors = i.getStringExtra("rotractors");
        mediaPhotoPath = i.getStringExtra("mediaPhotoPath");
        media_discription = i.getStringExtra("media_discription");

        //mediaPhoto is display

        if (mediaPhotoPath.equals("")) {
            //Image is deleted
        } else {
            media_photo_layout.setVisibility(View.VISIBLE);

            Picasso.with(DTAlbum.this).load(mediaPhotoPath)
                    //.fit()
                    //.resize(200, 200)
                    .placeholder(R.drawable.placeholder_new)
                    .into(iv_printmedia);
            iv_printmedia.setBackground(null);



        }

        gv.setOnItemClickListener(this);






        iv_printmedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DTAlbum.this,ViewMediaPhoto.class);
                intent.putExtra("imgURL",mediaPhotoPath);
                intent.putExtra("description",media_discription);
                startActivity(intent);
            }
        });


        //tv_title.setText(Html.fromHtml("<Html><body><center><font size=\"2\">Gallery</font> <br><font size=\"1\">" + title + "</font></center></body></Html>"));
        tv_title.setText(title);
       // tv_title.setText("Service Project");

        //tv_minititle.setVisibility(View.VISIBLE);
        tv_minititle.setText(title);
        Log.e("Description", description);
        tv_description.setText(description);
//        layout = (AppBarLayout) findViewById(R.id.app_bar);

       /* data = (ClubGalleryData) i.getSerializableExtra("albumData");

        setAlbumData(data);*/

      //  init();


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (InternetConnection.checkConnection(this)) {
            loadData();
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

        Log.d("Response", "PARAMETERS " + Constant.GetAlbumPhotoList_New + " :- " + arrayList.toString());
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

                if (albumPhotolist.size()==0){
                    photo_title_layout.setVisibility(View.GONE);
                }else{
                    photo_title_layout.setVisibility(View.VISIBLE);

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

    public void loadData() {

        Log.e("Touchbase", "------ loadData() called");
        String url = Constant.GetAlbumDetails_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("albumId", albumId));

        Log.d("Album Data", "PARAMETERS " + Constant.GetAlbumDetails_New + " :- " + arrayList.toString());
        DTAlbum.GetAlbumDetailsAsynctask1 task = new DTAlbum.GetAlbumDetailsAsynctask1(url, arrayList, this);
        task.execute();

    }
    public class GetAlbumDetailsAsynctask1 extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);

        String url = null;
        List<NameValuePair> argList = null;


        public GetAlbumDetailsAsynctask1(String url, List<NameValuePair> argList, Context ctx) {
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
            if (result != "") {
                Log.d("Response", "calling GetAlbumDetails");

                getAlbumDetails(result.toString());

                Log.d("GetAlbumDetails", result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void getAlbumDetails(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONObject jsonTBAlbumsListResult = jsonObj.getJSONObject("TBAlbumDetailResult");
            final String status = jsonTBAlbumsListResult.getString("status");

            if (status.equals("0")) {

                JSONArray jsonNewAlbumList = jsonTBAlbumsListResult.getJSONArray("AlbumDetailResult");

                if (jsonNewAlbumList.length() < 1) {
                    return;
                }

                JSONObject object = jsonNewAlbumList.getJSONObject(0);
                JSONObject albumObj = object.getJSONObject("AlbumDetail");

                data.setAlbumId(albumObj.getString("albumId"));
                data.setTitle(albumObj.getString("albumTitle"));
                data.setDescription(albumObj.getString("albumDescription"));
                data.setImage(albumObj.getString("albumImage"));
                data.setGrpId(albumObj.getString("groupId"));
//                data.setModuleId(albumObj.getString("moduleId"));
                //  data.setIsAdmin(albumObj.getString("isAdmin"));
                data.setClub_Name(albumObj.getString("clubname"));
                data.setProject_date(albumObj.getString("project_date"));
                data.setProject_cost(albumObj.getString("project_cost"));
                data.setBeneficiary(albumObj.getString("beneficiary"));
                data.setWorking_hour(albumObj.getString("working_hour"));
                data.setWorking_hour_type(albumObj.getString("working_hour_type"));
                data.setCost_of_project_type(albumObj.getString("cost_of_project_type"));
                data.setNoOfRotarians(albumObj.getString("NumberOfRotarian"));
                data.setShareType(albumObj.getString("shareType"));
                data.setAttendance(albumObj.getString("Attendance"));
                data.setAttendancePer(albumObj.getString("AttendancePer"));
                data.setMeetType(albumObj.getString("MeetingType"));
                data.setAgendaDocID(albumObj.getString("AgendaDoc"));
                data.setMomDocID(albumObj.getString("MOMDoc"));

                //Add New Field
                data.setRotractors(albumObj.getString("Rotaractors"));

                Ismedia = albumObj.getString("Ismedia");
                mediaPhotoPath = albumObj.getString("Mediaphoto");

                media_discription=albumObj.getString("MediaDesc").toString();

                if (mediaPhotoPath.equals("")) {
                    //Image is deleted
                } else {
                    media_photo_layout.setVisibility(View.VISIBLE);

                    Picasso.with(DTAlbum.this).load(mediaPhotoPath)
                            //.fit()
                            //.resize(200, 200)
                            .placeholder(R.drawable.placeholder_new)
                            .into(iv_printmedia);
                    iv_printmedia.setBackground(null);



                }


                setAlbumData(data);

/*
                if(fromNoti.equalsIgnoreCase("1")){
                    webservices();
                    iv_actionbtn.setVisibility(View.GONE);
                }
*/

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            //Toast.makeText(context, "Something went wrong1", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void setAlbumData(ClubGalleryData data ) {
        title = data.getTitle().toString();
        description = data.getDescription().toString();
        albumId = data.getAlbumId();
        albumimage = data.getImage();
        shareType = data.getShareType();
        rotractors = data.getRotractors();

        if (data.getProject_date().toString().isEmpty()) {
            ll_dop.setVisibility(View.GONE);
        }


        if (data.getRotractors().toString().isEmpty()) {
            ll_rotractors.setVisibility(View.GONE);
        }


//        if (data.getShareType().equalsIgnoreCase("0")) {
//            ll_details.setVisibility(View.GONE);
//        } else {
//            ll_details.setVisibility(View.VISIBLE);
//        }

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


        if (data.getCost_of_project_type().equalsIgnoreCase("1")) {
            currencyType = "\u20B9";
        } else if (data.getCost_of_project_type().equalsIgnoreCase("2")) {
            currencyType = "$";
        } else {
            currencyType = "";
        }
        if (data.getProject_cost().toString().isEmpty()) {
            ll_cop.setVisibility(View.GONE);
        }else {
            ll_cop.setVisibility(View.VISIBLE);
        }

        if (data.getBeneficiary().toString().isEmpty()) {
            ll_bene.setVisibility(View.GONE);
        }else {
            ll_bene.setVisibility(View.VISIBLE);
        }
        if (data.getWorking_hour().toString().isEmpty()) {
            ll_timespent.setVisibility(View.GONE);
        }else {
            ll_timespent.setVisibility(View.VISIBLE);
        }

        if (data.getNoOfRotarians().toString().isEmpty()) {
            ll_noOfRotarians.setVisibility(View.GONE);
        }else {
            ll_noOfRotarians.setVisibility(View.VISIBLE);
        }

        if (data.getRotractors().toString().isEmpty()) {
            ll_rotractors.setVisibility(View.GONE);
        }else {
            ll_rotractors.setVisibility(View.VISIBLE);
            tv_rotractors.setText(rotractors);
        }

//        tv_cop.setText(data.getProject_cost().toString());
        tv_cop.setText(data.getProject_cost().toString() + " " + currencyType);

        tv_beneficiary.setText(data.getBeneficiary().toString());


        tv_manPower.setText(data.getWorking_hour().toString() + " " + data.getWorking_hour_type());

        tv_noOfRotarians.setText(data.getNoOfRotarians().toString());
        //tv_title.setText(Html.fromHtml("<Html><body><center><font size=\"2\">Gallery</font> <br><font size=\"1\">" + title + "</font></center></body></Html>"));


        Log.e("Description", description);


        tv_description.setText(description);

        /*if (fromShowcase.equalsIgnoreCase("0")) {
            iv_actionbtn.setVisibility(View.GONE);
            tv_title.setText(title);
            txt_galleryTitle.setVisibility(View.GONE);
            //materialDesignFAM.setVisibility(View.GONE);
        } else {*/

        // iv_actionbtn.setVisibility(View.VISIBLE);
        String catid = PreferenceManager.getPreference(DTAlbum.this, PreferenceManager.MY_CATEGORY);

        TextView lbl_cost = (TextView) findViewById(R.id.lbl_cost);
        TextView lbl_ben = (TextView) findViewById(R.id.lbl_ben);
        TextView lbl_man = (TextView) findViewById(R.id.lbl_man);
        if (catid.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_CLUB))) {
            view.setVisibility(View.VISIBLE);
            txt_galleryTitle.setText(title);

            if (shareType.equalsIgnoreCase("0")) {
                tv_title.setText("Club Service");
                lbl_cost.setText("Attendance");
                lbl_ben.setText("Attendance(%)");
                lbl_man.setText("Meeting Type");

                if (data.getAttendance().toString().isEmpty()) {
                    ll_cop.setVisibility(View.GONE);
                } else {
                    ll_cop.setVisibility(View.VISIBLE);
                }

                if (data.getAttendancePer().toString().isEmpty()) {
                    ll_bene.setVisibility(View.GONE);
                } else {
                    ll_bene.setVisibility(View.VISIBLE);
                }

                if (data.getMeetType().toString().isEmpty()) {
                    ll_timespent.setVisibility(View.GONE);
                } else {
                    ll_timespent.setVisibility(View.VISIBLE);
                }

                if (data.getAgendaDocID().toString().isEmpty() || data.getAgendaDocID().equalsIgnoreCase("0")) {
                    ll_agenda.setVisibility(View.GONE);
                } else {
                    ll_agenda.setVisibility(View.VISIBLE);
                }

                if (data.getMomDocID().toString().isEmpty() || data.getMomDocID().equalsIgnoreCase("0")) {
                    ll_mom.setVisibility(View.GONE);
                } else {
                    ll_mom.setVisibility(View.VISIBLE);
                }

                attendance = data.getAttendance();

                tv_cop.setText(data.getAttendance());
                tv_beneficiary.setText(data.getAttendancePer());
                meetType = data.getMeetType();

                if (meetType != null && !meetType.isEmpty()) {
                    if (meetType.equalsIgnoreCase("0")) {
                        tv_manPower.setText("Regular");

                    } else if (meetType.equalsIgnoreCase("1")) {
                        tv_manPower.setText("BOD");
                    } else {
                        tv_manPower.setText("Assembly");
                    }
                }

            } else {
                tv_title.setText("Service Project");
                lbl_cost.setText("Cost");
                lbl_ben.setText("Beneficiaries");
                lbl_man.setText("Man hours");

                if (data.getRotractors().toString().isEmpty()) {
                    ll_rotractors.setVisibility(View.GONE);
                }else {
                    ll_rotractors.setVisibility(View.VISIBLE);
                    tv_rotractors.setText(data.getRotractors().toString());


                }
                ll_agenda.setVisibility(View.GONE);
                ll_mom.setVisibility(View.GONE);
            }
        } else {
            txt_galleryTitle.setVisibility(View.GONE);

            if(shareType.equalsIgnoreCase("0")){
                tv_title.setText("District Event");

                ll_cop.setVisibility(View.GONE);
                ll_timespent.setVisibility(View.GONE);
            }else {
                tv_title.setText("District Project");

                ll_cop.setVisibility(View.VISIBLE);
                ll_timespent.setVisibility(View.VISIBLE);
            }
        }
        //}
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
