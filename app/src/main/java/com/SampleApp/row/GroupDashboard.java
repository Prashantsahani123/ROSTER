package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.GroupDashboadAdapter_new;
import com.SampleApp.row.Data.ModuleData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.notification.GCMClientManager;
import com.SampleApp.row.sql.ModuleDataModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 31-12-2015.
 */
public class GroupDashboard extends Activity {

    GridView gv;
    TextView tv_title;
    ImageView iv_backbutton;
    public static  ArrayList<ModuleData> listmodules = new ArrayList<>();
    private ImageView iv_actionbtn;
    private String flag_callwebsercie ="0";
    GroupDashboadAdapter_new groupDashboadAdapter_new;
    ModuleDataModel moduleDataModel;
    long masteruid, groupId;
    int position;
    String group_Id;
    int myCategory = Constant.GROUP_CATEGORY_CLUB;
    int cnt=0,cnt_eve=0,cnt_ann=0, cnt_bulletin=0,cnt_doc=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.groupdashboard);

        boolean present = new ModuleDataModel(this).moduleExists(965, "11");

        Log.d("moduleExists", "Module with id 11 exists : "+present);

        Intent intent = getIntent();
        position= intent.getIntExtra("position",0);
        group_Id = intent.getStringExtra("groupId");
        myCategory = Integer.parseInt(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MY_CATEGORY));
        gv = (GridView) findViewById(R.id.gridView1);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn.setImageResource(R.drawable.overflow_btn_blue);

        if ( myCategory > Constant.GROUP_CATEGORY_CLUB) {
            iv_actionbtn.setVisibility(View.GONE);
        }

        //  iv_backbutton.setVisibility(View.GONE);
        tv_title.setText(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_NAME));

        moduleDataModel = new ModuleDataModel(this);
        moduleDataModel.printTable();

        Log.d("TouchBase", "*************** GRP ID PREF " + PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));

       // webservices();

        masteruid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID));
        groupId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));

        Log.e("ModuleList", listmodules.toString());

        listmodules = moduleDataModel.getModuleData(masteruid, groupId);

        groupDashboadAdapter_new = new GroupDashboadAdapter_new(GroupDashboard.this, listmodules, group_Id);
        gv.setAdapter(groupDashboadAdapter_new);

        /*gv.setAdapter(new GroupDashboadAdapter_new(this, listmodules,group_Id));*/

 		groupDashboadAdapter_new.notifyDataSetChanged();

        init();

        final SharedPreferences prefs = getGCMPreferences(GroupDashboard.this);
        String registrationId = prefs.getString(GCMClientManager.PROPERTY_REG_ID, "");

        Utils.log("GCM ID : "+registrationId);
        //loadRSS();
    }
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
       // groupDashboadAdapter_new.notifyDataSetChanged();
    }

    private void init() {

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            PopupMenu popup = new PopupMenu(GroupDashboard.this, iv_actionbtn);
            //Inflating the Popup using xml file
            //popup.getMenuInflater().inflate(R.menu.dashboard_menu, popup.getMenu());
            popup.getMenu().add(1, R.id.myprofile, 1, "My Profile");
//            popup.getMenu().add(1, R.id.settings, 2, "Settings");
            popup.getMenu().add(1, R.id.ll_anyclub, 2, "My Club");
           // popup.getMenu().add(1, R.id.selfremove, 3, "Exit Entity");
//            if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("Yes")) {
//                popup.getMenu().add(1, R.id.deleteentity, 4, "Delete Entity");
//            }
            //registering popup with OnMenuItemClickListener

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.myprofile:

                        Intent i = new Intent(getApplicationContext(), NewProfileActivity.class);
                        i.putExtra("memberProfileId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID));
                        i.putExtra("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
                        i.putExtra("fromMainDirectory", "yes");
                        startActivity(i);

                        // read the listItemPosition here
                        return true;

                    case R.id.settings:

                        if ( InternetConnection.checkConnection(getApplicationContext())) {
                            Intent ii = new Intent(getApplicationContext(), GroupSettings.class);
                            startActivity(ii);
                        } else {
                            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                        // read the listItemPosition here
                        return true;

                    case R.id.ll_anyclub:
                        Intent intent = new Intent(getApplicationContext(), MyClubActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.deleteentity:
                        /*Intent ii = new Intent(getApplicationContext(), GroupSettings.class);
                        startActivity(ii);*/

//                        final Dialog dialog = new Dialog(GroupDashboard.this, android.R.style.Theme_Translucent);
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog.setContentView(R.layout.popup_confrm_delete);
//                        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
//                        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
//                        TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
//                        tv_line1.setText("Are you sure you want to Delete this Entity ?");
//                        tv_no.setOnClickListener(new View.OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });
//                        tv_yes.setOnClickListener(new View.OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                if (InternetConnection.checkConnection(getApplicationContext())) {
//                                    //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
//                                    webservices_deleteentity();
//                                } else {
//                                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
//                                }
//                            }
//                        });
//
//                        dialog.show();
//
//                        // read the listItemPosition here
//                        return true;
                    case R.id.selfremove:
                            /*Intent ia = new Intent(getApplicationContext(),GroupSettings.class);
                            startActivity(ia);*/

//                        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
//                            //
//                            final Dialog dialog1 = new Dialog(GroupDashboard.this, android.R.style.Theme_Translucent);
//                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog1.setContentView(R.layout.popup_confrm_delete);
//                            TextView tv_no1 = (TextView) dialog1.findViewById(R.id.tv_no);
//                            TextView tv_yes1 = (TextView) dialog1.findViewById(R.id.tv_yes);
//                            TextView tv_line11 = (TextView) dialog1.findViewById(R.id.tv_line1);
//                            tv_line11.setText("Are you sure you want to get removed from Entity ?");
//                            tv_no1.setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    dialog1.dismiss();
//                                }
//                            });
//                            tv_yes1.setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    if (InternetConnection.checkConnection(getApplicationContext())) {
//                                        //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
//                                        webservices_removeself();
//                                    } else {
//                                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
//                                    }
//                                }
//                            });
//
//                            dialog1.show();
//
//                        } else {
//                            //String str = "You are the "+PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_NAME)+" Admin, you cannot delete yourself.";
//                            Utils.showToastWithTitleAndContext(getApplicationContext(), "You are the " + PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_NAME) + "'s Admin, you cannot delete yourself.");
//                        }
//                        // read the listItemPosition here
//                        return true;
                    default:
                        return false;
                }
                //return true;
                }
            });

            popup.show();
            }
        });


     }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("memberProfileId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));

        // arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        // arrayList.add(new BasicNameValuePair("memberProfileId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        flag_callwebsercie = "0";
        Log.d("Response", "PARAMETERS " + Constant.GetGroupModulesList + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsync(Constant.GetGroupModulesList, arrayList, GroupDashboard.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void webservices_removeself() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("type", "Member"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("profileID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));

        flag_callwebsercie = "1";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteByModuleName + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsync(Constant.DeleteByModuleName, arrayList, GroupDashboard.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }
    private void webservices_deleteentity() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("memberProfileId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID) ));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        //arrayList.add(new BasicNameValuePair("profileID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));

        flag_callwebsercie = "2";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteEntity + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsync(Constant.DeleteEntity, arrayList, GroupDashboard.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    public class WebConnectionAsync extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(GroupDashboard.this, R.style.TBProgressBar);

        public WebConnectionAsync(String url, List<NameValuePair> argList, Context ctx) {
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
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {
                if(flag_callwebsercie.equals("0")){
                getresult(result.toString());
                }else if(flag_callwebsercie.equals("1")){
                    getresultRemoved(result.toString());
                }else if(flag_callwebsercie.equals("2")){
                    getresultEntityDelete(result.toString());
                }
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBGetGroupModuleResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {

                JSONArray grpsarray = ActivityResult.getJSONArray("GroupListResult");
                for (int i = 0; i < grpsarray.length(); i++) {
                    JSONObject object = grpsarray.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("GroupList");

                    //listGroup.add(objects.getString("groupModuleId").toString());
                    ModuleData md = new ModuleData();
                    md.setGroupModuleId(objects.getString("groupModuleId").toString());
                    md.setGroupId(objects.getString("groupId").toString());
                    md.setModuleId(objects.getString("moduleId").toString());
                    md.setModuleName(objects.getString("moduleName").toString());
                    md.setModuleStaticRef(objects.getString("moduleStaticRef").toString());
                    // md.setImage(objects.getString("image").toString());

                    if (objects.has("image")) {
                        md.setImage(objects.getString("image").toString());
                    } else {
                        md.setImage("");
                    }
                    listmodules.add(md);
                }
                groupDashboadAdapter_new = new GroupDashboadAdapter_new(this,listmodules,group_Id);
                gv.setAdapter(groupDashboadAdapter_new);
				groupDashboadAdapter_new.notifyDataSetChanged();
            } else if ( status.equals("2")) {
                Utils.simpleMessage(GroupDashboard.this, ActivityResult.getString("message"), "Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }
    private void getresultRemoved(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {

                finish();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Failed to DELETE, please Try Again!");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }
    private void getresultEntityDelete(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("TBDeleteEntityResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {
                Intent i = new Intent(GroupDashboard.this, DashboardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Failed to DELETE, please Try Again!");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    //===========================get Notification Count Webservice =======================

    /*private void getNotificationCountWebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", "" + masteruid));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)

        Log.d("Response", "PARAMETERS " + Constant.GetNotificationCount + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionGetNotificationCount(Constant.GetNotificationCount, arrayList, getApplicationContext()).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }*/

    /*public class WebConnectionGetNotificationCount extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext(), R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public WebConnectionGetNotificationCount(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();

            *//*progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();*//*
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
            //progressDialog.dismiss();
            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {
                getNotificationCountResult(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }*/

    /*private void getNotificationCountResult(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject groupResult = jsonObj.getJSONObject("TBGroupResult");
            final String status = groupResult.getString("status");
            if (status.equals("0")) {
             //   listGroup = new ArrayList<String>();
                JSONArray groupCountList = groupResult.getJSONArray("GrpCountList");
                for (int i = 0; i < groupCountList.length(); i++) {

                    JSONObject result_object = groupCountList.getJSONObject(i);


                    NotificationCountData data = new NotificationCountData();
                    data.setGroupId(result_object.getString("groupId").toString());
                    data.setId2(result_object.getString("id2").toString());
                    data.setId3(result_object.getString("id3").toString());
                    data.setId4(result_object.getString("id4").toString());
                    data.setId9(result_object.getString("id9").toString());
                    data.setTotalCount(result_object.getString("totalCount").toString());

                    notificationCountDatas.add(data);
                }
                groupDashboadAdapter_new.notifyDataSetChanged();


            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_DOC && resultCode ==RESULT_OK){
            String groupID = data.getStringExtra("GroupID");
            refreshDashbord(groupID);
        }else if (requestCode == Constant.REQUEST_ANNOUNCEMENT && resultCode ==RESULT_OK) {
            String groupID = data.getStringExtra("GroupID");
            refreshDashbord(groupID);
        }else if (requestCode == Constant.REQUEST_EVENT && resultCode ==RESULT_OK) {
            String groupID = data.getStringExtra("GroupID");
            refreshDashbord(groupID);
        }else if (requestCode == Constant.REQUEST_EBULLITION && resultCode ==RESULT_OK) {
            String groupID = data.getStringExtra("GroupID");
            refreshDashbord(groupID);
        }

    }
    private void refreshDashbord(String groupID) {
        groupDashboadAdapter_new.notifyDataSetChanged();


        /*for(int i =0;i<notificationCountDatas.size();i++)
        {
            if(notificationCountDatas.get(i).getGroupId().equalsIgnoreCase(""+groupID))
            {
                int tempCountId2 = Integer.parseInt(notificationCountDatas.get(i).getId2());
                int tempCountId3 = Integer.parseInt(notificationCountDatas.get(i).getId3());
                int tempCountId4 = Integer.parseInt(notificationCountDatas.get(i).getId4());
                int tempCountId9 = Integer.parseInt(notificationCountDatas.get(i).getId9());
                int totalCount = tempCountId2+tempCountId3+tempCountId4+tempCountId9;
                notificationCountDatas.get(i).setTotalCount(""+totalCount);
            }
        }*/
    }



    //http://www.rotary.org/rss.xml
}
