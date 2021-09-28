package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.DocumentAdapter;
import com.NEWROW.row.Adapter.DocumentRVAdapter;
import com.NEWROW.row.Data.DocumentListData;
import com.NEWROW.row.NotificationDataBase.DatabaseHelper;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.NEWROW.row.Adapter.DocumentRVAdapter.count_read_documents;
import static com.NEWROW.row.Utils.PreferenceManager.IS_GRP_ADMIN;
import static com.NEWROW.row.Utils.PreferenceManager.isRIadminModule;

/**
 * Created by USER on 31-12-2015.
 */
public class Documents extends Activity {
    static final int DOCUMENT_UPLOAD_REQUEST = 2;
    DocumentRVAdapter rvAdapter;
    RecyclerView rvDocs;
    ArrayList<String> selectedsubgrp;
    ListView listview;
    ArrayAdapter<String> adapter;
    TextView tv_title;
    ImageView iv_backbutton, iv_actionbtn;
    EditText et_search;
    String moduleName = "";
    EditText et_serach_document;
    private ArrayList<DocumentListData> list_documentData = new ArrayList<DocumentListData>();
    private DocumentAdapter adapter_document;
    private String grpID = "0";
    private String memberProfileID = "0";
    private String isAdmin = "No";
    private String moduleId;
    Spinner spinner_filter_type;
    String type_filter_flag = "1";
    String filtertype[] = {"Published", "All", "UnPublished", "Expired"};
    String filtertype_notadmin[] = {"Published", "All", "Expired"};

