package com.NEWROW.row;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
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

public class Subproject extends AppCompatActivity {

    ProgressDialog progressDialog;
    private RecyclerView ongrecyl;
    ArrayList<Subpro_model> subpro_arr = new ArrayList<>();
    Subpro_adapter subpro_adapter ;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subproject);
        ongrecyl  = findViewById(R.id.ongrecyl);

        ongrecyl.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ongrecyl.setLayoutManager(mLayoutManager);



        getong_pro();

    }

    private void getong_pro() {

        try {

            progressDialog = new ProgressDialog(Subproject.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            //  Log.d("Responseeeeee", "PARAMETERS " + Constant.GetAlbumsList_New + " :- " + requestData.toString());
            JSONObject requestData = new JSONObject();
            requestData.put("GallaryId","3");

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.ongproject_detail,requestData , new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    JSONObject result;
                    Log.d("ongoingprojectdetails", "PARAMETERS " + Constant.ongoingproject + " :- " + response.toString());

                    subproject(response);
                    Utils.log(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    Utils.log("VollyError:- " + error.toString());

                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(Subproject.this, request);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void subproject(JSONObject response) {

        try {
            String status = response.getString("status");

            if (status.equalsIgnoreCase("0")) {

                JSONObject businessObject = response.getJSONObject("SubProjResult");

                JSONArray detail = businessObject.getJSONArray("Table");


                for (int i = 0; i < detail.length(); i++) {


                    Subpro_model subpro_model = new Subpro_model();

                    JSONObject categoryObj = detail.getJSONObject(i);

                    String pk_gallery_id = (categoryObj.getString("pk_gallery_id"));
                    String fk_group_master_id = (categoryObj.getString("fk_group_master_id"));
                    String date_of_project = (categoryObj.getString("date_of_project"));
                    String album_title = (categoryObj.getString("album_title"));
                    String cost_of_project = (categoryObj.getString("cost_of_project"));
                    String beneficiary = (categoryObj.getString("beneficiary"));
                    String man_hours_spent = (categoryObj.getString("man_hours_spent"));
                    String NumberOfRotarian = (categoryObj.getString("NumberOfRotarian"));
                    String Rotaractors = (categoryObj.getString("Rotaractors"));
                    String OnetimeOrOngoing = (categoryObj.getString("OnetimeOrOngoing"));
                    String NewOrExisting = (categoryObj.getString("NewOrExisting"));

                    subpro_model.setPk_gallery_id(pk_gallery_id);
                    subpro_model.setFk_group_master_id(fk_group_master_id);
                    subpro_model.setDate_of_project(date_of_project);
                    subpro_model.setAlbum_title(album_title);
                    subpro_model.setCost_of_project(cost_of_project);
                    subpro_model.setBeneficiary(beneficiary);
                    subpro_model.setMan_hours_spent(man_hours_spent);
                    subpro_model.setNumberOfRotarian(NumberOfRotarian);

                    subpro_model.setRotaractors(Rotaractors);
                    subpro_model.setOnetimeOrOngoing(OnetimeOrOngoing);
                    subpro_model.setNewOrExisting(NewOrExisting);

                    subpro_arr.add(subpro_model);

                }

                subpro_adapter = new Subpro_adapter(getApplicationContext(), subpro_arr);

                ongrecyl.setAdapter(subpro_adapter);
              //  ongrecyl.setVisibility(View.VISIBLE);
//




            } else {
                //progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //progressDialog.dismiss();
        }

    }

}
