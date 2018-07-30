package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.ClassificationRVAdapter;
import com.SampleApp.row.Adapter.DirectoryRVAdapter;
import com.SampleApp.row.Data.profiledata.AddressData;
import com.SampleApp.row.Data.profiledata.BusinessMemberDetails;
import com.SampleApp.row.Data.profiledata.CompleteProfile;
import com.SampleApp.row.Data.profiledata.FamilyMemberData;
import com.SampleApp.row.Data.profiledata.PersonalMemberDetails;
import com.SampleApp.row.Data.profiledata.ProfileMasterData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.TBPrefixes;
import com.SampleApp.row.Utils.UnzipUtility;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.services.DirectorySyncService;
import com.SampleApp.row.services.DirectoryUpdateService;
import com.SampleApp.row.sql.ProfileModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;


/**
 * Created by USER on 17-12-2015.
 */
public class DirectoryActivity extends Activity {
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
    private DownloadCompleteReceiver receiver;
    private UpdateStatusReceiver updateStatusReceiver;


    FloatingActionButton fab;
    String zipUrl, downloadPath;
    Context context;
    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton addManually, invite, addFromPhonebook;
    EditText et_serach_directory;
    String updatedOn = "1970/01/01 00:00:00";
    TextView tv_title;
    ImageView iv_backbutton;
    private ImageView iv_actionbtn;
    private long masterUid, grpId;
    //ImageView ivSearch;

    DirectoryRVAdapter adapter;
    ArrayList<ProfileMasterData> list;

    private LinearLayout llCenterMessage;
    private TextView tvCenterButton;
    private TextView tvCenterMessage;
    private ProgressBar progressBar;

    String filter[] = {"Rotarian", "Classification", "Family"};
    Spinner sp_filter;


    ClassificationRVAdapter classificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        context = this;
        receiver = new DownloadCompleteReceiver();
        updateStatusReceiver = new UpdateStatusReceiver();
        profileModel = new ProfileModel(context);
        permission = new MarshMallowPermission(this);
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by lekha
        grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
        Utils.log("GroupId : " + grpId);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(updateStatusReceiver, new IntentFilter(DirectoryUpdateService.ACTION_DIRECTORY_SYNC));
        initComponents();
        init();
        checkadminrights();
        loadFromDB();
        loadDynamicFields();
        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeAdapter(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Code to set filter in Directory
        //setFilter();

    }

    public void changeAdapter(AdapterView<?> parent, View view, int position, long id) {
        String filter = sp_filter.getSelectedItem().toString();

        if (filter.equalsIgnoreCase("Classification")) {
            Intent intent = new Intent(context, ClassificationActivity.class);
            intent.putExtra("moduleName", "Directory");

            startActivity(intent);
            finish();
        } else if (filter.equalsIgnoreCase("Rotarian")) {

        } else if (filter.equalsIgnoreCase("Family")) {
            Intent intent = new Intent(context, FamilywiseDirectoryActivity.class);
            intent.putExtra("moduleName", "Directory");
            startActivity(intent);
            finish();
        }
    }



