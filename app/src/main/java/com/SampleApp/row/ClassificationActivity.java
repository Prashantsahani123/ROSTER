package com.SampleApp.row;

import android.app.Activity;
import android.app.DownloadManager;
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

import com.SampleApp.row.Adapter.ClassificationRVAdapter;
import com.SampleApp.row.Data.profiledata.ClassificationData;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.ProfileModel;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by USER on 17-12-2015.
 */
public class ClassificationActivity extends Activity {
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
    ArrayList<ClassificationData> list;

    private LinearLayout llCenterMessage;
    private TextView tvCenterButton;
    private TextView tvCenterMessage;
    private ProgressBar progressBar;

    String filter[] = {"Rotarian","Classification", "Family"};
    Spinner sp_filter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

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

        refreshData("");
        // Code to set filter in Directory
        //setFilter();

    }



    public void changeAdapter(AdapterView<?> parent, View view, int position, long id) {
        String filter = sp_filter.getSelectedItem().toString();

        if ( filter.equalsIgnoreCase("Classification") ) {

        } else if ( filter.equalsIgnoreCase("Rotarian")) {
            Intent i = new Intent(context, DirectoryActivity.class);
            i.putExtra("moduleName",title);
            context.startActivity(i);
            finish();
        } else if ( filter.equalsIgnoreCase("Family")) {
            Intent intent = new Intent(context, FamilywiseDirectoryActivity.class);
            intent.putExtra("moduleName", "Directory");
            startActivity(intent);
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

        list = new ArrayList<>();
        adapter = new ClassificationRVAdapter(context, list, "0");
        rvDirectory.setLayoutManager(new LinearLayoutManager(context));
        rvDirectory.setAdapter(adapter);
        rvDirectory.setVisibility(View.VISIBLE);

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
                String q = et_serach_directory.getText().toString();
                if ( q.equals("")){
                    refreshData("");
                } else {
                    list = profileModel.searchClassification(grpId, et_serach_directory.getText().toString());
                    Utils.log("MyList Size : "+list.size());
                    adapter = new ClassificationRVAdapter(context, list, "0");
                    rvDirectory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setOnClassificationSelectedListener(classificationSeletedListener);
                    rvDirectory.setVisibility(View.VISIBLE);
                    if ( list.size() == 0 ) {
                        manageCenterMessage(View.VISIBLE, "No results",  View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
                    } else {
                        manageCenterMessage(View.GONE, "",  View.GONE, "", View.GONE, null, NO_ANIMATE);
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
        adapter.setOnClassificationSelectedListener(classificationSeletedListener);

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

    ClassificationRVAdapter.OnClassificationSelectedListener classificationSeletedListener = new ClassificationRVAdapter.OnClassificationSelectedListener() {

        @Override
        public void onClassificationSelected(ClassificationData data, int position) {
            try {
                Intent intent = new Intent(context, ClassificationDirectoryActivity.class);
                intent.putExtra("classification", data.getClassificationName());
                startActivity(intent);
            } catch(Exception e) {
                Utils.log("Error is : "+e);
                e.printStackTrace();
            }
        }


    };

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
        list = profileModel.getClassifications(grpId);
        if ( list.size() > 0 ) {
            Utils.log("MyList Size : " + list.size());
            adapter = new ClassificationRVAdapter(context, list, "0");
            rvDirectory.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnClassificationSelectedListener(classificationSeletedListener);
            rvDirectory.setVisibility(View.VISIBLE);
            Utils.log("Loaded from local db");
            manageCenterMessage(View.GONE, "", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
        } else {
            manageCenterMessage(View.VISIBLE, "No new updates", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
        }
    }
}