package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.AutoCompleteTextviewAdapter;
import com.SampleApp.row.Adapter.ShowCaseAdapter;
import com.SampleApp.row.Adapter.SpinnerAdapter;
import com.SampleApp.row.Data.AlbumData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ShowCaseCategoryActivity extends Activity {

    public RecyclerView rv_cat;
    public Button btnSearch;
    public Context context;
    public ArrayList<AlbumData> list = new ArrayList<>();
    public ShowCaseAdapter adapter;
    ProgressDialog progressDialog;
    CheckBox cbAll;
    RelativeLayout rlNoData;
    TextView tvNoData;
    Button btnRetry;
    Spinner sp_year, sp_district;
    private String fromYear, toYear;
    ArrayList<String> filtertype;
    ArrayList<AlbumData> categoryList = new ArrayList<>();
    ArrayList<AlbumData> districtList = new ArrayList<>();
    ArrayList<AlbumData> clubList = new ArrayList<>();
    SpinnerAdapter spAdapter, clubAdapter;
    String districtId = "", clubId = "0", categoryIds = "";
    AutoCompleteTextView sp_club;
    AutoCompleteTextviewAdapter autoAdapter;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_case_category);

        context = this;


        rv_cat = (RecyclerView) findViewById(R.id.rv_cat);
        rv_cat.setLayoutManager(new LinearLayoutManager(context));


        Intent intent = getIntent();
        //String modulename = intent.getExtras().getString("moduleName", "Showcase");
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Showcase");

        rlNoData = (RelativeLayout) findViewById(R.id.rl_NoData);
        tvNoData = (TextView) findViewById(R.id.tvNoData);
        btnRetry = (Button) findViewById(R.id.btnRetry);
        btnSearch = (Button) findViewById(R.id.tv_yes);

        init();

        if (InternetConnection.checkConnection(ShowCaseCategoryActivity.this)) {
            getAllShowcaseDetails();
        } else {
            Toast.makeText(ShowCaseCategoryActivity.this, "No Internet connection.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void init() {

        sp_year = (Spinner) findViewById(R.id.sp_year);
        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = sp_year.getSelectedItem().toString();
                String array[] = selectedYear.split("-");
                fromYear = array[0];
                toYear = array[1];
                Utils.log(fromYear + " " + toYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_district = (Spinner) findViewById(R.id.sp_district);
        sp_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AlbumData data = districtList.get(position);
                districtId = data.getDistrict_id();
                if (flag != 0) {
                    getClublist(data.getDistrict_id());
                }
                flag = 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_club = (AutoCompleteTextView) findViewById(R.id.sp_club);
        sp_club.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof AlbumData) {
                    AlbumData data = (AlbumData) item;
                    clubId = data.getDistrict_id();
                    sp_club.setText(data.getDistrict_Name());
                    sp_club.setSelection(data.getDistrict_Name().length());
                }
            }
        });


