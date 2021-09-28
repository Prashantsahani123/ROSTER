package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.NEWROW.row.Adapter.GroupDashboardAdminAdapter;
import com.NEWROW.row.Data.ModuleDataAdmin;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
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

public class GroupDashboardAdmin extends AppCompatActivity {

    private RecyclerView gv;
    private TextView tv_title;
    private Context ctx;
    private String title="",groupId="";
    private ArrayList<ModuleDataAdmin> dataAdminArrayList = new ArrayList<>();
    private GroupDashboardAdminAdapter adminAdapter;
    ProgressDialog refreshDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_dashboard_admin);

        ctx = GroupDashboardAdmin.this;

        intit();

        gv.setLayoutManager(new GridLayoutManager(this, 3));

        title = getIntent().getExtras().getString("moduleName", "Admin");

        groupId = getIntent().getExtras().getString("GroupID", "");

        Log.e("admin","satish group id => "+groupId);

        tv_title.setText(title);

        getModuleList();
    }

    private void intit(){
        gv = (RecyclerView) findViewById(R.id.gridView1);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    private void getModuleList(){

        if(InternetConnection.checkConnection(ctx)){

            try {

                refreshDialog = new ProgressDialog(ctx, R.style.TBProgressBar);
                refreshDialog.setCancelable(false);
                refreshDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                refreshDialog.show();

                JSONObject requestData = new JSONObject();

                requestData.put("Fk_groupID", PreferenceManager.getPreference(ctx, PreferenceManager.GROUP_ID));
                requestData.put("fk_ProfileID",PreferenceManager.getPreference(ctx, PreferenceManager.GRP_PROFILE_ID));

                Log.d("Response", "PARAMETERS " + Constant.GetAdminModulelist + " :- " + requestData.toString());

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetAdminModulelist, requestData, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        refreshDialog.dismiss();
                        Utils.log(response.toString());
                        parseData(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        refreshDialog.dismiss();
                        Utils.log("VollyError:- " + error.toString());
                        //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });

                request.setRetryPolicy(
                        new DefaultRetryPolicy(120000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(ctx, request);

            } catch (Exception e) {
                refreshDialog.dismiss();
                e.printStackTrace();
            }

        } else {
            Utils.showToastWithTitleAndContext(ctx,getString(R.string.noInternet));
        }

    }

    private void parseData(JSONObject response){

        try {

            JSONObject mainObject = response.getJSONObject("AdminSubmodulesResult");
            String status = mainObject.getString("status");

            if(status.equalsIgnoreCase("0")){

                JSONArray list = mainObject.getJSONArray("list");

                dataAdminArrayList.clear();

                for(int i=0;i<list.length();i++){

                    JSONObject jsonObject = list.getJSONObject(i);

                    String ModuleID = jsonObject.getString("ModuleID");
                    String Title = jsonObject.getString("Title");
                    String imgurl = jsonObject.getString("imgurl");
                    String url = jsonObject.getString("url");
                    String UserName = jsonObject.getString("UserName");
                    String Pass = jsonObject.getString("Pass");
                    String fk_countryID = jsonObject.getString("fk_countryID");

                    ModuleDataAdmin moduleDataAdmin = new ModuleDataAdmin(ModuleID,Title,imgurl,url,UserName,Pass,fk_countryID);

                    dataAdminArrayList.add(moduleDataAdmin);
                }

                adminAdapter = new GroupDashboardAdminAdapter(dataAdminArrayList,ctx,groupId);

                gv.setAdapter(adminAdapter);

            } else {
                Utils.showToastWithTitleAndContext(ctx,getString(R.string.msgRetry));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
