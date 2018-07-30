package com.SampleApp.row;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.ClassificationAdapter;
import com.SampleApp.row.Adapter.ClassificationRVAdapter;
import com.SampleApp.row.Data.profiledata.ClassificationData;
import com.SampleApp.row.Inteface.OnLoadMoreListener;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.EndlessOnScrollListener;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.ProfileModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Created by USER on 17-12-2015.
 */
public class DTClassificationActivity extends Activity implements OnLoadMoreListener {
    private static final int DELETE_PROFILE_REQUEST = 100;
    private static final boolean NO_ANIMATE = false, ANIMATE = true;
    private static final int ADD_MANUALY_REQUEST = 1;
    private static final int PHONE_BOOK_LIST = 101;

    private MarshMallowPermission permission;
    private DownloadManager downloadManager;
    private ProfileModel profileModel;

    private String firstTime = "no";
    private static final String TAG = "";
    private long downloadId;
    private File downloadDir, tempFile;


    String title;
    Context context;

    EditText et_serach_directory;
    String updatedOn = "";
    TextView tv_title;
    ImageView iv_backbutton;
    private ImageView iv_actionbtn;
    private long masterUid, grpId;
    //ImageView ivSearch;

    ClassificationRVAdapter adapter;

    ClassificationAdapter cAdapter;
    ArrayList<ClassificationData> list =new ArrayList<>();

    private LinearLayout llCenterMessage;
    private TextView tvCenterButton;
    private TextView tvCenterMessage;
    private ProgressBar progressBar;
    ProgressDialog dialog;
    String filter[] = {"Rotarian","Classification"};
    Spinner sp_filter;

    private boolean isLoading;
    int totalPages, currentPage=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dt_classification);

        context = this;
        profileModel = new ProfileModel(context);
        permission = new MarshMallowPermission(this);
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by lekha
        grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
        Utils.log("GroupId : "+grpId);
        initComponents();
        init();
        checkadminrights();
        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeAdapter(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (InternetConnection.checkConnection(context)) {
            if(dialog==null){
                dialog=new ProgressDialog(context,R.style.TBProgressBar);
                dialog.setCancelable(false);
                dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            }
            dialog.show();
            getClassification(currentPage);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }

//        refreshData("");
        // Code to set filter in Directory
        //setFilter();

    }



    public void changeAdapter(AdapterView<?> parent, View view, int position, long id) {
        String filter = sp_filter.getSelectedItem().toString();

        if ( filter.equalsIgnoreCase("Classification") ) {

        } else if ( filter.equalsIgnoreCase("Rotarian")) {
            Intent i = new Intent(context, DTDirectoryActivity.class);
            i.putExtra("moduleName",title);
            context.startActivity(i);
            finish();
        }
    }

    public void loadClassificationAdapter() {

    }
    /*private void setFilter(){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.directory_filter,R.id.textView, filter);
        sp_filter.setAdapter(spinnerArrayAdapter);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void initComponents() {
        //ivSearch = (ImageView) findViewById(R.id.btnSearch);
        sp_filter = (Spinner)findViewById(R.id.sp_filter);
        sp_filter.setSelection(1);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn.setImageDrawable(getResources().getDrawable(R.drawable.search_btn));

        tvCenterMessage = (TextView) findViewById(R.id.tv_no_records_found);
        llCenterMessage = (LinearLayout) findViewById(R.id.llCenterMessage);
        tvCenterButton = (TextView) findViewById(R.id.tvRetry);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        title = getIntent().getExtras().getString("moduleName", "Directory");
        tv_title.setText(title);

        et_serach_directory = (EditText) findViewById(R.id.et_serach_directory);

        rvDirectory = (RecyclerView) findViewById(R.id.rvDirectory);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        rvDirectory.setLayoutManager(linearLayoutManager);

        rvDirectory.setVisibility(View.VISIBLE);

        rvDirectory.addOnScrollListener(new EndlessOnScrollListener(linearLayoutManager) {
            @Override
            public void onScrolledToEnd() {
                if (!isLoading) {
                    isLoading = true;
                    onLoadMore();
                    // add 10 by 10 to tempList then notify changing in data
                }
                isLoading = false;
            }
        });

    }

    /*public void manageCenterMessage(int visibility, String message, int msgVisibility, String btnLabel, int btnVisibility, View.OnClickListener onClickListener) {
        llCenterMessage.setVisibility(visibility);
        tvCenterMessage.setText(message);
        tvCenterMessage.setVisibility(msgVisibility);
        tvCenterButton.setText(btnLabel);
        tvCenterButton.setVisibility(btnVisibility);
        if (onClickListener!=null) {
            tvCenterButton.setOnClickListener(onClickListener);
        }
    }*/

    public void manageCenterMessage(int visibility, final String message, int msgVisibility, String btnLabel, int btnVisibility, View.OnClickListener onClickListener, boolean animateMessage) {
        llCenterMessage.setVisibility(visibility);
        tvCenterMessage.setText(message);
        tvCenterMessage.setVisibility(msgVisibility);

        if ( animateMessage ) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        tvCenterButton.setText(btnLabel);
        tvCenterButton.setVisibility(btnVisibility);
        if (onClickListener!=null) {
            tvCenterButton.setOnClickListener(onClickListener);
        }
    }
    private void checkadminrights() {
        String isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        if ( isAdmin.equals("No")) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadFromDB();
    }

    public void init() {

        et_serach_directory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_serach_directory.selectAll();
                }
            }
        });
        /*ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = et_serach_directory.getText().toString();
                try {
                    hideInput();
                } catch(Exception e) {
                    Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
                    e.printStackTrace();
                }
            }
        });*/


        et_serach_directory.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                String q = et_serach_directory.getText().toString();