//        sp_club.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                AlbumData data =  clubList.get(position);
//                clubId = data.getDistrict_id();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Utils.log("Month: " + month + " year : " + year);
        if (month > 6) {
            fromYear = String.valueOf(year);
            toYear = String.valueOf(year + 1);
        } else {
            toYear = String.valueOf(year);
            fromYear = String.valueOf(year - 1);
        }

        filtertype = new ArrayList<>();
        int flag = 0;
        for (int i = year; i >= 2015; i--) {
            String filterYear;

            if (month > 6 && i == year) {
                filterYear = (i) + "-" + (i + 1);
                flag=1;
            } else if (month <= 6 && i == year) {
                filterYear = (i-1) + "-" + (i);
                flag=2;
            } else {
                if(flag==1){
                    filterYear = (i) + "-" + (i+1);
                }else {
                    filterYear = (i-1) + "-" + (i);
                }

            }


            filtertype.add(filterYear);
        }
        if(flag!=1){

            filtertype.remove(filtertype.size()-1);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype);
        sp_year.setAdapter(spinnerArrayAdapter);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.checkConnection(ShowCaseCategoryActivity.this)) {
                    if (validation()) {
                        categoryIds = "";
                        Intent i = new Intent(ShowCaseCategoryActivity.this, Gallery.class);
                        i.putExtra("moduleName", "Gallery");
                        i.putExtra("grpId", PreferenceManager.getPreference(ShowCaseCategoryActivity.this, PreferenceManager.GROUP_ID));
                        i.putExtra("districtId", districtId);
                        if (sp_club.getText().length() != 0) {
                            i.putExtra("clubId", clubId);
                        } else {
                            i.putExtra("clubId", clubList.get(0).getDistrict_id());
                        }
                        i.putExtra("fromShowcase", "0");
                        if (adapter.getCategoryList().get(0).isSelected()) {
                            categoryIds = "0";
                        } else {
                            for (int i1 = 1; i1 < adapter.getCategoryList().size(); i1++) {

                                if (adapter.getCategoryList().get(i1).isSelected()) {
                                    categoryIds = categoryIds.concat(adapter.getCategoryList().get(i1).getCat_id()) + ",";
                                }
                            }

                            if (categoryIds.charAt(categoryIds.length()-1)==',') {
                                categoryIds = categoryIds.substring(0, categoryIds.length() - 1);
                            }

                        }

                        i.putExtra("categoryList", categoryIds);
                        i.putExtra("year", sp_year.getSelectedItem().toString());
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(ShowCaseCategoryActivity.this, "No Internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getClublist(String district_id) {

        try {
            progressDialog = new ProgressDialog(ShowCaseCategoryActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            requestData.put("DistrictID", district_id);

            Log.d("Response", "PARAMETERS " + Constant.GetShowcaseDetails + " :- " + requestData.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetShowcaseDetails, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    getClubs(response);
                    Utils.log(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Utils.log("VollyError:- " + error.toString());
                    //showErrorDialog();
                    //Utils.showMsg(context, "Something went wrong");
                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
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

    private void getClubs(JSONObject response) {
        JSONObject ShowcaseDetails = null;
        try {
            ShowcaseDetails = response.getJSONObject("ShowcaseDetails");

            String status = ShowcaseDetails.getString("status");
            if (status.equalsIgnoreCase("0")) {
                JSONObject result = ShowcaseDetails.getJSONObject("Result");

                JSONArray clubObj = result.getJSONArray("Club");
                clubList.clear();
                if (clubObj.length() > 0) {
                    for (int i = 0; i < clubObj.length(); i++) {
                        JSONObject club = clubObj.getJSONObject(i);
                        AlbumData data = new AlbumData();
                        data.setDistrict_Name(club.getString("Name"));
                        data.setDistrict_id(club.getString("ID"));
                        clubList.add(data);
                    }
                    autoAdapter = new AutoCompleteTextviewAdapter(ShowCaseCategoryActivity.this, android.R.layout.simple_list_item_1, clubList);
                    autoAdapter.notifyDataSetChanged();
                    sp_club.setText(clubList.get(0).getDistrict_Name());
                    sp_club.setSelection(clubList.get(0).getDistrict_Name().length());
                    sp_club.setAdapter(autoAdapter);
                    clubId="0";
                }
            }
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getAllShowcaseDetails() {

        try {
            progressDialog = new ProgressDialog(ShowCaseCategoryActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            requestData.put("DistrictID", "0");

            Log.d("Response", "PARAMETERS " + Constant.GetShowcaseDetails + " :- " + requestData.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetShowcaseDetails, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    setAllShowcaseDetails(response);
                    Utils.log(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Utils.log("VollyError:- " + error.toString());
                    //showErrorDialog();
                    //Utils.showMsg(context, "Something went wrong");
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
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

    private void setAllShowcaseDetails(JSONObject response) {
        try {
            JSONObject ShowcaseDetails = response.getJSONObject("ShowcaseDetails");
            String status = ShowcaseDetails.getString("status");
            if (status.equalsIgnoreCase("0")) {
                JSONObject result = ShowcaseDetails.getJSONObject("Result");
                JSONArray Categories = result.getJSONArray("Categories");
                for (int i = 0; i < Categories.length(); i++) {
                    JSONObject categoryObj = Categories.getJSONObject(i);
                    AlbumData data = new AlbumData();
                    data.setCat_id(categoryObj.getString("ID"));
                    data.setCat_name(categoryObj.getString("Name"));
                    categoryList.add(i, data);
                }
                adapter = new ShowCaseAdapter(categoryList, context);
                rv_cat.setAdapter(adapter);
                adapter.selectAll(true);

                JSONArray districtObj = result.getJSONArray("District");
                for (int i = 0; i < districtObj.length(); i++) {
                    JSONObject district = districtObj.getJSONObject(i);
                    AlbumData data = new AlbumData();
                    data.setDistrict_Name(district.getString("Name"));
                    data.setDistrict_id(district.getString("ID"));
                    districtList.add(data);
                }
                spAdapter = new SpinnerAdapter(context, districtList);
                sp_district.setAdapter(spAdapter);


                JSONArray clubObj = result.getJSONArray("Club");
                for (int i = 0; i < clubObj.length(); i++) {
                    JSONObject club = clubObj.getJSONObject(i);
                    AlbumData data = new AlbumData();
                    data.setDistrict_Name(club.getString("Name"));
                    data.setDistrict_id(club.getString("ID"));
                    clubList.add(data);
                }
                autoAdapter = new AutoCompleteTextviewAdapter(ShowCaseCategoryActivity.this, android.R.layout.simple_list_item_1, clubList);
                sp_club.setText(clubList.get(0).getDistrict_Name());
                sp_club.setSelection(clubList.get(0).getDistrict_Name().length());
                sp_club.setAdapter(autoAdapter);
                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

    }


//    private void getCatList() {
//        try {
//            JSONObject requestData = new JSONObject();
//            requestData.put("groupId", "1");
//
//
////            requestData.put("MasterId", "157542");
//            Log.d("Response", "PARAMETERS " + Constant.GetShowcaseList + " :- " + requestData.toString());
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetShowcaseList, requestData, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//                    JSONObject result;
////                    globalResponse=response;
//                    setCatData(response);
//                    //loadRssBlogs();
//                    Utils.log(response.toString());
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if(progressDialog!=null){
//                        progressDialog.dismiss();
//                    }
//                    Utils.log("VollyError:- " + error.toString());
//                    showErrorDialog();
//                    //Utils.showMsg(context, "Something went wrong");
//                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
//                }
//            });
//
//            request.setRetryPolicy(
//                    new DefaultRetryPolicy(120000,
//                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//            AppController.getInstance().addToRequestQueue(context, request);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setCatData(JSONObject response){
//        list=new ArrayList<>();
//
//        list.clear();
//        try {
//            JSONObject TBCSRResult=response.getJSONObject("TBShowcaseListResult");
//            String status=TBCSRResult.getString("status");
//            if(status.equals("0")){
//
//                JSONArray CSRList=TBCSRResult.getJSONArray("Result");
//                for(int i=0;i<CSRList.length();i++){
//                    JSONObject object=CSRList.getJSONObject(i);
//                    AlbumData data=new AlbumData();
//                    data.setCat_id(object.getString("CategoryId"));
//                    data.setCat_name(object.getString("CategoryName"));
//                    list.add(data);
//                }
//
//                ShowCaseAdapter adapter=new ShowCaseAdapter(list,context);
//                rv_cat.setAdapter(adapter);
//
//            }else {
//                showErrorDialog();
//                //Utils.showMsg(context,getString(R.string.messageSorry));
//            }
//
//            if(progressDialog!=null){
//                progressDialog.dismiss();
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            if(progressDialog!=null){
//                progressDialog.dismiss();
//            }
//        }
//    }
//
//
//    public void showErrorDialog() {
//
//        LayoutInflater inflater = getLayoutInflater();
//        final View alertLayout = inflater.inflate(R.layout.popup_for_error_msg, null);
//        TextView tv_yes = (TextView) alertLayout.findViewById(R.id.tv_yes);
//        tv_yes.setText("Retry");
//        TextView tv_cancel = (TextView) alertLayout.findViewById(R.id.tv_cancel);
//        TextView tv_line1 = (TextView) alertLayout.findViewById(R.id.tv_line1);
//        TextView tv_header = (TextView) alertLayout.findViewById(R.id.tv_header);
//        View view = alertLayout.findViewById(R.id.view);
//        view.setVisibility(View.GONE);
//
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("");
//        tv_header.setVisibility(View.VISIBLE);
//        tv_header.setTypeface(tv_header.getTypeface(), Typeface.BOLD);
//        //tv_header.setText("GPS is not Enabled!");
//        tv_header.setVisibility(View.GONE);
//        tv_line1.setText(R.string.serverError);
//        // this is set the view from XML inside AlertDialog
//        alert.setView(alertLayout);
//        // disallow cancel of AlertDialog on click of back button and outside touch
//        alert.setCancelable(false);
//        final AlertDialog dialog = alert.create();
//
//        tv_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (InternetConnection.checkConnection(context)){
//                    progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
//                    progressDialog.setCancelable(false);
//                    progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//                    progressDialog.show();
//                    getCatList();
//                    rlNoData.setVisibility(View.GONE);
//                }
//                else {
//                    //Utils.showMsg(context,getString(R.string.noInternet));
//                    rlNoData.setVisibility(View.VISIBLE);
//                    tvNoData.setText(R.string.noInternet);
//                }
//                dialog.dismiss();
//            }
//        });
//
//
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
//
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    finish();
//                    dialog.dismiss();
//                }
//                return true;
//            }
//
//        });
//    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean validation() {

        if (adapter != null) {
            for (int i1 = 0; i1 < adapter.getCategoryList().size(); i1++) {
                if (adapter.getCategoryList().get(i1).isSelected()) {
                    return true;
                }
            }
        }
        Toast.makeText(ShowCaseCategoryActivity.this, "Please select atleast one category.", Toast.LENGTH_SHORT).show();
        return false;
    }

}
