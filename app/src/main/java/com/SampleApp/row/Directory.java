package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.SampleApp.row.Adapter.DirectoryAdapter;
import com.SampleApp.row.Data.ContactData;
import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.TBPrefixes;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.DirectoryDataModel;

/**
 * Created by USER on 17-12-2015.
 */
public class Directory extends Activity {
    private static final String TAG = "";
    ListView listview;
    FloatingActionButton fab;


    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton addManually, invite, addFromPhonebook;


    ArrayAdapter<String> adapter;
    EditText et_serach_directory;

    private ArrayList<DirectoryData> directoryData = new ArrayList<DirectoryData>();


    private DirectoryAdapter directoryAdapter;

    private ArrayList<DirectoryData> newDirectoryData = new ArrayList<>();
    String updatedOn = "";

    TextView tv_title;
    ImageView iv_backbutton;
    private ImageView iv_actionbtn;
    private long masterUid, grpId;
    DirectoryDataModel directoryDataModel;
    private List<ContactData> list_contactData = new ArrayList<ContactData>();
    private static final int PHONE_BOOK_LIST = 101;

    public static ArrayList<DirectoryData> directoryDataComparison = new ArrayList<DirectoryData>();

    /*int finishCount = 2;
    private Handler inputFinishHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.e("TouchBase", "♦♦♦♦Value of finish count : "+finishCount);
            if ( finishCount == 0 ) {
                if (InternetConnection.checkConnection(Directory.this)) {
                    searchOnline();
                }
            } else {
                finishCount--;
                inputFinishHandler.sendEmptyMessageDelayed(0, 1000);

            }
        }
    };*/

