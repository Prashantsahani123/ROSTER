package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.ActivitiesNotificationAdapter;
import com.NEWROW.row.Data.ActivityNotiData;
import com.NEWROW.row.Data.LabelData;
import com.NEWROW.row.Data.profiledata.Separator;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivitiesNotificationDetails extends AppCompatActivity {

    private RecyclerView rv_activities;
    Context context;
    String grpID,type;
    ProgressDialog progressDialog;
    ArrayList<Object> list = new ArrayList<>();
    ArrayList<ActivityNotiData> rotaryList = new ArrayList<>();
    ArrayList<ActivityNotiData> clubList = new ArrayList<>();
    TextView tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_activities_notification_details);

        context = this;

        tv_title = (TextView)findViewById(R.id.tv_title);

        rv_activities = (RecyclerView)findViewById(R.id.rv_activities);

        rv_activities.setLayoutManager(new LinearLayoutManager(context));

        Bundle bundle= getIntent().getExtras();

        if(bundle!=null){
            grpID=bundle.getString("grpID");
            type=bundle.getString("type");

            if(type.equalsIgnoreCase("ClubActivity")){
                tv_title.setText("Activities");
            }else {
                tv_title.setText("Gallery");
            }

        }

        if (InternetConnection.checkConnection(getApplicationContext())) {
            getList();
        }else {
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
        }


    }

    private void getList(){
        try {
            JSONObject requestData = new JSONObject();
            requestData.put("GroupID", grpID);
            requestData.put("Type", type);
            Log.d("Response", "PARAMETERS " + Constant.GetNotificationlist + " :- " + requestData.toString());
            progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetNotificationlist, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Utils.log(response.toString());

                    setList(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Utils.log("VollyError:- " + error.toString());
                    Toast.makeText(context,getString(R.string.msgRetry), Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(
                    new DefaultRetryPolicy(120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(context, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setList(JSONObject response){

        progressDialog.dismiss();
        try {
            JSONObject TBNotificationDetailResult=response.getJSONObject("TBNotificationDetailResult");

            String status=TBNotificationDetailResult.getString("status");

            if(status.equals("0")){
                JSONArray NotificationDetailResult=TBNotificationDetailResult.getJSONArray("NotificationDetailResult");

                if(NotificationDetailResult.length()>0){

                    for(int i=0;i<NotificationDetailResult.length();i++){
                        JSONObject object=NotificationDetailResult.getJSONObject(i);
                        JSONObject AlbumDetail=object.getJSONObject("AlbumDetail");
                        ActivityNotiData data=new ActivityNotiData();
                        data.setAlbumId(AlbumDetail.getString("GalleryID"));
                        data.setTitle(AlbumDetail.getString("Title"));
                        data.setDesc(AlbumDetail.getString("Description"));
                        data.setDatetime(AlbumDetail.getString("GalleryDate"));
                        data.setName(AlbumDetail.getString("member_name"));
                        data.setGrpID(AlbumDetail.getString("groupName"));
                        String galleryType=AlbumDetail.getString("GalleryType");

                        if(galleryType.equals("0")){// Rotary Service

                            rotaryList.add(data);
                        }else {
                            clubList.add(data);
                        }

                    }

                    if (rotaryList != null && rotaryList.size() > 0) {
                        if(type.equalsIgnoreCase("ClubActivity")){
                            list.add(new LabelData("Rotary Service", ""));
                        }else {
                            list.add(new LabelData("District Event", ""));
                        }

                        list.addAll(rotaryList);
                    }

                    if (clubList != null && clubList.size() > 0) {

                        if ( clubList.size() > 0 ) {
                            list.add(new Separator());
                        }

                        if(type.equalsIgnoreCase("ClubActivity")){
                            list.add(new LabelData("Club Service", ""));
                        }else {
                            list.add(new LabelData("District Project", ""));
                        }


                        list.addAll(clubList);
                    }
                    ActivitiesNotificationAdapter adapter = new ActivitiesNotificationAdapter(context, list);
                    rv_activities.setAdapter(adapter);
                }
            }else {
                Toast.makeText(context,getString(R.string.msgRetry), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context,getString(R.string.msgRetry), Toast.LENGTH_LONG).show();
        }
    }
}
