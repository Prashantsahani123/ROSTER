package com.SampleApp.row;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.DTEventAdapter;
import com.SampleApp.row.Adapter.DTNewslettersAdapter;
import com.SampleApp.row.Data.DTEventsData;
import com.SampleApp.row.Data.DTNewslettersData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * Created by USER on 17-12-2015.
 */
public class DTNewsLetters extends Activity {
    private HashSet<Long> downloadList;

    private DTNewslettersAdapter adapter;
    private ArrayList<DTNewslettersData> list;
    private RecyclerView.LayoutManager mLayoutmanager;
    private RecyclerView rvEvents;


    private TextView tv_title;
    private ImageView iv_backbutton;

    private EditText et_search;
    private String moduleName = "Newsletters";
    private String grpID = "0";

    private Context context;
    FileDownloadReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dt_events);
        context = this;
        receiver = new FileDownloadReceiver();
        downloadList = new HashSet<Long>();
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));



        marshMallowPermission = new MarshMallowPermission(this);
        et_search = (EditText) findViewById(R.id.et_serach);
        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);
        mLayoutmanager = new LinearLayoutManager(context);
        rvEvents.setLayoutManager(mLayoutmanager);
        adapter = new DTNewslettersAdapter(context, list);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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
            Utils.log("URL : "+Constant.DT_GET_NEWSLETTERS+" PARAMETERS : "+jsonParams);

            final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.DT_GET_NEWSLETTERS,
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
            JSONObject result = response.getJSONObject("TBPublicNewsletterList");
            String status = result.getString("status");
            if ( status.equals("0")) {
                String eventsList = result.getJSONArray("Result").toString();
                TypeToken<ArrayList<DTNewslettersData>> typeToken = new TypeToken<ArrayList<DTNewslettersData>>(){};
                list = new Gson().fromJson(eventsList, typeToken.getType());
                adapter = new DTNewslettersAdapter(context, list);
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
                ArrayList<DTNewslettersData> tempArrayList = new ArrayList<>();
                for (DTNewslettersData c : list) {
                    if (c.getEbulletinTitle().toLowerCase().contains(cs.toString().toLowerCase())) {
                        tempArrayList.add(c);
                    }
                }
                //Data_array= tempArrayList;
                //DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);

                adapter = new DTNewslettersAdapter(context, tempArrayList);
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

    private DTNewslettersAdapter.OnItemSelectedListener onItemSelectedListener = new DTNewslettersAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int position) {
            DTNewslettersData item = adapter.getItems().get(position);

            if (item.getEbulletinType().equals("Link")) {
                String url = item.getEbulletinlink();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);

            } else {
                final String link = item.getEbulletinlink();
                Uri u = Uri.parse(link);
                File f = new File("" + u);
                String fileName = f.getName();
                Log.d("-----LINK---------", "-----Downloaded-----" + fileName);
                String servicestring = Context.DOWNLOAD_SERVICE;

                downloadmanager = (DownloadManager) getSystemService(servicestring);
                Uri uri = Uri.parse(link);
                Log.d("-----LINK---------", "-----Downloaded-----" + uri);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                Log.d("-----LINK---------", "-----Downloaded-----" + request);
                //  Long reference = downloadmanager.enqueue(request);
                // Log.d("-----LINK---------","-----Downloaded-----"+reference);

                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    id = downloadmanager.enqueue(request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE).setAllowedOverRoaming(false).setTitle("File Downloading...").setDescription("File Download").setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName));
                    downloadList.add(Long.valueOf(id));
                    Toast.makeText(context, "Newsletter is added to download queue. You will be notified once download completes.", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    MarshMallowPermission marshMallowPermission;

    long id;
    public class FileDownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Utils.log("Download Extras : "+intent.getExtras());
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                Toast.makeText(context, "Newsletter downloaded successfully", Toast.LENGTH_LONG).show();
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if ( downloadList.contains(downloadId) ) {
                    try {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(downloadId);
                        Cursor cursor = downloadmanager.query(query);
                        if (cursor.moveToNext()) {
                            try {
                                String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                Intent openFileIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                startActivity(openFileIntent);
                            } catch(ActivityNotFoundException nae) {
                                Toast.makeText(context, "No application found to open downloaded newsletter", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, R.string.messageSorry, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    DownloadManager downloadmanager;
}