    ImageView ivSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory);
        ivSearch = (ImageView) findViewById(R.id.btnSearch);
        listview = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.INVISIBLE);
        // iv_backbutton.setVisibility(View.GONE);
        String title = getIntent().getExtras().getString("moduleName", "Directory");
        tv_title.setText(title);
        directoryData = new ArrayList<>();
        et_serach_directory = (EditText) findViewById(R.id.et_serach_directory);
        masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by lekha
        grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));

        listview.setTextFilterEnabled(true);
        directoryDataModel = new DirectoryDataModel(getApplicationContext());
        Log.e("GroupID" , "--------------Group ID is : "+grpId);
        directoryDataModel.printTable();


        Log.d("******* LIST DATA *****","*******"+directoryDataModel.getDirectoryData(masterUid,grpId));

        fab = (FloatingActionButton)findViewById(R.id.fab);


        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        addManually = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        invite = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        addFromPhonebook = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

      /*  Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        list_contactData= (List<ContactData>)bundle.getSerializable("value");
        Log.d("******* LIST DATA *****","*******"+list_contactData);*/

        Log.d("******* LIST DATA *****","******* "+directoryData);

        init();
        loadFromDB();

        checkadminrights();

    }

    private void checkadminrights() {
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
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

                if(InternetConnection.checkConnection(getApplicationContext())) {
                    Intent i = new Intent(Directory.this, AddMemberToGroup.class);
                    startActivityForResult(i, 1);
                    materialDesignFAM.toggle(false);
                }

                else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }

            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Directory.this,android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_invite);


                final TextView tv_link = (TextView) dialog.findViewById(R.id.tv_link);
                TextView tv_share = (TextView) dialog.findViewById(R.id.tv_share);
                TextView tv_clipboard = (TextView) dialog.findViewById(R.id.tv_clipboard);
                ImageView iv_close = (ImageView)dialog.findViewById(R.id.iv_close);

              //  tv_link.setText(Constant.INVITE_BASE_URL+grpId);

                tv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String shareBody = "Hi Lets get connected through TouchBase app for Real time, Focused and Spam free communication. Download it now at\n\n"+Constant.INVITE_BASE_URL+grpId;

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Touchbase Invitation");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody );
                        //----------------------------------------------------------------



                       /* PackageManager pm = getPackageManager();
                        List<ResolveInfo> resInfo = pm.queryIntentActivities(sharingIntent, 0);
                        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
                        for (int i = 0; i < resInfo.size(); i++) {
                            // Extract the label, append it, and repackage it in a LabeledIntent
                            ResolveInfo ri = resInfo.get(i);
                            String packageName = ri.activityInfo.packageName;
                            if(packageName.contains("android.email")) {

                                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                emailIntent.setType("plain/text");
                                emailIntent.setData(Uri.parse("mailto:"));
                               // emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{tv_email.getText().toString()});
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi Lets get connected through TouchBase app for Real time, Focused and Spam free communication. Download it now at");
                                sharingIntent.setPackage(packageName);
                            }

                        }

*/

                        //--------------------------------------------------------------------
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                      //  dialog.dismiss();



                    }
                });
                tv_link.setText(Html.fromHtml(Constant.INVITE_BASE_URL+grpId));
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
                if(InternetConnection.checkConnection(getApplicationContext())) {

                    Intent phone_book_intent = new Intent(Directory.this, Contact_Import.class);
                    startActivityForResult(phone_book_intent, PHONE_BOOK_LIST);
                    materialDesignFAM.toggle(false);
                }
                else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }

            }
        });


      /*  iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* Intent i = new Intent(Directory.this, AddMemberToGroup.class);
                startActivityForResult(i, 1);*//*
                PopupMenu popup = new PopupMenu(Directory.this, iv_actionbtn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.directory_popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_manually:
                                Intent i = new Intent(Directory.this, AddMemberToGroup.class);
                                startActivityForResult(i, 1);
                                // read the listItemPosition here
                                return true;
                          *//*  case R.id.phone_book:
                                Intent phone_book_intent = new Intent(Directory.this, Contact_Import.class);
                                startActivityForResult(phone_book_intent,PHONE_BOOK_LIST);
                                // read the listItemPosition here
                                return true;*//*

                            default:
                                return false;
                        }
                        //return true;
                    }
                });

                popup.show();

            }
        });
*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phone_book_intent = new Intent(Directory.this, Contact_Import.class);
                startActivityForResult(phone_book_intent,PHONE_BOOK_LIST);

            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Directory.this, ProfileActivityV4.class);
                i.putExtra("memberprofileid", directoryData.get(position).getProfileID());
                i.putExtra("groupId", directoryData.get(position).getGrpID());
                startActivity(i);

                //Log.d("******* LIST DATA *****","*******"+list_contactData);
            }
        });


        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = et_serach_directory.getText().toString();
                try {
                    hideInput();
                    if (InternetConnection.checkConnection(Directory.this)) {
                        searchOnline();
                    } else {
                        if (q.indexOf("'") != -1) {
                            Toast.makeText(Directory.this, "Invalid characters in search keyword", Toast.LENGTH_LONG).show();
                            return;
                        }
                        ArrayList<DirectoryData> newDirectoryData = directoryDataModel.search(masterUid, grpId, q);
                        directoryAdapter = new DirectoryAdapter(Directory.this, R.layout.directory_list_item, newDirectoryData, "0");
                        listview.setAdapter(directoryAdapter);
                        if (newDirectoryData.size() == 0) {
                            TextView empty = ((TextView) findViewById(R.id.tv_no_records_found));
                            empty.setText("No records found");
                            listview.setEmptyView(empty);
                        }
                    }
                } catch(Exception e) {
                    Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
                    e.printStackTrace();
                }
            }
        });


        et_serach_directory.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String q = et_serach_directory.getText().toString();
                if ( q.equals("")){
                    if (q.indexOf("'") != -1) {
                        Toast.makeText(Directory.this, "Invalid characters in search keyword", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ArrayList<DirectoryData> newDirectoryData = directoryDataModel.search(masterUid, grpId, q);
                    directoryAdapter = new DirectoryAdapter(Directory.this, R.layout.directory_list_item, newDirectoryData, "0");
                    listview.setAdapter(directoryAdapter);
                    if (newDirectoryData.size() == 0) {
                        TextView empty = ((TextView) findViewById(R.id.tv_no_records_found));
                        empty.setText("No records found");
                        listview.setEmptyView(empty);
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

                    // webservices();

                    /*if (InternetConnection.checkConnection(getApplicationContext())) {
                        webservices();
                    } else {
                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    }*/
                    return true;
                }
                return false;
            }
        });
    }


    public void finishActivity(View v) {
        finish();
    }

    private void webservices() {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("searchText", et_serach_directory.getText().toString()));
        arrayList.add(new BasicNameValuePair("page", ""));
        /*String updatedOn = PreferenceManager.getPreference(this, "DirectoryUpdatedOn", );
        arrayList.add(new BasicNameValuePair("updatedOn", "1970/01/01 00:00:00"));*/

        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.DIRECTORY_PREFIX+grpId, "1970/01/01 00:00:00");
        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));
        Log.e("UpdatedOn", "Last updated on time is : "+updatedOn);
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GetDirectoryList + " :- " + arrayList.toString());
        new WebConnectionAsyncDirectory(Constant.GetDirectoryList, arrayList, Directory.this).execute();
    }


    private void searchOnline() {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("searchText", et_serach_directory.getText().toString()));
        arrayList.add(new BasicNameValuePair("page", ""));
        /*String updatedOn = PreferenceManager.getPreference(this, "DirectoryUpdatedOn", );
        arrayList.add(new BasicNameValuePair("updatedOn", "1970/01/01 00:00:00"));*/

        updatedOn = "1970/01/01 00:00:00";
        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "♦♦♦♦PARAMETERS " + Constant.GetDirectoryList + " :- " + arrayList.toString());
        new SearchOnlineTask(Constant.GetDirectoryList, arrayList, Directory.this).execute();
    }

    public class SearchOnlineTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Directory.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public SearchOnlineTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();

            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData(url, argList);
                val = val.toString();
                Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //	Log.d("response","Do post"+ result.toString());
            if (result != "") {

                Log.d("Response", "calling getDirectorydetails");
                getSearchItems(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }

    private void getSearchItems(String result) {
        Log.e("TouchBase", "♦♦♦♦Search result : "+result);
        if ( directoryData != null ) directoryData.clear();

        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("TBMemberResult");
            final String status = DirectoryResult.getString("status");
            directoryData = new ArrayList<DirectoryData>();
            if (status.equals("0")) {
                JSONArray DirectoryListResdult = DirectoryResult.getJSONArray("MemberListResults");

                //   Log.d("@@@@@@@@@", "@@@@@@@@@@" + DirectoryListResdult.length());
                directoryData.clear();
                for (int i = 0; i < DirectoryListResdult.length(); i++) {
                    JSONObject object = DirectoryListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");

                    DirectoryData data = new DirectoryData();

                    data.setMasterUID(objects.getString("masterUID").toString());
                    data.setGrpID(objects.getString("grpID").toString());
                    data.setProfileID(objects.getString("profileID").toString());
                    data.setGroupName(objects.getString("groupName").toString());
                    data.setMemberName(objects.getString("memberName").toString());
                    data.setPic(objects.getString("pic").toString());
                    //  data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setMembermobile(objects.getString("membermobile").toString());
                    data.setGrpCount(objects.getString("grpCount").toString());

                    directoryData.add(data);
                }
                directoryAdapter = new DirectoryAdapter(Directory.this, R.layout.directory_list_item, directoryData, "0");
                listview.setAdapter(directoryAdapter);
                listview.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));

                directoryAdapter.notifyDataSetChanged();
            } else {
                directoryData.clear();
                directoryAdapter = new DirectoryAdapter(Directory.this, R.layout.directory_list_item, directoryData, "0");
                listview.setAdapter(directoryAdapter);
                listview.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));
                directoryAdapter.notifyDataSetChanged();
            }

            //finishCount = 0;
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Directory.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public WebConnectionAsyncDirectory(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();

            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData(url, argList);
                val = val.toString();
                Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //	Log.d("response","Do post"+ result.toString());
            if (result != "") {

                Log.d("Response", "calling getDirectorydetails");
                getDirectoryItems(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }

    private void getDirectoryItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("TBMemberResult");
            final String status = DirectoryResult.getString("status");
            if (status.equals("0")) {
                JSONArray DirectoryListResdult = DirectoryResult.getJSONArray("MemberListResults");
                directoryData = new ArrayList<DirectoryData>();
                //   Log.d("@@@@@@@@@", "@@@@@@@@@@" + DirectoryListResdult.length());
                directoryData.clear();
                for (int i = 0; i < DirectoryListResdult.length(); i++) {
                    JSONObject object = DirectoryListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");

                    DirectoryData data = new DirectoryData();

                    data.setMasterUID(objects.getString("masterUID").toString());
                    data.setGrpID(objects.getString("grpID").toString());
                    data.setProfileID(objects.getString("profileID").toString());
                    data.setGroupName(objects.getString("groupName").toString());
                    data.setMemberName(objects.getString("memberName").toString());
                    data.setPic(objects.getString("pic").toString());
                    //  data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setMembermobile(objects.getString("membermobile").toString());
                    data.setGrpCount(objects.getString("grpCount").toString());

                    directoryData.add(data);
                }
                directoryAdapter = new DirectoryAdapter(Directory.this, R.layout.directory_list_item, directoryData, "0");
                listview.setAdapter(directoryAdapter);
                updatedOn = DirectoryResult.getString("curDate");
                directoryDataHandler.sendEmptyMessageDelayed(0, 1000);
            }
            listview.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TOUCHBASE", "REQUEST CODE" + resultCode);
        // check if the request code is same as what is passed  here it is 1
        if (requestCode == 1) {
            if (InternetConnection.checkConnection(getApplicationContext())) {
                checkForUpdate();
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
            // webservices();
        }

        if (requestCode == PHONE_BOOK_LIST && resultCode ==RESULT_OK) {
            if (data != null) {


                checkForUpdate();



/*
                Bundle bundle = data.getExtras();
                ArrayList<ContactData> contactDatas= (ArrayList<ContactData>) bundle.getSerializable("value");
                //list_contactData =  (ArrayList<ContactData>)getIntent().getSerializableExtra("value");
                DirectoryData insertData = null;
                insertData = new DirectoryData();
                grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));

                */
/*
                JSONArray array = new JSONArray();
                array.put("1st array item");
                array.put("2nd array item");
                *//*


                for(int i=0;i<contactDatas.size();i++) {
                    insertData.setMasterUID("");
                    insertData.setGrpID("");
                    insertData.setProfileID("");
                    insertData.setGroupName("");
                    insertData.setMemberName(""+contactDatas.get(i).getContactName());
                    insertData.setPic("");
                    insertData.setMembermobile(""+contactDatas.get(i).getContactNumber());
                    insertData.setGrpCount("");
                  //  directoryDataModel.insertPhoneContact(masterUid,contactDatas.get(i).getContactName(),contactDatas.get(i).getContactNumber(),grpId,insertData);
                }
*/
             /*   for (int i=0;i<modifyContactDatas.size();i++)
                {

                    //  list_contactData.add(new ContactData(p.getContactName(), p.getContactNumber() , p.getCountryCode() , p.getIdNumber() ,p.box,p.getId() ));

                    ContactData contactData = new ContactData();
                    contactData.setContactName(modifyContactDatas.get(i).getContactName());
                    contactData.setContactNumber(modifyContactDContactDataatas.get(i).getContactNumber());
                    contactData.setId(modifyContactDatas.get(i).getId());
                    int pos =Integer.parseInt(modifyContactDatas.get(i).getId());
                    list_contactData= (List<ContactData>)bundle.getSerializable("value");

                    Log.d("-------","-------"+list_contactData);
                }*/
                    // adapter_contactData.notifyDataSetChanged();
                }

        }
    }

    //-----------------------Offline Database ----------------------

    public void loadFromDB() {

        Log.d("Touchbase", "Trying to load from local db");

        //directoryData.clear();

        // directoryData = directoryDataModel.getGroups(masterUid);

        directoryData = new ArrayList<DirectoryData>();
        directoryData = directoryDataModel.getDirectoryData(masterUid, grpId);
     //   directoryDataComparison =directoryDataModel.getDirectoryData(masterUid,grpId);
    //    Log.d("**** Database LIST DATA *****","**** "+directoryData);

        //Log.e("touchbase list", "**************"+grplist.toString());
        boolean isDataAvailable = directoryDataModel.isDataAvailable(grpId);
        Log.e("DataAvailable", "Data available : " + isDataAvailable);

        if (!isDataAvailable) {
            Log.d("Touchbase", "Loading from server");
            if (InternetConnection.checkConnection(getApplicationContext()))
                loadFromServer();
            else
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
        } else {

            Log.d("Touchbase", "Loaded from local db");
            directoryAdapter = new DirectoryAdapter(Directory.this, R.layout.directory_list_item, directoryData, "0");
            listview.setAdapter(directoryAdapter);

            // If data is loaded from local database then check for update

            checkForUpdate();
            Log.d("---------------","Check for update gets called------");
        }
    }

    public void loadFromServer() {
        webservices();
    }

    Handler directoryDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("DBError", "Handler is called");
            boolean saved = directoryDataModel.insert(masterUid, directoryData);
            if (!saved) {
                Log.d("Touchbase", "Failed to save offlline. Retrying in 2 seconds");
                sendEmptyMessageDelayed(0, 2000);
            } else {
                PreferenceManager pm = new PreferenceManager();
                pm.savePreference(getApplicationContext(), TBPrefixes.DIRECTORY_PREFIX+grpId, updatedOn);

                //System.out.println(savePreference())
               /* SyncInfoModel syncModel = new SyncInfoModel(getContext());
                boolean updated = syncModel.update(masterUid, DateHelper.getCurrentDate());
                if ( !updated ) syncModel.insert(masterUid, DateHelper.getCurrentDate());
*/

                Log.d("-----------", "----Directory data Offline----");
                //syncModel.update(masterUid, Utils.)
            }
        }
    };

    //========================== changes by lekha for updating data(Sync) ===============

    public void checkForUpdate() {
        if (InternetConnection.checkConnection(getApplicationContext())) {
            Log.e("Touchbase", "------ Checking for update");
            String url = Constant.GetDirectoryListSync;
            ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
            arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
            arrayList.add(new BasicNameValuePair("grpId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
         //   arrayList.add(new BasicNameValuePair("searchText", et_serach_directory.getText().toString()));
         //   arrayList.add(new BasicNameValuePair("page", ""));
            updatedOn = PreferenceManager.getPreference(this, TBPrefixes.DIRECTORY_PREFIX+grpId);
            Log.e("UpdatedOn", "Last updated date is : "+updatedOn);
            Log.e("MasterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID));

            arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));
            //arrayList.add(new BasicNameValuePair("updatedOn", "2016/1/18 17:8:34"));

            Log.e("request", arrayList.toString());
            UpdateDirectoryAsyncTask task = new UpdateDirectoryAsyncTask(url, arrayList, getApplicationContext());
            task.execute();
            //Log.d("Response", "PARAMETERS " + Constant.GetDirectoryListSync + " :- " + arrayList.toString());
            //new WebConnectionAsyncDirectory(Constant.GetDirectoryListSync, arrayList, Directory.this).execute();

        } else {
            Log.e("SyncFailed", "No internet connection to sync data");
        }
    }

    public class UpdateDirectoryAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Directory.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public UpdateDirectoryAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();

            /*progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();*/
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData(url, argList);
                val = val.toString();
                Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            //progressDialog.dismiss();
            //	Log.d("response","Do post"+ result.toString());

            if (result != "" && result!=null) {
                Log.d("Response", "calling getDirectorydetails");

                getUpdatedDirectoryItems(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    public void getUpdatedDirectoryItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("MemberDirectorResult");

            final String status = DirectoryResult.getString("status");
            newDirectoryData = new ArrayList<>();

            if (status.equals("0")) {
               // final ArrayList<DirectoryData> newDirectoryData = new ArrayList<DirectoryData>();
                JSONArray newDirectoryListResult = DirectoryResult.getJSONObject("Result").getJSONArray("newMembers");
                int newCount = newDirectoryListResult.length();


                for (int i = 0; i < newCount; i++) {
                    // JSONObject object = newDirectoryListResult.getJSONObject(i);
                    //JSONObject objects = object.getJSONObject("MemberListResult");

                    DirectoryData data = new DirectoryData();

                    JSONObject result_object = newDirectoryListResult.getJSONObject(i);

                    data.setMasterUID(result_object.getString("masterUID").toString());
                    data.setGrpID(result_object.getString("grpID").toString());
                    data.setProfileID(result_object.getString("profileID").toString());
                    data.setGroupName(result_object.getString("groupName").toString());
                    data.setMemberName(result_object.getString("memberName").toString());
                    data.setPic(result_object.getString("pic").toString());
                    //  data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setMembermobile(result_object.getString("membermobile").toString());
                    data.setGrpCount(result_object.getString("grpCount").toString());
                    data.setIsDeleted(Boolean.valueOf(result_object.getString("isDeleted").toString()));
                    newDirectoryData.add(data);
                    //directoryData.add(data);
                }
                final ArrayList<DirectoryData> updatedDirectoryData = new ArrayList<DirectoryData>();
                JSONArray updatedDirectoryListResult = DirectoryResult.getJSONObject("Result").getJSONArray("updatedMembers");
                int updateCount = updatedDirectoryListResult.length();
                for (int i = 0; i < updateCount; i++) {
                   /* JSONObject object = updatedDirectoryListResult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");*/

                    DirectoryData data = new DirectoryData();

                    JSONObject updated_object = updatedDirectoryListResult.getJSONObject(i);

                    data.setMasterUID(updated_object.getString("masterUID").toString());
                    data.setGrpID(updated_object.getString("grpID").toString());
                    data.setProfileID(updated_object.getString("profileID").toString());
                    data.setGroupName(updated_object.getString("groupName").toString());
                    data.setMemberName(updated_object.getString("memberName").toString());
                    data.setPic(updated_object.getString("pic").toString());
                    //  data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setMembermobile(updated_object.getString("membermobile").toString());
                    data.setIsDeleted(Boolean.valueOf(updated_object.getString("isDeleted").toString()));
                    data.setGrpCount(updated_object.getString("grpCount").toString());
                    updatedDirectoryData.add(data);
                }

                final ArrayList<DirectoryData> deletedDirectoryData = new ArrayList<DirectoryData>();
                JSONArray deletedDirectoryListResult = DirectoryResult.getJSONObject("Result").getJSONArray("deletedMembers");
                int deleteCount = deletedDirectoryListResult.length();
                for (int i = 0; i < deleteCount; i++) {
                   /* JSONObject object = deletedDirectoryListResult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");*/

                    DirectoryData data = new DirectoryData();
                    JSONObject deleted_object = deletedDirectoryListResult.getJSONObject(i);

                    data.setMasterUID(deleted_object.getString("masterUID").toString());
                    data.setGrpID(deleted_object.getString("grpID").toString());
                    data.setProfileID(deleted_object.getString("profileID").toString());
                    data.setGroupName(deleted_object.getString("groupName").toString());
                    data.setMemberName(deleted_object.getString("memberName").toString());
                    data.setPic(deleted_object.getString("pic").toString());
                    //data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setMembermobile(deleted_object.getString("membermobile").toString());
                    data.setIsDeleted(Boolean.valueOf(deleted_object.getString("isDeleted").toString()));
                    data.setGrpCount(deleted_object.getString("grpCount").toString());
                    deletedDirectoryData.add(data);
                }

                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        boolean saved = directoryDataModel.syncData(masterUid, grpId, newDirectoryData, updatedDirectoryData, deletedDirectoryData);
                        if ( ! saved ) {
                            Log.e("SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            // updating last updated date in shared preferences.
                            PreferenceManager.savePreference(getApplicationContext(), TBPrefixes.DIRECTORY_PREFIX+grpId, updatedOn);
                           // Reloading all data for display purpose.
                            directoryData = directoryDataModel.getDirectoryData(masterUid, grpId);
                            directoryAdapter = new DirectoryAdapter(Directory.this, R.layout.directory_list_item, directoryData, "0");
                            //directoryAdapter.notifyDataSetChanged();
                            listview.setAdapter(directoryAdapter);
                            Log.d("-----------","----Updated data------"+directoryData);
                          //  Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                int overAllCount = newCount + updateCount + deleteCount;
                System.out.println("Number of records for update are : "+overAllCount);
                if ( newCount + updateCount + deleteCount != 0) {
                    updatedOn = DirectoryResult.getString("curDate");
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Log.e("NoUpdate", "No updates found");
                }
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
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

    public void hideInput(){
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }
}