    //Added By Gaurav for RI admin only publish will apperad
    String filtertype_RI[] = {"Published"};
    String filtertype_notadmin_RI[] = {"Published"};
    private String messageId_temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );

        setContentView( R.layout.documents );

        listview = (ListView) findViewById( R.id.listView );

        tv_title = (TextView) findViewById( R.id.tv_title );
        iv_backbutton = (ImageView) findViewById( R.id.iv_backbutton );

        // iv_backbutton.setVisibility(View.GONE);
        String title = getIntent().getExtras().getString( "moduleName", "Documents" );

        tv_title.setText( title );

        et_serach_document = (EditText) findViewById( R.id.et_serach_document );
        iv_actionbtn = (ImageView) findViewById( R.id.iv_actionbtn );
        iv_actionbtn.setVisibility( View.VISIBLE );

        rvDocs = (RecyclerView) findViewById( R.id.rvDocs );

        rvDocs.setLayoutManager( new LinearLayoutManager( Documents.this ) );

        spinner_filter_type = (Spinner) findViewById( R.id.spinner_filter_type );

        spinnerSelectedDropdown();
        //-------------------------------
        grpID = PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.GROUP_ID );
        memberProfileID = PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.GRP_PROFILE_ID );
        moduleId = PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.MODULE_ID );

        isAdmin = PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.IS_GRP_ADMIN );
        Log.d( "Touchbase", "ID ID ID :- " + grpID + " - " + memberProfileID );
        Bundle intent = getIntent().getExtras();

        Intent intenti = getIntent();
        if (intenti.hasExtra( "memID" )) {
            memberProfileID = intenti.getStringExtra( "memID" );
            grpID = intenti.getStringExtra( "grpID" );
            isAdmin = intenti.getStringExtra( "isAdmin" );

            //update Data into Database
            messageId_temp = intenti.getStringExtra( "messageId" );
            if (messageId_temp != null) {
                //Create Database Helper Class Object
                DatabaseHelper databaseHelpers = new DatabaseHelper( this );

                boolean notificationInsert = databaseHelpers.updateData( messageId_temp );
                Log.d( "messageId_temp", "messageID ID ID AFTER :- " + messageId_temp );
                Log.d( "messageId_temp", "Is Data Updated :- " + notificationInsert );


            }


        }

        //Log.d("Touchbase", "ID ID ID AFTER :- " + grpID + " - " + memberProfileID);
        //-------------------------------
        if (InternetConnection.checkConnection( getApplicationContext() )) {
            //webservices();
            init();
            checkadminrights();
        } else {
            Toast.makeText( getApplicationContext(), "No internet connection", Toast.LENGTH_LONG ).show();
        }

        /*iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }

    private void spinnerSelectedDropdown() {

        if (PreferenceManager.getPreference( getApplicationContext(), isRIadminModule ).equals( "Yes" )) {

            //This code is addded By Gaurav for Ri admin

            spinner_filter_type.setVisibility( View.GONE );


          /*  if (PreferenceManager.getPreference(getApplicationContext(), IS_GRP_ADMIN).equals("No")) {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype_notadmin_RI);
                spinner_filter_type.setAdapter(spinnerArrayAdapter);
                spinner_filter_type.setVisibility(View.GONE);
            } else {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype_RI);
                spinner_filter_type.setAdapter(spinnerArrayAdapter);
                spinner_filter_type.setVisibility(View.GONE);

            }
*/
        } else {

            //This is normal flow for club and District Module

            if (PreferenceManager.getPreference( getApplicationContext(), IS_GRP_ADMIN ).equals( "No" )) {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, filtertype_notadmin );
                spinner_filter_type.setAdapter( spinnerArrayAdapter );
                spinner_filter_type.setVisibility( View.GONE );
            } else {
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, filtertype );
                spinner_filter_type.setAdapter( spinnerArrayAdapter );
            }

        }
    }

    private void checkadminrights() {
        if (isAdmin.equals( "No" )) {
            iv_actionbtn.setVisibility( View.GONE );
        }
        //Added By Gaurav
        if (PreferenceManager.getPreference( getApplicationContext(), isRIadminModule ).equals( "Yes" )) {
            iv_actionbtn.setVisibility( View.GONE );


        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InternetConnection.checkConnection( Documents.this )) {
            // Avaliable
            try{
                webservices();
            }
            catch (Exception e)
            {

            }

        } else {
            Utils.showToastWithTitleAndContext( Documents.this, "No Internet Connection!" );
            // Not Available...
        }
    }

    @Override
    public void onBackPressed() {

        try {
            int tempCount = FragmentALL.notificationCountDatas.getModuleCount( grpID, moduleId );
            if (tempCount >= count_read_documents) {
                int unreadCount = tempCount - count_read_documents;

                if (Documents_upload.count_write_documents > 0) {
                    unreadCount = unreadCount + Documents_upload.count_write_documents;
                }

                FragmentALL.notificationCountDatas.updateModuleCount( grpID, moduleId, "" + unreadCount );
            } else {
                FragmentALL.notificationCountDatas.updateModuleCount( grpID, moduleId, "0" );
            }
        /*for(int i =0;i<notificationCountDatas.size();i++)
        {
            if(notificationCountDatas.get(i).getGroupId().equalsIgnoreCase(""+grpID))
            {
                int tempCount = Integer.parseInt(notificationCountDatas.get(i).getId9());



                if(tempCount>=count_read_documents)
                {
                    int unreadCount = tempCount - count_read_documents;

                    if(count_write_documents>0) {
                        unreadCount = unreadCount+count_write_documents;
                    }

                    notificationCountDatas.get(i).setId9(""+unreadCount);
                }
                else {
                    notificationCountDatas.get(i).setId9(""+0);
                }
            }

        }*/

            count_read_documents = 0;
            Documents_upload.count_write_documents = 0;
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString( "GroupID", "" + grpID );
            intent.putExtras( bundle );
            setResult( RESULT_OK, intent );
            finish();

        } catch (Exception e) {
            Log.e( "TouchBase", "♦♦♦♦Error is : " + e.getMessage() );
            e.printStackTrace();
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            rvAdapter.unregisterFileDownloadReceiver();
        } catch (NullPointerException npe) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {

        et_serach_document.addTextChangedListener( new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //adapter.getFilter().filter(cs);
                //adapter.getFilter().filter(cs.toString());
                if (cs.equals( "" )) {
                    rvDocs.setAdapter( rvAdapter );
                    return;
                }
                int textlength = cs.length();
                ArrayList<DocumentListData> tempArrayList = new ArrayList<DocumentListData>();

                for (DocumentListData c : list_documentData) {
                    if (textlength <= c.getDocTitle().length()) {
                        if (c.getDocTitle().toLowerCase().contains( cs.toString().toLowerCase() )) {
                            tempArrayList.add( c );
                        }
                    }
                }
                //Data_array= tempArrayList;
                //   DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);
                if (tempArrayList.size() == 0) {
                    tempArrayList.add( new DocumentListData( "-1", "", "", "", "", "", "", "" ) );
                }
                DocumentRVAdapter adapter = new DocumentRVAdapter( Documents.this, tempArrayList );

                rvDocs.setAdapter( adapter );
                /*listview.setTextFilterEnabled(true);
                listview.setAdapter(adapter);*/

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
        } );

        et_serach_document.setOnEditorActionListener( new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (!InternetConnection.checkConnection( getApplicationContext() )) {
                    Toast.makeText( getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT ).show();
                    return true;
                }
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    webservices();
                    return true;
                }
                return false;
            }
        } );

        iv_actionbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( Documents.this, Documents_upload.class );
                startActivityForResult( i, DOCUMENT_UPLOAD_REQUEST );
            }
        } );

        spinner_filter_type.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d( "TouchBase", "@@@@@@@@@@@@@ " + spinner_filter_type.getSelectedItem().toString() );
                //{"All", "Published", "UnPublished", "Expired"};
                if (spinner_filter_type.getSelectedItem().toString().equals( "All" )) {
                    type_filter_flag = "0";
                } else if (spinner_filter_type.getSelectedItem().toString().equals( "Published" )) {
                    type_filter_flag = "1";
                } else if (spinner_filter_type.getSelectedItem().toString().equals( "UnPublished" )) {
                    type_filter_flag = "2";
                } else if (spinner_filter_type.getSelectedItem().toString().equals( "Expired" )) {
                    type_filter_flag = "3";
                }
                if (InternetConnection.checkConnection( getApplicationContext() )) {
                    //Available
                    webservices();
                } else {
                    Utils.showToastWithTitleAndContext( getApplicationContext(), "No Internet Connection!" );
                    // Not Available...
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
    }

    public void finishActivity(View v) {
        finish();
    }


    private void webservices() {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}

        //This code is added by Gaurav for when user View the data only when Data Cames from Rotary India admin module
        if (PreferenceManager.getPreference( getApplicationContext(), isRIadminModule ).equals( "Yes" )) {

            List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
            arrayList.add( new BasicNameValuePair( "memberProfileID", PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.GRP_PROFILE_ID ) ) );
            arrayList.add( new BasicNameValuePair( "grpID", grpID ) );
            arrayList.add( new BasicNameValuePair( "searchText", et_serach_document.getText().toString() ) );//

            arrayList.add( new BasicNameValuePair( "type", "1" ) );

            if (PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.IS_GRP_ADMIN ).equals( "No" )) {
                arrayList.add( new BasicNameValuePair( "isAdmin", "0" ) );

            } else {
                arrayList.add( new BasicNameValuePair( "isAdmin", "1" ) );
            }


            //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

            Log.d( "Response", "PARAMETERS " + Constant.RotaryIndiaGetDocumentList + " :- " + arrayList.toString() );
            new WebConnectionAsyncDirectory( Constant.RotaryIndiaGetDocumentList, arrayList, Documents.this ).execute();

        } else {

            List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
            // arrayList.add(new BasicNameValuePair("memberProfileID", memberProfileID));
            arrayList.add( new BasicNameValuePair( "memberProfileID", PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.GRP_PROFILE_ID ) ) );
            arrayList.add( new BasicNameValuePair( "grpID", grpID ) );
            arrayList.add( new BasicNameValuePair( "searchText", et_serach_document.getText().toString() ) );//

            if (PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.IS_GRP_ADMIN ).equals( "No" )) {
                arrayList.add( new BasicNameValuePair( "type", "1" ) );
            } else {
                arrayList.add( new BasicNameValuePair( "type", type_filter_flag ) );
            }
            if (PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.IS_GRP_ADMIN ).equals( "No" )) {
                arrayList.add( new BasicNameValuePair( "isAdmin", "0" ) );

            } else {
                arrayList.add( new BasicNameValuePair( "isAdmin", "1" ) );
            }


            //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

            Log.d( "Response", "PARAMETERS " + Constant.GetDocumentList + " :- " + arrayList.toString() );
            new WebConnectionAsyncDirectory( Constant.GetDocumentList, arrayList, Documents.this ).execute();
        }
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog( Documents.this, R.style.TBProgressBar );
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

            progressDialog.setCancelable( false );
            progressDialog.setProgressStyle( android.R.style.Widget_ProgressBar_Small );
            progressDialog.show();

        }

        @Override
        protected Object doInBackground(String... params) {


            try {

                val = HttpConnection.postData( url, argList );
                val = val.toString();

                Log.d( "Response", "we" + val );
            } catch (Exception e) {
                e.printStackTrace();


            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute( result );

            progressDialog.dismiss();
            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {
                Log.d( "Response", "calling getDirectorydetails" );

                getEbulletineItems( result.toString() );

            } else {
                Log.d( "Response", "Null Resposnse" );
            }
        }

    }

    private void getEbulletineItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject( result );
            JSONObject DocumentResult = jsonObj.getJSONObject( "TBDocumentistResult" );
            final String status = DocumentResult.getString( "status" );
            if (DocumentResult.has( "smscount" )) {
                Utils.smsCount = DocumentResult.getString( "smscount" );
            }
            list_documentData.clear();
            if (status.equals( "0" )) {
                JSONArray DocumentListResdult = DocumentResult.getJSONArray( "DocumentLsitResult" );

                //Log.d("@@@@@@@@@", "@@@@@@@@@@" + DocumentListResdult.length());
                list_documentData.clear();
                for (int i = 0; i < DocumentListResdult.length(); i++) {
                    JSONObject object = DocumentListResdult.getJSONObject( i );
                    JSONObject objects = object.getJSONObject( "DocumentList" );

                    DocumentListData data = new DocumentListData();

                    data.setDocTitle( objects.getString( "docTitle" ).toString() );
                    String url = data.setDocURL( objects.getString( "docURL" ).toString() );
                    data.setDocID( objects.getString( "docID" ).toString() );
                    data.setDocType( objects.getString( "docType" ).toString() );
                    data.setCreateDateTime( objects.getString( "createDateTime" ).toString() );
                    data.setAccessType( objects.getString( "docAccessType" ).toString() );
                    data.setIsRead( objects.getString( "isRead" ).toString() );
                    try {
                        data.setFilterType( objects.getString( "filterType" ).toString() );
                    } catch (JSONException jse) {
                        jse.printStackTrace();
                    }
                    list_documentData.add( data );
                }
                //adapter_document = new DocumentAdapter(Documents.this, R.layout.document_list_item, list_documentData,isAdmin);
                //listview.setAdapter(adapter_document);
                if (list_documentData.size() == 0) {
                    list_documentData.add( new DocumentListData( "-1", "", "", "", "", "", "", "" ) );
                }
                rvAdapter = new DocumentRVAdapter( Documents.this, list_documentData );
                rvDocs.setAdapter( rvAdapter );
            } else if (status.equals( "1" )) {
                if (list_documentData.size() == 0) {
                    // Document with id -1 indicates its empty object
                    list_documentData.add( new DocumentListData( "-1", "", "", "", "", "", "", "" ) );
                }
                rvAdapter = new DocumentRVAdapter( Documents.this, list_documentData );
                rvDocs.setAdapter( rvAdapter );
            }
            //listview.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));
        } catch (Exception e) {
            Log.d( "exec", "Exception :- " + e.toString() );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == DOCUMENT_UPLOAD_REQUEST) {
            if (InternetConnection.checkConnection( getApplicationContext() )) {
                webservices();
            } else {
                Toast.makeText( getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT ).show();
            }
        }
    }

}