//                if ( q.equals("")){
//                    refreshData("");
//                } else {
//                    list = profileModel.searchClassification(grpId, et_serach_directory.getText().toString());
//                    Utils.log("MyList Size : "+list.size());
//                    adapter = new ClassificationRVAdapter(context, list, "0");
//                    rvDirectory.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                    adapter.setOnClassificationSelectedListener(classificationSeletedListener);
//                    rvDirectory.setVisibility(View.VISIBLE);
//                    if ( list.size() == 0 ) {
//                        manageCenterMessage(View.VISIBLE, "No results",  View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
//                    } else {
//                        manageCenterMessage(View.GONE, "",  View.GONE, "", View.GONE, null, NO_ANIMATE);
//                    }
//                }

                if(et_serach_directory.getText().toString().isEmpty()){
                    list.clear();

                    if (InternetConnection.checkConnection(context)) {
                        if(dialog==null){
                            dialog=new ProgressDialog(context,R.style.TBProgressBar);
                            dialog.setCancelable(false);
                            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                        }
                        dialog.show();
                        getClassification(1);
                    } else {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                    }
                }
                
                
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        et_serach_directory.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    list.clear();

                    if (InternetConnection.checkConnection(context)) {
                        if(dialog==null){
                            dialog=new ProgressDialog(context,R.style.TBProgressBar);
                            dialog.setCancelable(false);
                            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                        }
                        dialog.show();
                        getClassification(1);
                    } else {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                    }

                    return true;
                }
                return false;
            }
        });

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdvancedSearchActivity.class);
                intent.putExtra("groupId", ""+grpId);
                startActivity(intent);
            }
        });
