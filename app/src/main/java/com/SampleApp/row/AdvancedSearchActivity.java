package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.Tables;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by USER1 on 21-04-2017.
 */
public class AdvancedSearchActivity extends Activity {
    private static final String DYNAMIC_FIELDS_FILE = "dynamicField.json";
    ImageView iv_backbutton, iv_actionbtn;
    TextView tv_title;
    String groupId = "";
    Context context;
    boolean isFieldPresent = false;
    LinearLayout llDynamicFields;
    Hashtable<String, EditText> componentTable;
    Hashtable<String, String[]> searchTable;
    Hashtable<String, String> tableMapping;

    TextView btnSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);

        context = this;
        groupId = getIntent().getStringExtra("groupId");
        componentTable = new Hashtable<>();

        searchTable = new Hashtable<>();
        tableMapping = new Hashtable<>();
        tableMapping.put("P", Tables.PersonalMemberDetails.TABLE_NAME);
        tableMapping.put("D", Tables.PersonalMemberDetails.TABLE_NAME);
        tableMapping.put("A", Tables.AddressDetails.TABLE_NAME);
        tableMapping.put("B", Tables.BusinessMemberDetails.TABLE_NAME);
        tableMapping.put("F", Tables.FamilyMemberDetail.TABLE_NAME);

        init();
        actionbarFunction();
        loadDynamicFields();
    }

    public void init() {
        llDynamicFields = (LinearLayout) findViewById(R.id.llDynamicFields);
        btnSearch = (TextView) findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    public void search() {
        boolean allEmpty = true;
        try {
            searchTable.clear();
            Enumeration<String> searchFields = componentTable.keys();

            while(searchFields.hasMoreElements()) {
                String fieldName = searchFields.nextElement();
                EditText etField = componentTable.get(fieldName);
                String value = etField.getText().toString();
                if  (!value.trim().equals("")) {
                    allEmpty = false;
                }
                String columnName = ((JSONObject) etField.getTag()).getString("dbColumnName");
                String tableName = tableMapping.get(((JSONObject) etField.getTag()).getString("fieldType"));
                String[] ar = new String[]{value, columnName, tableName};
                if ( !value.trim().equals("")) {
                    searchTable.put(fieldName, ar);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ( allEmpty ) {
            Toast.makeText(context, "Please enter value of atleast one field to search", Toast.LENGTH_LONG).show();
            return;
        }

        String searchData = new Gson().toJson(searchTable).toString();
        Utils.log("Search json : "+new Gson().toJson(searchTable).toString());
        Intent intent = new Intent(context, AdvancedSearchResultActivity.class);
        intent.putExtra("searchData", searchData);
        intent.putExtra("groupId", groupId);

        startActivity(intent);
    }

    public void loadDynamicFields() {
        try {
            FileInputStream fin = openFileInput(groupId+"_"+DYNAMIC_FIELDS_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while ( fin.available() != 0 ) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();
            isFieldPresent = true;
            handleLocalResult(new JSONObject(fieldData));
            Utils.log("Loaded from local file");
            //loadDynamicFieldsFromServer();
        } catch(FileNotFoundException fne) {
            Utils.log("Dynamic fields are not present in local file");
            loadDynamicFieldsFromServer();
        } catch(IOException fne) {
            Utils.log("Dynamic fields are not present in local file");
            loadDynamicFieldsFromServer();
        } catch(JSONException fne) {
            Utils.log("Dynamic fields are not present in local file");
            loadDynamicFieldsFromServer();
        }
    }
    public void loadDynamicFieldsFromServer() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(context);

            progressDialog.setMessage("Please wait. Loading search fields");

            Hashtable<String, String> params = new Hashtable<>();
            params.put("groupId", groupId);
            JSONObject requestParams = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Constant.GET_ADVANCED_SEARCH_FIELDS,
                requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if ( ! isFieldPresent ) {
                            progressDialog.hide();
                        }
                        handleSuccessResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if ( ! isFieldPresent ) {
                            progressDialog.hide();
                        }
                        Utils.log("Error is : " + error);
                        error.printStackTrace();
                    }
                });
            AppController.getInstance().addToRequestQueue(context, request);
            if ( ! isFieldPresent ) {
                progressDialog.show();
            }
        } catch(JSONException jse) {
            Utils.log("Error is : "+ jse);
        }
    }

    /*
    * {
        "SearchFilterResult": {
            "status": "0",
            "message": "success",
            "GroupFilters": [
                {
                    "filterID": "1",
                    "fieldID": "1",
                    "dbColumnName": "member_name",
                    "displayName": "Name",
                    "ColType": "Text",
                    "fieldType": "s",
                    "value": null
                },
                {
                    "filterID": "2",
                    "fieldID": "2",
                    "dbColumnName": "member_mobile_no",
                    "displayName": "Mobile Number",
                    "ColType": "Number",
                    "fieldType": "s",
                    "value": null
                }
            ]
        }
    }*/
    public void handleSuccessResult(JSONObject response) {
        try {
            Utils.log("Response : "+response);
            JSONObject filtersData = response.getJSONObject("SearchFilterResult");
            String status = filtersData.getString("status");
            String message = filtersData.getString("message");
            if ( status.equals("0")) {
                JSONArray fields = filtersData.getJSONArray("GroupFilters");
                try {
                    FileOutputStream fout = openFileOutput(groupId+"_"+DYNAMIC_FIELDS_FILE, MODE_PRIVATE);
                    fout.write(response.toString().getBytes());
                } catch(IOException ioe) {
                    Utils.log("Error is : "+ioe);
                    ioe.printStackTrace();
                }
                renderFields(fields);
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        } catch(JSONException jse) {
            Utils.log("Error is : "+jse);
            jse.printStackTrace();
        }
    }

    public void handleLocalResult(JSONObject response) {
        try {
            Utils.log("Response : "+response);
            JSONObject filtersData = response.getJSONObject("SearchFilterResult");
            String status = filtersData.getString("status");
            String message = filtersData.getString("message");
            if ( status.equals("0")) {
                JSONArray fields = filtersData.getJSONArray("GroupFilters");

                renderFields(fields);
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        } catch(JSONException jse) {
            Utils.log("Error is : "+jse);
            jse.printStackTrace();
        }
    }
    public void renderFields(JSONArray filterFields) {
        try {
            int n = filterFields.length();

            llDynamicFields.removeAllViews();
            componentTable.clear();


            for(int i=0; i<n; i++) {
                JSONObject jsonField = filterFields.getJSONObject(i);

                /*String filterID = jsonField.getString("filterID") ,
                        fieldID = jsonField.getString("fieldID"),
                        dbColumnName = jsonField.getString("dbColumnName"),
                        displayName = jsonField.getString("displayName"),
                        ColType = jsonField.getString("ColType"),
                        fieldType = jsonField.getString("fieldType");

                SearchFilter filter = new SearchFilter(filterID, fieldID, dbColumnName, displayName, ColType, fieldType);

                list.add(filter);*/
                String displayName = jsonField.getString("displayName");
                String fieldUniqueName = jsonField.getString("dbColumnName");
                String fieldType = jsonField.getString("fieldType");
                String value = "";

                String[] ar = new String[]{value, fieldType, fieldType};
                value = value + "," + jsonField.getString("dbColumnName");
                searchTable.put(fieldUniqueName, ar);


                TextView tvFieldTitle = new TextView(context);
                tvFieldTitle.setText(displayName);
                EditText etFieldValue = new EditText(context);
                etFieldValue.setBackground(getResources().getDrawable(R.drawable.border));
                llDynamicFields.addView(tvFieldTitle);
                llDynamicFields.addView(etFieldValue);

                int sizeInDp = 16;

                float scale = getResources().getDisplayMetrics().density;
                int bottomMargin = (int) (sizeInDp*scale + 0.5f);
                int topMargin = (int) (4*scale + 0.5f);

                int leftPadding = (int) (10*scale + 0.5f);
                int rightPadding = (int) (10*scale + 0.5f);
                int topPadding = (int) (5*scale + 0.5f);
                int bottomPadding = (int) (5*scale + 0.5f);

                LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                etParams.setMargins(0, topMargin, 0, bottomMargin);

                etFieldValue.setLayoutParams(etParams);
                etFieldValue.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
                etFieldValue.setHint(displayName);
                etFieldValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constant.NORMAL_TEXT_SIZE);
                etFieldValue.setSingleLine(true);
                etFieldValue.setTag(jsonField);


                componentTable.put(fieldUniqueName, etFieldValue);
            }
        } catch(JSONException jse) {
            Utils.log("Error : " + jse);
            jse.printStackTrace();
        }
    }
    private void actionbarFunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        tv_title.setText("Advanced Search");
        iv_actionbtn.setVisibility(View.GONE);

        /*iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }
}