    /*private void setFilter(){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.directory_filter,R.id.textView, filter);
        sp_filter.setAdapter(spinnerArrayAdapter);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(updateStatusReceiver);

    }


    public void initComponents() {
        //ivSearch = (ImageView) findViewById(R.id.btnSearch);

        sp_filter = (Spinner) findViewById(R.id.sp_filter);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn.setImageDrawable(getResources().getDrawable(R.drawable.search_btn));

        tvCenterMessage = (TextView) findViewById(R.id.tv_no_records_found);
        llCenterMessage = (LinearLayout) findViewById(R.id.llCenterMessage);
        tvCenterButton = (TextView) findViewById(R.id.tvRetry);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        String title = getIntent().getExtras().getString("moduleName", "Directory");
        tv_title.setText(title);

        et_serach_directory = (EditText) findViewById(R.id.et_serach_directory);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        addManually = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        invite = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        addFromPhonebook = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

        rvDirectory = (RecyclerView) findViewById(R.id.rvDirectory);

        list = new ArrayList<>();
        adapter = new DirectoryRVAdapter(context, list, "0");
        rvDirectory.setLayoutManager(new LinearLayoutManager(context));
        rvDirectory.setAdapter(adapter);
        rvDirectory.setVisibility(View.VISIBLE);
        if ( list.size() != 0) {
            Toast.makeText(context, "Total members " + list.size(), Toast.LENGTH_LONG).show();
        }
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

        if (animateMessage) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        tvCenterButton.setText(btnLabel);
        tvCenterButton.setVisibility(btnVisibility);
        if (onClickListener != null) {
            tvCenterButton.setOnClickListener(onClickListener);
        }
    }

    private void checkadminrights() {
        String isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        if (isAdmin.equals("No")) {
            materialDesignFAM.setVisibility(View.GONE);
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
        addManually.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    Intent i = new Intent(DirectoryActivity.this, AddMemberToGroup.class);
                    startActivityForResult(i, ADD_MANUALY_REQUEST);
                    materialDesignFAM.toggle(false);
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }

            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DirectoryActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_invite);


                final TextView tv_link = (TextView) dialog.findViewById(R.id.tv_link);
                TextView tv_share = (TextView) dialog.findViewById(R.id.tv_share);
                TextView tv_clipboard = (TextView) dialog.findViewById(R.id.tv_clipboard);
                ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);

                //  tv_link.setText(Constant.INVITE_BASE_URL+grpId);

                tv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //String shareBody = "Hi Lets get connected through TouchBase app for Real time, Focused and Spam free communication. Download it now at\n\n" + Constant.INVITE_BASE_URL + grpId;
                        String shareBody = "You have been invited to join Roster on wheels (ROW) by adminstrator of your Rotary club.\n" +
                                "\n" +
                                "Please click on the link below.\n\n"+
                                Constant.INVITE_BASE_URL + grpId;

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation to join Roster On Wheels (ROW)");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        //  dialog.dismiss();


                    }
                });
                tv_link.setText(Html.fromHtml(Constant.INVITE_BASE_URL + grpId));
                tv_link.setMovementMethod(LinkMovementMethod.getInstance());

                tv_clipboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        copyTextToClipBoard(tv_link.getText().toString());
                    }
                });
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                materialDesignFAM.toggle(false);
            }
        });
        addFromPhonebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    Intent phone_book_intent = new Intent(DirectoryActivity.this, Contact_Import.class);
                    startActivityForResult(phone_book_intent, PHONE_BOOK_LIST);
                    materialDesignFAM.toggle(false);
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phone_book_intent = new Intent(DirectoryActivity.this, Contact_Import.class);
                startActivityForResult(phone_book_intent, PHONE_BOOK_LIST);

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
                if (q.equals("")) {
                    refreshData("");
                } else {
                    list = profileModel.search(grpId, et_serach_directory.getText().toString());
                    Utils.log("MyList Size : " + list.size());
                    adapter = new DirectoryRVAdapter(context, list, "0");
                    rvDirectory.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setOnMemberSelectedListener(memberSelectedListener);
                    rvDirectory.setVisibility(View.VISIBLE);
                    if (list.size() == 0) {
                        manageCenterMessage(View.VISIBLE, "No results", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
                    } else {
                        manageCenterMessage(View.GONE, "", View.GONE, "", View.GONE, null, NO_ANIMATE);
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
                intent.putExtra("groupId", "" + grpId);
                startActivity(intent);
            }
        });
        adapter.setOnMemberSelectedListener(memberSelectedListener);

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

    DirectoryRVAdapter.OnMemberSelectedListener memberSelectedListener = new DirectoryRVAdapter.OnMemberSelectedListener() {
        @Override
        public void onMemberSelected(ProfileMasterData data, int position) {
            try {
                Intent intent = new Intent(context, NewProfileActivity.class);
                intent.putExtra("memberProfileId", data.getProfileId());
                intent.putExtra("groupId", data.getGrpId());
                intent.putExtra("fromMainDirectory", "yes");
                startActivityForResult(intent, DELETE_PROFILE_REQUEST);
            } catch (Exception e) {
                Utils.log("Error is : " + e);
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
                getProfileSyncInfo();
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
            // webservices();
        } else if (requestCode == PHONE_BOOK_LIST && resultCode == RESULT_OK) {
            if (data != null) {
                getProfileSyncInfo();
            }
        } else if (resultCode == RESULT_OK && requestCode == DELETE_PROFILE_REQUEST) {
            getProfileSyncInfo();
        }
    }

    //-----------------------Offline Database ----------------------
    public void loadFromDB() {
        Log.d("Touchbase", "Trying to load from local db");
        boolean isDataAvailable = profileModel.isDataAvailable(grpId);

        Log.e("DataAvailable", "Data available : " + isDataAvailable);
        if (!isDataAvailable) {
            Log.d("Touchbase", "Loading from server");
            firstTime = "yes";
            if (InternetConnection.checkConnection(getApplicationContext())) {
                getProfileSyncInfo();
            } else {
                rvDirectory.setVisibility(View.GONE);
                //llCenterMessage.setVisibility(View.VISIBLE);
                manageCenterMessage(
                        View.VISIBLE,
                        "No internet connection. Please retry",
                        View.VISIBLE,
                        "Retry",
                        View.VISIBLE,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //llCenterMessage.setVisibility(View.INVISIBLE);
                                llCenterMessage.setVisibility(View.GONE);
                                new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        loadFromDB();
                                    }
                                }.sendEmptyMessageDelayed(0, 500);

                            }
                        }, NO_ANIMATE);

            }
        } else {
            firstTime = "no";
            Log.d("Touchbase", "Loaded from local db");
            manageCenterMessage(View.GONE, "", View.GONE, "", View.GONE, null, NO_ANIMATE);
            refreshData("");

            getProfileSyncInfo();

            Log.d("---------------", "Check for update gets called------");
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

    public void hideInput() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    //-----------------------All new declarations -----------
    RecyclerView rvDirectory;

    ArrayList<ProfileMasterData> profileList = new ArrayList<>();


    public void getProfileSyncInfo() {
        //final ProgressDialog pd = new ProgressDialog(context);
        //pd.setCancelable(false);

        String isRequested=PreferenceManager.getPreference(DirectoryActivity.this,PreferenceManager.REQUESTED+grpId,"N");
        if(isRequested.equalsIgnoreCase("N")){
            if (firstTime.equalsIgnoreCase("yes")) {
                //pd.setMessage();
                manageCenterMessage(
                        View.VISIBLE,
                        "Downloading Directory from cloud \n" +
                                "Please wait it may take some time... ",
                        View.VISIBLE,
                        "",
                        View.GONE,
                        null, ANIMATE);

                Intent service=new Intent(context, DirectorySyncService.class);
                startService(service);
            } else {
                //pd.setMessage("Loading please wait");
                manageCenterMessage(
                        View.GONE,
                        "Checking for updates. Please wait",
                        View.VISIBLE,
                        "",
                        View.GONE,
                        null, ANIMATE);


                Utils.log("Started getProfileSyncInfo");
                Hashtable<String, String> paramTable = new Hashtable<>();
                updatedOn = PreferenceManager.getPreference(this, TBPrefixes.DIRECTORY_PREFIX + grpId, "1970/01/01 00:00:00");

                paramTable.put("updatedOn", updatedOn);
                paramTable.put("grpID", "" + grpId);

                JSONObject jsonRequestData = null;
                try {
                    jsonRequestData = new JSONObject(new Gson().toJson(paramTable));
                    Utils.log("Url : " + Constant.GetMemberListSync + " Data : " + jsonRequestData);
                    PreferenceManager.savePreference(DirectoryActivity.this, PreferenceManager.REQUESTED + grpId, "Y");
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                            Constant.GetMemberListSync,
                            jsonRequestData,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    manageCenterMessage(View.GONE, "", View.GONE, "", View.GONE, null, NO_ANIMATE);
                                    Utils.log("Success : " + response);
                                    handleSyncInfo(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (list.size() == 0) {
                                        manageCenterMessage(
                                                View.VISIBLE,
                                                "Something went wrong. Please try again after sometime.",
                                                View.VISIBLE,
                                                "Try Now",
                                                View.VISIBLE,
                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        llCenterMessage.setVisibility(View.GONE);
                                                        loadFromDB();
                                                    }
                                                }, NO_ANIMATE);
                                    }
                                    Utils.log("Error is : " + error);
                                    error.printStackTrace();
                                    PreferenceManager.savePreference(DirectoryActivity.this, PreferenceManager.REQUESTED + grpId, "N");
                                }
                            });
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));

                    AppController.getInstance().addToRequestQueue(context, request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            if(firstTime.equalsIgnoreCase("yes")){
                manageCenterMessage(
                        View.VISIBLE,
                        "Downloading Directory from cloud \n" +
                                "Please wait it may take some time... ",
                        View.VISIBLE,
                        "",
                        View.GONE,
                        null, ANIMATE);
                moduleDataHandler.sendEmptyMessageDelayed(0, 2000);

            }



        }


    }

    Handler moduleDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String isRequested=PreferenceManager.getPreference(DirectoryActivity.this,PreferenceManager.REQUESTED+grpId,"N");
            if(isRequested.equalsIgnoreCase("N")){
                boolean isDataAvailable = profileModel.isDataAvailable(grpId);
                if (!isDataAvailable) {
                    manageCenterMessage(
                            View.VISIBLE,
                            "Something went wrong. Please try again after sometime.",
                            View.VISIBLE,
                            "Try Now",
                            View.VISIBLE,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    llCenterMessage.setVisibility(View.GONE);
                                    loadFromDB();
                                }
                            }, NO_ANIMATE);
                    //Log.d("Touchbase---@@@@@@@@", "Failed to save offlline. Retrying in 2 seconds");

                } else {
//                String tempUpdate=PreferenceManager.getPreference(context,TBPrefixes.TEMP_UPDATED_ON + grpId,"");
//                PreferenceManager.savePreference(DirectoryActivity.this,TBPrefixes.DIRECTORY_PREFIX + grpId,tempUpdate);
                    //PreferenceManager.savePreference(DirectoryActivity.this,TBPrefixes.DIRECTORY_PREFIX + grpId,Utils.serUpdateOn);
                    manageCenterMessage(View.GONE, "", View.GONE, "", View.GONE, null, NO_ANIMATE);
                    refreshData("");
                }
            }else {
                sendEmptyMessageDelayed(0, 2000);
            }


        }
    };

    public void handleSyncInfo(JSONObject response) {
        try {
            String status = response.getString("status");
            if (status.equals("0")) {
                Utils.log("Response is : " + response);
                updatedOn = response.getString("curDate");
                zipUrl = response.getString("zipFilePath");
                Utils.log("Zip File Path : " + zipUrl);
                PreferenceManager.savePreference(context, TBPrefixes.TEMP_UPDATED_ON + grpId, updatedOn);
                if (zipUrl.trim().equals("")) {
                    processDirectRecords(response);
                } else { // means zip file is available for download
//                    if (permission.checkPermissionForExternalStorage()) {
//                        startDownload();
//                    } else {
//                        permission.requestPermissionForExternalStorage();
//                    }
                    startDownload();
                }
            }else {
                updatedOn = response.getString("curDate");
                PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
            }
        } catch (JSONException je) {
            Utils.log("Error is : " + je);
            je.printStackTrace();
            PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
            manageCenterMessage(
                    View.VISIBLE,
                    "Something went wrong. Please try again after sometime.",
                    View.VISIBLE,
                    "Try Now",
                    View.VISIBLE,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            llCenterMessage.setVisibility(View.GONE);
                            loadFromDB();
                        }
                    }, NO_ANIMATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MarshMallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (permission.checkPermissionForExternalStorage()) {
                startDownload();
            } else {
                Toast.makeText(context, "Operation cannot be completed because of permission.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void startDownload() {
        Utils.log("Starting downloading file");
        // File sdCard = Environment.getExternalStorageDirectory();
        File sdCard=getExternalFilesDir(null);
        downloadDir = new File(sdCard, "Touchbase/temp");
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        tempFile = new File(downloadDir, "directory_" + new Date().getTime() + ".zip");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(zipUrl));
        request.setVisibleInDownloadsUi(false);
        request.setDestinationUri(Uri.fromFile(tempFile));
        downloadId = downloadManager.enqueue(request);
        Utils.log("Files is added to the download queue");
        manageCenterMessage(
                View.VISIBLE,
                "Processing and storing offline",
                View.VISIBLE,
                "",
                View.GONE,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llCenterMessage.setVisibility(View.GONE);
                        loadFromDB();
                    }
                }, ANIMATE);
        //loadDynamicFieldsFromServer();
    }

    public class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String actionString = intent.getAction();
            if (actionString.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                try {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cur = downloadManager.query(query);

                    if (cur.moveToNext()) {
                        int status = cur.getInt(cur.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            UnzipUtility uu = new UnzipUtility();
                            try {
                                File unzipDir = new File(downloadDir.getPath() + File.separator + tempFile.getName().replaceAll(".zip", ""));
                                unzipDir.mkdir();
                                uu.unzip(tempFile.getPath(), unzipDir.getPath());
                                tempFile.delete();  // delete zip file after extracting
                                PreferenceManager.savePreference(context, Constant.UPDATE_FILE_NAME, unzipDir.getPath());
                                Intent serviceIntent = new Intent(context, DirectoryUpdateService.class);
                                serviceIntent.putExtra("directoryPath", unzipDir.getPath());
                                context.startService(serviceIntent);
                                Utils.log("Download completed and services is processing the updates");
                                deleteServerFile();
                            } catch (IOException e) {
                                Utils.log("Error is : " + e);
                                e.printStackTrace();
                                showRetryDialog();
                                PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
                            }
                        } else {
                            Utils.log("Failed to download update file");
                            manageCenterMessage(
                                    View.VISIBLE,
                                    "Failed to download update file",
                                    View.VISIBLE,
                                    "Try Now",
                                    View.VISIBLE,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            llCenterMessage.setVisibility(View.GONE);
                                            loadFromDB();
                                        }
                                    }, NO_ANIMATE);
                            PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
                        }
                    }
                } catch (NullPointerException npe) {
                    Utils.log("Error is : " + npe);
                    npe.printStackTrace();
                    manageCenterMessage(
                            View.VISIBLE,
                            "Failed to download update file",
                            View.VISIBLE,
                            "Try Now",
                            View.VISIBLE,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    llCenterMessage.setVisibility(View.GONE);
                                    loadFromDB();
                                }
                            }, NO_ANIMATE);
                    PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
                } catch (Exception e) {
                    Utils.log("Error is : " + e);
                    e.printStackTrace();
                    manageCenterMessage(
                            View.VISIBLE,
                            "Failed to download update file",
                            View.VISIBLE,
                            "Try Now",
                            View.VISIBLE,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    llCenterMessage.setVisibility(View.GONE);
                                    loadFromDB();
                                }
                            }, NO_ANIMATE);
                    PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
                }
            }
        }
    }

    public void showRetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.popup_retry, null);
        builder.setView(view);

        final Dialog retryDialog = builder.create();

        TextView tvYes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tvNo = (TextView) view.findViewById(R.id.tv_no);

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryDialog.hide();
            }
        });

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfileSyncInfo();
                retryDialog.hide();
            }
        });

        retryDialog.show();
    }

    public void deleteServerFile() {
        try {
            String zipFileDelete = new URL(zipUrl).getFile();
            Hashtable<String, String> params = new Hashtable<>();
            params.put("folderPath", zipFileDelete);
            JSONObject jsonRequest = new JSONObject(new Gson().toJson(params));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.DELETE_ZIP_FILE, jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.log(response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            AppController.getInstance().addToRequestQueue(context, request);
        } catch (MalformedURLException mfe) {
            Utils.log("Error is : " + mfe);
            mfe.printStackTrace();
        } catch (JSONException jse) {
            Utils.log("Error is : " + jse);
            jse.printStackTrace();
        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

    }

    public class UpdateStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Utils.log("Inside UpdateStatusReceiver");
            String action = intent.getAction();
            Utils.log("Value of action is : " + action);
            if (action.equals(DirectoryUpdateService.ACTION_DIRECTORY_SYNC)) {
                String message = intent.getExtras().getString("ExtraMessage");
                Utils.log("Value of message : " + message);
                if (message.equals(DirectoryUpdateService.ACTION_DIRECTORY_SYNC_COMPLETED)) {
                    Utils.log("Processing is completed and fetching data from local data");
                    Utils.log("first directory");
                    PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                    refreshData("");
                }
            }
        }
    }

    public void refreshData(String msg) {
        PreferenceManager.savePreference(DirectoryActivity.this,PreferenceManager.REQUESTED+grpId,"N");
        list = profileModel.getMembers(grpId);
        if (list.size() > 0) {
            Utils.log("MyList Size : " + list.size());
            adapter = new DirectoryRVAdapter(context, list, "0");
            rvDirectory.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnMemberSelectedListener(memberSelectedListener);
            rvDirectory.setVisibility(View.VISIBLE);
            Utils.log("Loaded from local db");
            manageCenterMessage(View.GONE, "", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
            Toast.makeText(context, "Total members " + list.size(), Toast.LENGTH_LONG).show();

        } else {
            manageCenterMessage(View.VISIBLE, "No new updates", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
        }
    }


    //Processing records when records are not provided in zip file

    public void processDirectRecords(JSONObject response) {
        try {
            final ProfileModel model = new ProfileModel(getBaseContext());
            //final ProgressDialog pd = new ProgressDialog(context);
            //pd.setMessage("Processing contacts & Storing offline");
            manageCenterMessage(
                    View.VISIBLE,
                    "Processing contacts & Storing offline",
                    View.VISIBLE,
                    "",
                    View.GONE,
                    null, ANIMATE);
            Utils.log("Is very first time : " + firstTime);
            if (firstTime.equals("yes")) {  // Means process only new records.
                final JSONArray newRecords = response.getJSONObject("MemberDetail").getJSONArray("NewMemberList");
                Utils.log("Found records : " + newRecords.length());


                final ArrayList<CompleteProfile> newRecordsList = processRecords(newRecords);
                final int numberOfRecords = newRecordsList.size();

                Utils.log("Parsed number of records : " + newRecordsList.size());

                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        boolean success = model.addProfiles(newRecordsList);

                        if (!success) {
                            Utils.log("Failed to insert new records in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            Utils.log("two directory");
                            PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                            PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
                            //pd.hide();
                            manageCenterMessage(
                                    View.GONE,
                                    "",
                                    View.VISIBLE,
                                    "",
                                    View.VISIBLE,
                                    null, NO_ANIMATE);
                            Toast.makeText(context, numberOfRecords + " contacts stored", Toast.LENGTH_LONG).show();
                            refreshData("");
                        }
                    }
                };
                handler.sendEmptyMessage(0);
                //pd.show();
            } else {  // means process all new, updated and deleted records
                JSONArray newRecords = response.getJSONObject("MemberDetail").getJSONArray("NewMemberList");
                JSONArray updatedRecords = response.getJSONObject("MemberDetail").getJSONArray("UpdatedMemberList");
                final String deletedRecords = response.getJSONObject("MemberDetail").getString("DeletedMemberList");
                final ArrayList<CompleteProfile> newRecordsList = processRecords(newRecords);
                final ArrayList<CompleteProfile> updatedRecordsList = processRecords(updatedRecords);

                final int numberOfRecordsProcessed;


                if (!deletedRecords.equals("")) {
                    numberOfRecordsProcessed = newRecordsList.size() + updatedRecordsList.size() + deletedRecords.split(",").length;
                } else {
                    numberOfRecordsProcessed = newRecordsList.size() + updatedRecordsList.size();
                }
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        boolean success = model.updateProfiles(newRecordsList, updatedRecordsList, deletedRecords);
                        if (!success) {
                            Utils.log("Failed to update updated records in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            Utils.log("three directory");
                            PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                            PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
                            //if ( numberOfRecordsProcessed != 0 ) {
                            Toast.makeText(context, numberOfRecordsProcessed + " contacts processed", Toast.LENGTH_SHORT).show();
                            //}
                            refreshData("");
                        }
                    }
                };
                handler.sendEmptyMessage(0);

                if(numberOfRecordsProcessed==0){
                    PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");

                }
            }


        } catch (JSONException e) {
            PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
            e.printStackTrace();
        }

        //loadDynamicFields();
    }


    public ArrayList<CompleteProfile> processRecords(JSONArray memberProfiles) {
        ArrayList<CompleteProfile> profileList = new ArrayList<>();
        try {
            int n = memberProfiles.length();

            for (int i = 0; i < n; i++) {
                JSONObject profile = memberProfiles.getJSONObject(i);
                Utils.log(profile.toString());
                // start of ProfileMasterData
                String masterId = profile.getString("masterID");
                String grpId = profile.getString("grpID");
                String profileId = profile.getString("profileID");
                String isAdmin = profile.getString("isAdmin");
                String memberName = profile.getString("memberName");
                String memberEmail = profile.getString("memberEmail");
                String memberMobile = profile.getString("memberMobile");
                String memberCountry = profile.getString("memberCountry");
                String profilePic = profile.getString("profilePic");
                String familyPic = profile.getString("familyPic");
                String isPersonalDetVisible = profile.getString("isPersonalDetVisible");
                String isBussinessDetVisible = profile.getString("isBusinDetVisible");
                String isFamilyDetVisible = profile.getString("isFamilDetailVisible");
                String isResidanceAddrVisible = profile.getJSONObject("addressDetails").getString("isResidanceAddrVisible");
                String isBusinessAddrVisible = profile.getJSONObject("addressDetails").getString("isBusinessAddrVisible");
                ProfileMasterData profileData = new ProfileMasterData(
                        masterId,
                        grpId,
                        profileId,
                        isAdmin,
                        memberName,
                        memberEmail,
                        memberMobile,
                        memberCountry,
                        profilePic,
                        familyPic,
                        isPersonalDetVisible,
                        isBussinessDetVisible,
                        isFamilyDetVisible,
                        isResidanceAddrVisible,
                        isBusinessAddrVisible);
                // end of ProfileMasterData

                // start of PersonalMemberDetails
                ArrayList<PersonalMemberDetails> personalDetailsList = new ArrayList<>();
                try {
                    JSONArray personalMemberDetails = profile.getJSONArray("personalMemberDetails");
                    int m = personalMemberDetails.length();

                    for (int j = 0; j < m; j++) {
                        JSONObject personalDetail = personalMemberDetails.getJSONObject(j);
                        String fieldID = personalDetail.getString("fieldID");
                        String uniquekey = personalDetail.getString("uniquekey");
                        String key = personalDetail.getString("key");
                        String value = personalDetail.getString("value");
                        String colType = personalDetail.getString("colType");
                        String isEditable = personalDetail.getString("isEditable");
                        String isVisible = personalDetail.getString("isVisible");

                        PersonalMemberDetails data = new PersonalMemberDetails(profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                        personalDetailsList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // end of PersonalMemberDetails

                // start of BusinessMemberDetails

                ArrayList<BusinessMemberDetails> businessDetailsList = new ArrayList<>();
                try {
                    JSONArray businessMemberDetails = profile.getJSONArray("businessMemberDetails");
                    int m = businessMemberDetails.length();
                    for (int j = 0; j < m; j++) {

                        JSONObject personalDetail = businessMemberDetails.getJSONObject(j);
                        String fieldID = personalDetail.getString("fieldID");
                        String uniquekey = personalDetail.getString("uniquekey");
                        String key = personalDetail.getString("key");
                        String value = personalDetail.getString("value");
                        String colType = personalDetail.getString("colType");
                        String isEditable = personalDetail.getString("isEditable");
                        String isVisible = personalDetail.getString("isVisible");

                        BusinessMemberDetails data = new BusinessMemberDetails(profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                        businessDetailsList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // end of BusinessMemberDetails

                // Start of family MemberDetails

                ArrayList<FamilyMemberData> familyMemberList = new ArrayList<>();
                try {
                    JSONArray familyMemberDetails = profile.getJSONObject("familyMemberDetails").getJSONArray("familyMemberDetail");

                    int m = familyMemberDetails.length();

                    for (int j = 0; j < m; j++) {
                        JSONObject familyMember = familyMemberDetails.getJSONObject(j);
                        String familyMemberId = familyMember.getString("familyMemberId");
                        String familyMemberName = familyMember.getString("memberName");
                        String relationship = familyMember.getString("relationship");
                        String dob = familyMember.getString("dOB");
                        String emailID = familyMember.getString("emailID");
                        String anniversary = familyMember.getString("anniversary");
                        String contactNo = familyMember.getString("contactNo");
                        String[] contactFields = contactNo.split(" "); // contact number is in the form "countryId contactNumber" e.g. 1 8877665544 here 1 is country code and 8877665544 is mobile number
                        String countryId = "0";

                        try {
                            countryId = contactFields[0];
                            String countryCode = Utils.getCountryCode(context, countryId);
                            contactNo = countryCode + " " +contactFields[1];
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        String particulars = familyMember.getString("particulars");
                        String bloodGroup = familyMember.getString("bloodGroup");
                        FamilyMemberData data = new FamilyMemberData(profileId, familyMemberId, familyMemberName, relationship, dob, emailID, anniversary, contactNo, particulars, bloodGroup, countryId);
                        familyMemberList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // End of family member details

                // Start of address details
                ArrayList<AddressData> addressList = new ArrayList<>();
                try {
                    JSONArray addressDetails = profile.getJSONObject("addressDetails").getJSONArray("addressResult");
                    int m = addressDetails.length();

                    for (int j = 0; j < m; j++) {
                        JSONObject address = addressDetails.getJSONObject(j);
                        String addressID = address.getString("addressID");
                        String addressType = address.getString("addressType");
                        String addressText = address.getString("address");
                        String city = address.getString("city");
                        String state = address.getString("state");
                        String country = address.getString("country");
                        String pincode = address.getString("pincode");
                        String phoneNo = address.getString("phoneNo");
                        String fax = address.getString("fax");

                        AddressData data = new AddressData(profileId, addressID, addressType, addressText, city, state, country, pincode, phoneNo, fax);
                        addressList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // end of address details
                CompleteProfile cp = new CompleteProfile(profileData, personalDetailsList, businessDetailsList, familyMemberList, addressList);
                profileList.add(cp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileList;
    }

    /*Loading dynamic search fields*/
    public void loadDynamicFields() {
        try {
            FileInputStream fin = openFileInput(grpId + "_" + DYNAMIC_FIELDS_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while (fin.available() != 0) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();
            isFieldPresent = true;
            handleLocalResult(new JSONObject(fieldData));
            Utils.log("Loaded from local file");
            loadDynamicFieldsFromServer();
        } catch (FileNotFoundException fne) {
            Utils.log("Dynamic fields are not present in local file");
            loadDynamicFieldsFromServer();
        } catch (IOException fne) {
            Utils.log("Dynamic fields are not present in local file");
            loadDynamicFieldsFromServer();
        } catch (JSONException jse) {
            Utils.log("Error is : " + jse);
            jse.printStackTrace();
        }
    }

    public void loadDynamicFieldsFromServer() {
        try {
            //final ProgressDialog progressDialog = new ProgressDialog(context);

            //progressDialog.setMessage("Please wait. Loading search fields");

            Hashtable<String, String> params = new Hashtable<>();
            params.put("groupId", "" + grpId);
            JSONObject requestParams = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GET_ADVANCED_SEARCH_FIELDS,
                    requestParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            handleSuccessResult(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Utils.log("Error is : " + error);
                            error.printStackTrace();
                        }
                    });
            AppController.getInstance().addToRequestQueue(context, request);

        } catch (JSONException jse) {
            Utils.log("Error is : " + jse);
        }
    }

    public void handleSuccessResult(JSONObject response) {
        try {
            Utils.log("Response : " + response);
            JSONObject filtersData = response.getJSONObject("SearchFilterResult");
            String status = filtersData.getString("status");
            String message = filtersData.getString("message");
            if (status.equals("0")) {
                JSONArray fields = filtersData.getJSONArray("GroupFilters");
                try {
                    FileOutputStream fout = openFileOutput(grpId + "_" + DYNAMIC_FIELDS_FILE, MODE_PRIVATE);
                    fout.write(response.toString().getBytes());
                    fout.close();
                } catch (IOException ioe) {
                    Utils.log("Error is : " + ioe);
                    ioe.printStackTrace();
                }

                if (fields.length() == 0) {
                    iv_actionbtn.setVisibility(View.GONE);
                } else {
                    iv_actionbtn.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException jse) {
            Utils.log("Error is : " + jse);
            jse.printStackTrace();
        }
    }

    public void handleLocalResult(JSONObject response) {
        try {
            Utils.log("Response : " + response);
            JSONObject filtersData = response.getJSONObject("SearchFilterResult");
            String status = filtersData.getString("status");
            String message = filtersData.getString("message");
            if (status.equals("0")) {
                JSONArray fields = filtersData.getJSONArray("GroupFilters");
                if (fields.length() == 0) {
                    iv_actionbtn.setVisibility(View.GONE);
                } else {
                    iv_actionbtn.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException jse) {
            Utils.log("Error is : " + jse);
            jse.printStackTrace();
        }
    }

    boolean isFieldPresent = false;
    private static final String DYNAMIC_FIELDS_FILE = "dynamicField.json";

//    @Override
//    public void onBackPressed() {
//       // PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
//        super.onBackPressed();
//    }
}