//        adapter.setOnClassificationSelectedListener(classificationSeletedListener);

        // Code for filter selection

        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    ClassificationAdapter.OnClassificationSelectedListener classificationSeletedListener = new ClassificationAdapter.OnClassificationSelectedListener() {

        @Override
        public void onClassificationSelected(ClassificationData data, int position) {
            try {
                Intent intent = new Intent(context, ClassificationDirectoryActivity.class);
                intent.putExtra("module","Directory");
                intent.putExtra("classification", data.getClassificationName());
                startActivity(intent);
            } catch(Exception e) {
                Utils.log("Error is : "+e);
                e.printStackTrace();
            }
        }


    };

    public void getClassification(int page){
        Hashtable<String, String> paramTable = new Hashtable<>();

        paramTable.put("grpID", "" + grpId);
        paramTable.put("pageNo",String.valueOf(page));
        paramTable.put("recordCount","");
        paramTable.put("searchText",et_serach_directory.getText().toString());

        JSONObject jsonRequestData = null;
        try {
            jsonRequestData = new JSONObject(new Gson().toJson(paramTable));
            Utils.log("Url : " + Constant.GetClassificationList_new+ " Data : " + jsonRequestData);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.GetClassificationList_new,
                    jsonRequestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                            Log.d("suhas","inside api");
//                            manageCenterMessage(View.GONE, "", View.GONE, "", View.GONE, null, NO_ANIMATE);
                            Utils.log("Success : " + response);
                            //handleSyncInfo(response);
                            if(dialog!=null && dialog.isShowing()){
                                dialog.dismiss();
                            }
                            parseJson(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


//                            if (list.size() == 0) {
//                                manageCenterMessage(
//                                        View.VISIBLE,
//                                        "Something went wrong. Please try again after sometime.",
//                                        View.VISIBLE,
//                                        "Try Now",
//                                        View.VISIBLE,
//                                        new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                llCenterMessage.setVisibility(View.GONE);
//                                                loadFromDB();
//                                            }
//                                        }, NO_ANIMATE);
//                            }
                            if(dialog!=null && dialog.isShowing()){
                                dialog.dismiss();
                            }
                            Utils.log("Error is : " + error);
                            error.printStackTrace();
                        }
                    });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    1200000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            AppController.getInstance().addToRequestQueue(context, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseJson(JSONObject object){
        try {
            JSONObject response=object.getJSONObject("ClassificationListResult");
            String status=response.getString("status");

            if(status.equals("0")){
//                list.remove(list.size()-1);
//                adapter.notifyItemRemoved(list.size());

                totalPages= Integer.parseInt(response.getString("TotalPages"));
                JSONArray result=response.getJSONArray("Result");
                for(int i=0;i<result.length();i++){
                    JSONObject data=result.getJSONObject(i);
                    ClassificationData classificationData=new ClassificationData();
                    classificationData.setClassificationName(data.getString("classification"));
                    list.add(classificationData);
                }

//                DistrictAdapter adapter= (DistrictAdapter)rvDirectory.getAdapter();
                if(cAdapter!=null){

//                    ArrayList<ProfileMasterData> arrayList=adapter.getArrayList();
//                    arrayList.addAll(list);
                    cAdapter.notifyDataSetChanged();
                    isLoading=false;
                }
                else {
                    cAdapter=new ClassificationAdapter(context,list);
                    cAdapter.setonClassificationSelectedListener(classificationSeletedListener);
                    rvDirectory.setAdapter(cAdapter);

                }


            }
            else {
//                list.remove(list.size()-1);
//                adapter.notifyItemRemoved(list.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void finishActivity(View v) {
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TOUCHBASE", "REQUEST CODE" + resultCode);
        // check if the request code is same as what is passed  here it is 1
        if (requestCode == ADD_MANUALY_REQUEST) {
            if (InternetConnection.checkConnection(getApplicationContext())) {
                //getProfileSyncInfo();
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
            // webservices();
        } else if (requestCode == PHONE_BOOK_LIST && resultCode ==RESULT_OK) {
            if (data != null) {
                //getProfileSyncInfo();
            }
        } else if ( resultCode == RESULT_OK && requestCode == DELETE_PROFILE_REQUEST ) {
            //getProfileSyncInfo();
        }
    }

    private void copyTextToClipBoard(String chatMessage) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(chatMessage);
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Link", chatMessage);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_LONG).show();
    }

    //-----------------------All new declarations -----------
    RecyclerView rvDirectory;

    public void refreshData(String msg) {
//        list = profileModel.getClassifications(grpId);
//        if ( list.size() > 0 ) {
//            Utils.log("MyList Size : " + list.size());
//            adapter = new ClassificationRVAdapter(context, list, "0");
//            rvDirectory.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//            adapter.setOnClassificationSelectedListener(classificationSeletedListener);
//            rvDirectory.setVisibility(View.VISIBLE);
//            Utils.log("Loaded from local db");
//            manageCenterMessage(View.GONE, "", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
//        } else {
//            manageCenterMessage(View.VISIBLE, "No new updates", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
//        }
    }

    @Override
    public void onLoadMore() {
        if(currentPage<totalPages){
            currentPage=currentPage+1;

            if (InternetConnection.checkConnection(context)) {
                getClassification(currentPage);
            } else {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            }

        }
    }
}