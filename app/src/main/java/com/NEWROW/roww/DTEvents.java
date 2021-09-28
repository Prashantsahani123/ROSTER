package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.DTEventAdapter;
import com.NEWROW.row.Data.DTEventsData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by USER on 17-12-2015.
 */
public class DTEvents extends Activity {

    private DTEventAdapter adapter;
    private ArrayList<DTEventsData> list;
    private RecyclerView.LayoutManager mLayoutmanager;
    private RecyclerView rvEvents;
    private TextView tv_title;
    private ImageView iv_backbutton;
    private EditText et_search;
    private String moduleName = "Events";
    private String grpID = "0";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dt_events);

        context = this;

        et_search = (EditText) findViewById(R.id.et_serach);
        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);
        mLayoutmanager = new LinearLayoutManager(context);
        rvEvents.setLayoutManager(mLayoutmanager);

        adapter = new DTEventAdapter(context, list);
        adapter.setOnItemSelectedListener(onItemSelectedListener);
        adapter.notifyDataSetChanged();

        actionbarfunction();
        Intent intenti = getIntent();
        grpID = intenti.getStringExtra("groupId");
        init();
        loadEvents();
        // condition is compared here because if user is not admin so spinner is not visible and websevie method is never called for him. So to call webservice.
        // below code is written.
    }

    public void loadEvents() {

        if ( ! InternetConnection.checkConnection(context) ) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }
        Hashtable<String, String> params = new Hashtable<>();
        params.put("grpID", grpID);

        try {
            JSONObject jsonParams = new JSONObject(new Gson().toJson(params));

            Utils.log("URL : "+Constant.DT_GET_EVENTS+" PARAMETERS : "+jsonParams);

            final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.DT_GET_EVENTS,
                    jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.hide();
                            processSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            processError(error);
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(context, request);
            progressDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processSuccess(JSONObject response) {
        try {
            Utils.log("Response : "+response);
            JSONObject result = response.getJSONObject("TBPublicEventList");
            String status = result.getString("status");
            if ( status.equals("0")) {
                String eventsList = result.getJSONArray("Result").toString();
                TypeToken<ArrayList<DTEventsData>> typeToken = new TypeToken<ArrayList<DTEventsData>>(){};
                list = new Gson().fromJson(eventsList, typeToken.getType());
                adapter = new DTEventAdapter(context, list);
                rvEvents.setAdapter(adapter);
                adapter.setOnItemSelectedListener(onItemSelectedListener);
            } else {
                Toast.makeText(context, R.string.messageSorry, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.messageSorry, Toast.LENGTH_LONG).show();
        }
    }

    public void processError(VolleyError error) {
        Toast.makeText(context, R.string.messageSorry, Toast.LENGTH_LONG).show();
    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText(moduleName);
    }

    public void init() {

        /*iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //adapter.getFilter().filter(cs);
                //adapter.getFilter().filter(cs.toString());

                int textlength = cs.length();
                ArrayList<DTEventsData> tempArrayList = new ArrayList<>();
                for (DTEventsData c : list) {
                    if (c.getEventTitle().toLowerCase().contains(cs.toString().toLowerCase())) {
                        tempArrayList.add(c);
                    }
                }
                //Data_array= tempArrayList;
                //DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);

                adapter = new DTEventAdapter(context, tempArrayList);
                rvEvents.setAdapter(adapter);
                adapter.setOnItemSelectedListener(onItemSelectedListener);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

    }
    private DTEventAdapter.OnItemSelectedListener onItemSelectedListener = new DTEventAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int position) {
            DTEventsData data = adapter.getItems().get(position);
            Intent intent = new Intent(context, DTEventDetails.class);

            intent.putExtra("eventImage", data.getEventImg());
            intent.putExtra("eventTitle", data.getEventTitle());
            intent.putExtra("eventDesc", data.getEventDesc());
            intent.putExtra("eventVenue", data.getVenue());
            intent.putExtra("eventDateTime", data.getEventDateTime());

            startActivity(intent);
        }
    };

}
