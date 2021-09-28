package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Data.RotaryLibraryData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutDeveloperNew extends AppCompatActivity {

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
        setContentView(R.layout.activity_about_developer_new);

        context = this;

        Bundle i = getIntent().getExtras();
        if (i!=null){
            moduleName = i.getString("moduleName");
            grpID = i.getString("grpID");
            moduleID = i.getString("moduleID");
        }



        actionbarfunction();

        init();

        loadRotaryLibraryFromServer();

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

    public void loadRotaryLibraryFromServer() {
//        Hashtable<String,String> params = new Hashtable<>();
//        params.put("grpId",grpId);
        try {

            final ProgressDialog progressDialog = new ProgressDialog(AboutDeveloperNew.this, R.style.TBProgressBar);
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
                            Toast.makeText(AboutDeveloperNew.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(AboutDeveloperNew.this, request);

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
                          //  tv_title.setText(data.getTitle());
                            tv_title.setText("About Developer");
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
