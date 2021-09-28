package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.RotaryIndiaAdminAdapter;
import com.NEWROW.row.ModuleData.RotaryIndiaAdminModule;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.NEWROW.row.Utils.PreferenceManager.IS_GRP_ADMIN;
import static com.NEWROW.row.Utils.PreferenceManager.savePreference;

public class RotaryIndia extends AppCompatActivity {

    GridView gv;
    RecyclerView rv_grpList;
    TextView tv_title;
    ImageView iv_backbutton;
    //ArrayList of Rotary India
    String[] appName_RI = {"Rotary India Website", "Cashback", "Rotary India Leaders", "Rotary India Web-Committee"};
    Integer[] appImg_RI = {R.drawable.riwebsite, R.drawable.ricashback, R.drawable.rileaders, R.drawable.riwebc};
    //ArrayList of Rotary World
    String[] appName_RW = {"Library", "Rotary News", "Rotary Blog", "Rotary.org"};
    Integer[] appImg_RW = {R.drawable.library, R.drawable.news, R.drawable.blog, R.drawable.rotary};
    String title = "";

    //ArrayList Added

    ArrayList<RotaryIndiaAdminModule> rotaryAdminList = new ArrayList<>();
    //Context
    Context context;


    private static final String TAG = RotaryIndia.class.getName();
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    RequestQueue mque;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotray_layout);
        //  gv = (GridView) findViewById(R.id.gridView1);
        context = RotaryIndia.this;

        rv_grpList = (RecyclerView) findViewById(R.id.rv_grpList);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        tv_title.setText(title);

        if (tv_title.getText().equals("Rotary India")) {


            if (InternetConnection.checkConnection(context)) {
                progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
                progressDialog.show();
                GetRotaryIndiaAdminModules();
            } else {
                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();

            }


        }


    }


    public void GetRotaryIndiaAdminModules() {

        Log.d("Parameter", Constant.GetRotaryIndiaAdminModules);
       /* final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
        progressDialog.show();*/


        StringRequest adminrequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, Constant.GetRotaryIndiaAdminModules, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                progressDialog.dismiss();

                getResult(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Error.Response", String.valueOf(error));
                progressDialog.dismiss();


            }
        });

        adminrequest.setRetryPolicy(
                new DefaultRetryPolicy(70000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        AppController.getInstance().addToRequestQueue(context, adminrequest);

        //  mque.add(adminrequest);


    }

    private void getResult(String response) {


        try {

            JSONObject object = new JSONObject(response);


            object = object.getJSONObject("TBRotaryIndiaModulesResult"); //AdminmobilepupupResult

            String status = object.getString("status");

            if (status.equals("0")) {
                //Response get data
                JSONArray RotaryIndiaModulesResult = object.getJSONArray("RotaryIndiaModulesResult");


                for (int i = 0; i < RotaryIndiaModulesResult.length(); i++) {
                    RotaryIndiaAdminModule adminModule = new RotaryIndiaAdminModule();

                    JSONObject moduleObject = RotaryIndiaModulesResult.getJSONObject(i);
                    adminModule.setModuleName(moduleObject.getString("moduleName"));
                    adminModule.setGroupId(moduleObject.getString("groupId"));
                    adminModule.setModuleId(moduleObject.getString("moduleId"));
                    adminModule.setModuleOrderNo(moduleObject.getString("moduleOrderNo"));
                    adminModule.setImage(moduleObject.getString("image"));
                    adminModule.setIsweblink(moduleObject.getString("isweblink"));
                    adminModule.setURL(moduleObject.getString("URL"));
                    rotaryAdminList.add(adminModule);


                }


                //Set Recyclerview in GridView format
                setLayout();

                //Set Adapter
                RotaryIndiaAdminAdapter adapter_admin = new RotaryIndiaAdminAdapter(context, rotaryAdminList);


                //Module Click Listner
                adapter_admin.setOnGroupSelectedListener(new RotaryIndiaAdminAdapter.OnGroupSelectedListener() {
                    @Override
                    public void onGroupSelected(int position) {

                        try {
                            String moduleName = ((RotaryIndiaAdminModule) rotaryAdminList.get(position)).getModuleName();
                            String isWebLink = ((RotaryIndiaAdminModule) rotaryAdminList.get(position)).getIsweblink();
                            String URL = ((RotaryIndiaAdminModule) rotaryAdminList.get(position)).getURL();


                            //If groupId is -1 then data only view


                            String loginType = PreferenceManager.getPreference(context, PreferenceManager.LOGIN_TYPE);

                            Utils.log("Login Type : " + loginType);

                            if (loginType.equals("1")) {
                                savePreference(context, IS_GRP_ADMIN, "No");
                            } else {
                                savePreference(context, IS_GRP_ADMIN, "YES");
                            }

                           /* if (rotaryAdminList.get(position).getGroupId().equals("-1")) {
                                savePreference(context, IS_GRP_ADMIN, "No");
                            }*/

                            //If is it weblink then call this class
                            if (isWebLink.equalsIgnoreCase("1")) {

                                Intent i1 = new Intent(RotaryIndia.this, OpenLinkActivity.class);
                                i1.putExtra("modulename", moduleName);
                                i1.putExtra("link", URL);
                                startActivity(i1);

                            } else if (moduleName.equalsIgnoreCase("Announcements")) {

                                //Consider MasterUId as profile id in Rotary India admin  module

                                Long masterUid = Long.parseLong(PreferenceManager.getPreference(context, PreferenceManager.MASTER_USER_ID));


                                String profile_id = String.valueOf(PreferenceManager.savePreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID, String.valueOf(masterUid)));

                                Intent i = new Intent(context, Announcement.class);
                                i.putExtra("moduleName", rotaryAdminList.get(position).getModuleName());
                                i.putExtra("GroupID", String.valueOf(rotaryAdminList.get(position).getGroupId()));
                                i.putExtra("moduleID", String.valueOf(rotaryAdminList.get(position).getModuleId()));
                                PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, rotaryAdminList.get(position).getModuleId());
                                PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, rotaryAdminList.get(position).getModuleName());
                                PreferenceManager.savePreference(context, PreferenceManager.isRIadminModule, "Yes");
                                PreferenceManager.savePreference(context, PreferenceManager.GROUP_ID, rotaryAdminList.get(position).getGroupId());


                                context.startActivity(i);


                                //startActivity(i);
                            } else if (moduleName.equalsIgnoreCase("Events")) {

                                //Consider MasterUId as profile id in Rotary India admin  module

                                Long masterUid = Long.parseLong(PreferenceManager.getPreference(context, PreferenceManager.MASTER_USER_ID));

                                String profile_id = String.valueOf(PreferenceManager.savePreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID, String.valueOf(masterUid)));


                                Intent i = new Intent(context, Events.class);
                                i.putExtra("moduleName", rotaryAdminList.get(position).getModuleName());
                                i.putExtra("GroupID", String.valueOf(rotaryAdminList.get(position).getGroupId()));
                                PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, rotaryAdminList.get(position).getModuleId());
                                PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, rotaryAdminList.get(position).getModuleName());
                                PreferenceManager.savePreference(context, PreferenceManager.isRIadminModule, "Yes");
                                PreferenceManager.savePreference(context, PreferenceManager.GROUP_ID, rotaryAdminList.get(position).getGroupId());

                                context.startActivity(i);
                            } else if (moduleName.equalsIgnoreCase("Documents")) {

                                //Consider MasterUId as profile id in Rotary India admin  module

                                Long masterUid = Long.parseLong(PreferenceManager.getPreference(context, PreferenceManager.MASTER_USER_ID));


                                String profile_id = String.valueOf(PreferenceManager.savePreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID, String.valueOf(masterUid)));


                                Intent i = new Intent(context, Documents.class);
                                i.putExtra("moduleName", rotaryAdminList.get(position).getModuleName());
                                PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, rotaryAdminList.get(position).getModuleId());
                                PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, rotaryAdminList.get(position).getModuleName());
                                i.putExtra("GroupID", String.valueOf(rotaryAdminList.get(position).getGroupId()));
                                PreferenceManager.savePreference(context, PreferenceManager.isRIadminModule, "Yes");
                                PreferenceManager.savePreference(context, PreferenceManager.GROUP_ID, rotaryAdminList.get(position).getGroupId());


                                context.startActivity(i);

                            } else if (moduleName.equalsIgnoreCase("Newsletters")) {
                                //Consider MasterUId as profile id in Rotary India admin  module
                                Long masterUid = Long.parseLong(PreferenceManager.getPreference(context, PreferenceManager.MASTER_USER_ID));

                                String profile_id = String.valueOf(PreferenceManager.savePreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID, String.valueOf(masterUid)));


                                Intent i = new Intent(context, E_Bulletin.class);
                                i.putExtra("moduleName", rotaryAdminList.get(position).getModuleName());
                                PreferenceManager.savePreference(context, PreferenceManager.MODULE_ID, rotaryAdminList.get(position).getModuleId());
                                PreferenceManager.savePreference(context, PreferenceManager.MODUEL_NAME, rotaryAdminList.get(position).getModuleName());
                                i.putExtra("GroupID", String.valueOf(rotaryAdminList.get(position).getGroupId()));
                                i.putExtra("GroupID", String.valueOf(rotaryAdminList.get(position).getGroupId()));
                                PreferenceManager.savePreference(context, PreferenceManager.isRIadminModule, "Yes");
                                PreferenceManager.savePreference(context, PreferenceManager.GROUP_ID, rotaryAdminList.get(position).getGroupId());
                                context.startActivity(i);

                            } else {
                                Toast.makeText(RotaryIndia.this, moduleName + " Coming Soon !!", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                        }

                    }
                });

                rv_grpList.setAdapter(adapter_admin);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setLayout() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 12);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {

                if (rotaryAdminList.size() % 3 == 0) {
                    return 4;
                } /*else {

                    if (position == rotaryAdminList.size() - 1) {
                        return 12;
                    }
                    return 4;
                }*/
                return 4;
            }
        });

        rv_grpList.setLayoutManager(gridLayoutManager);


    }


}
