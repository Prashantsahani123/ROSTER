package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.SampleApp.row.Adapter.SpinnerAdapter_categoery;
import com.SampleApp.row.Adapter.SpinnerAdapter_country;
import com.SampleApp.row.Data.CategoryData;
import com.SampleApp.row.Data.CountryData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.ImageCompression;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.croputility.Crop;

import static com.SampleApp.row.Utils.PreferenceManager.savePreference;

/**
 * Created by USER on 29-12-2015.
 */
public class CreateGroup extends Activity {

    String grouptype[] = {"Select Nature", "Open", "Close"};

    ArrayList<CountryData> listcounty = new ArrayList<>();
    ArrayList<CategoryData> listcategoery = new ArrayList<>();
    TextView tv_title;
    ImageView iv_backbutton, iv_groupimg;
    ImageView call_button;
    Spinner spinner_group_type, spinner_group_category, spinner_group_country;
    TextView ib_next,tv_countrycode;
    EditText et_grpname, et_address1, et_address2, et_city, et_pincode, et_state, et_email_address, et_website, et_mobile, et_other;
    String flag_callwebsercie = "0";
    String countryid = "";
    String categoeryid = "";
    String selectedgrouptype = "Close";
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    private String uploadedimgid = "0";
    ProgressDialog pd;
    private static double longitute, latitude;
    private String group_id;
    String picturePath;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String groupid = "0";
    private String callforgroupedit = "0";

    boolean updateProfileImage = false;
    static int Capture_Camera = 100;
    Bitmap updatedProfileImage;
    static Uri mCapturedImageURI;
    private String hasimageflag = "0";
    String city = "";
    String state = "";
    String country = "";
    String postalCode = "";


    final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("New Entity");

        ib_next = (TextView) findViewById(R.id.ib_next);
        tv_countrycode = (TextView) findViewById(R.id.tv_countrycode);
        // spinner_group_type = (Spinner) findViewById(R.id.group_type);
        spinner_group_category = (Spinner) findViewById(R.id.group_category);
        spinner_group_country = (Spinner) findViewById(R.id.group_country);
        et_grpname = (EditText) findViewById(R.id.et_grpname);
        et_address1 = (EditText) findViewById(R.id.et_address1);
        //  et_address2 = (EditText) findViewById(R.id.et_address2);
        // et_city = (EditText) findViewById(R.id.et_city);
        // et_pincode = (EditText) findViewById(R.id.et_pincode);
        // et_state = (EditText) findViewById(R.id.et_state);
        et_email_address = (EditText) findViewById(R.id.et_email_address);
        // et_website = (EditText) findViewById(R.id.et_website);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_other = (EditText) findViewById(R.id.et_other);
        iv_groupimg = (ImageView) findViewById(R.id.iv_groupimg);

        // spinner_group_type.setPrompt("Select your favorite Planet!");
        // ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.tv_name, grouptype);
        // spinner_group_type.setAdapter(spinnerArrayAdapter);


        //   ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, colors_groupcategory);
        //   spinner_group_category.setAdapter(spinnerArrayAdapter1);


        // ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, colors_country);
        //group_country.setAdapter(spinnerArrayAdapter2);

        //webservices_getdata();

        if (InternetConnection.checkConnection(getApplicationContext())) {
            // Avaliable
            webservices_getdata();
        } else {
            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
            // Not Available...
        }


        //getdatatoeditgroup();

        init();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getdatatoeditgroup() {
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            group_id = intent.getString("groupid"); // Created Group ID
            if (InternetConnection.checkConnection(getApplicationContext())) {
                // Avaliable
                callforgroupedit = "1";
                ib_next.setText("Update Entity");
                tv_title.setText("Update Entity");
                webservices_groupdataedit();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                // Not Available...
            }
        }

    }


    private void init() {

      /*  et_address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EventVenueSearchAddress.class);
                startActivityForResult(i, 5);
            }
        });*/


        et_address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TOUCHBASE", "CLICK CLICK");

           /*     if (et_address1.getText().toString().trim().length() <= 0) {
                    Intent i = new Intent(getApplicationContext(), EventVenueSearchAddress.class);
                    startActivityForResult(i, 5);
                    et_address1.setFocusableInTouchMode(false);
                } else
           */

                ///==========================
                    et_address1.setFocusableInTouchMode(true);

            }
        });

        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupback();
            }

        });
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent i = new Intent(CreateGroup.this, CreateGroupModule.class);
                if (validation() == true) {
                    //   webservices();
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        // Avaliable
                        webservices();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }
                }
                // startActivity(i);
            }
        });

        spinner_group_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Log.d("Touchbase", "Selected Country :- " + listcounty.get(position).getCountryName());
                    countryid = listcounty.get(position).getCountryId();
                    tv_countrycode.setText(listcounty.get(position).getCountryCode());
                } else {
                    countryid = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_group_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Log.d("Touchbase", "Selected Country :- " + listcategoery.get(position).getCatName());
                    categoeryid = listcategoery.get(position).getCatId();
                    if (listcategoery.get(position).getCatName().equals("Other")) {
                        et_other.setVisibility(View.VISIBLE);
                        // et_other.setText("");
                    } else {
                        et_other.setVisibility(View.GONE);
                        et_other.setText("");
                    }
                } else {
                    categoeryid = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  spinner_group_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String workRequestType = parent.getItemAtPosition(position).toString();
                    selectedgrouptype = workRequestType;
                    Log.d("Touchbase", "Categoery :- " + workRequestType);
                } else {
                    selectedgrouptype = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        iv_groupimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        selectImage();
                    }
                }
            }
        });
    }

    private void popupback() {
        final Dialog dialog = new Dialog(CreateGroup.this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_confrm_delete);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
        tv_line1.setText("Are you sure you want to go back? All your data will be lost.");
        tv_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialog.show();
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        get_AddressDetails(et_address1.getText().toString());
        arrayList.add(new BasicNameValuePair("grpId", groupid)); // 0 to create new grp
        arrayList.add(new BasicNameValuePair("grpName", et_grpname.getText().toString()));
        //  arrayList.add(new BasicNameValuePair("grpPic", ""));
        arrayList.add(new BasicNameValuePair("grpImageID", uploadedimgid));
        arrayList.add(new BasicNameValuePair("grpType", selectedgrouptype));
        arrayList.add(new BasicNameValuePair("grpCategory", categoeryid));
        // address = address.replaceAll("(\\r|\\n)", "");
        //arrayList.add(new BasicNameValuePair("addrss1", et_address1.getText().toString()));
        arrayList.add(new BasicNameValuePair("addrss1", ""+et_address1.getText().toString().trim().replaceAll("(\\r|\\n)", "").replaceAll("[()]", "")));
        arrayList.add(new BasicNameValuePair("addrss2", ""));
        arrayList.add(new BasicNameValuePair("city", city));
        arrayList.add(new BasicNameValuePair("state", state));
        arrayList.add(new BasicNameValuePair("pincode", postalCode));
        arrayList.add(new BasicNameValuePair("country", countryid));

        arrayList.add(new BasicNameValuePair("emailid", et_email_address.getText().toString()));
        arrayList.add(new BasicNameValuePair("mobile", et_mobile.getText().toString()));
        arrayList.add(new BasicNameValuePair("website", ""));

        arrayList.add(new BasicNameValuePair("other", et_other.getText().toString()));
        arrayList.add(new BasicNameValuePair("userId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        //  arrayList.add(new BasicNameValuePair("userpwd", ""));

        flag_callwebsercie = "0";
        Log.d("Response", "PARAMETERS " + Constant.CreateGroup + " :- " + arrayList.toString());
        new WebConnectionAsync(Constant.CreateGroup, arrayList, CreateGroup.this).execute();
    }

    private void webservices_groupdataedit() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("grpID", group_id));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        flag_callwebsercie = "2";
        Log.d("Response", "PARAMETERS " + Constant.GetGroupInfo + " :- " + arrayList.toString());
        new WebConnectionAsync(Constant.GetGroupInfo, arrayList, CreateGroup.this).execute();
    }

    private void webservices_getdata() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        //arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        flag_callwebsercie = "1";
        Log.d("Response", "PARAMETERS " + Constant.GetAllCountriesAndCategories + " :- " + arrayList.toString());
        new WebConnectionAsync(Constant.GetAllCountriesAndCategories, arrayList, CreateGroup.this).execute();
    }

    private void remove_photo_webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", group_id));
        arrayList.add(new BasicNameValuePair("type", "Group"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

        flag_callwebsercie = "4";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteImage + " :- " + arrayList.toString());
        new WebConnectionAsync(Constant.DeleteImage, arrayList, CreateGroup.this).execute();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateGroup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://kaizen.app.com.touchbase/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateGroup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://kaizen.app.com.touchbase/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class WebConnectionAsync extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(CreateGroup.this, R.style.TBProgressBar);

        public WebConnectionAsync(String url, List<NameValuePair> argList, Context ctx) {
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
            if (result != "") {
                if (flag_callwebsercie.equals("0")) {
                    getresultCreateGroup(result.toString());
                } else if (flag_callwebsercie.equals("1")) {
                    getresult(result.toString());
                } else if (flag_callwebsercie.equals("2")) {
                    getresult_addeddata(result.toString());
                } else if (flag_callwebsercie.equals("4")) {
                    getresultOfRemovephoto(result.toString());
                }

            } else {
                Log.d("Response", "Null Resposnse");
            }
        }


        private void getresult(String val) {
            try {
                JSONObject jsonObj = new JSONObject(val);
                JSONObject ActivityResult = jsonObj.getJSONObject("TBCountryResult");
                final String status = ActivityResult.getString("status");
                if (status.equals("0")) {

                    JSONArray grpsarray = ActivityResult.getJSONArray("CountryLists");
                    listcounty.add(new CountryData("0", "0", "Select Country"));
                    for (int i = 0; i < grpsarray.length(); i++) {
                        JSONObject object = grpsarray.getJSONObject(i);
                        JSONObject objects = object.getJSONObject("GrpCountryList");

                        CountryData gd = new CountryData();
                        gd.setCountryId(objects.getString("countryId").toString());
                        gd.setCountryCode(objects.getString("countryCode").toString());
                        gd.setCountryName(objects.getString("countryName").toString());

                        listcounty.add(gd);

                    }

                    JSONArray catarray = ActivityResult.getJSONArray("CategoryLists");
                    listcategoery.add(new CategoryData("0", "Select Category"));
                    for (int i = 0; i < catarray.length(); i++) {
                        JSONObject object = catarray.getJSONObject(i);
                        JSONObject objects = object.getJSONObject("GrpCatList");

                        CategoryData gd = new CategoryData();
                        gd.setCatId(objects.getString("catId").toString());
                        gd.setCatName(objects.getString("catName").toString());

                        listcategoery.add(gd);


                    }
                   // Log.d("TOUCHBASE", "CATEGOERY :--" + listcategoery.toString());
                    //    ArrayAdapter<CountryData> spinnerArrayAdapter2 = new ArrayAdapter<CountryData>(this, android.R.layout.simple_spinner_dropdown_item, );
                    //  group_country.setAdapter(spinnerArrayAdapter2);
                    spinner_group_country.setAdapter(new SpinnerAdapter_country(CreateGroup.this, listcounty));
                    spinner_group_category.setAdapter(new SpinnerAdapter_categoery(CreateGroup.this, listcategoery));
                    // group_country.setAdapter(new SpinnerAdapter_country());
                    getdatatoeditgroup();// This is called afterwards because it has to execute after the country webservice

                }

            } catch (Exception e) {
                Log.d("exec", "Exception :- " + e.toString());
            }

        }

        private void getresultCreateGroup(String val) {
            try {
                JSONObject jsonObj = new JSONObject(val);
                JSONObject ActivityResult = jsonObj.getJSONObject("CreateGRpResult");
                final String status = ActivityResult.getString("status");
                if (status.equals("0")) {
                    String msg = ActivityResult.getString("message");

                    //   Log.d("Touchbase", "*************** " + status);
                    //  Log.d("Touchbase", "*************** " + msg);
                    if (callforgroupedit.equals("1")) {
                        Intent intent = new Intent();
                        setResult(1, intent);
                        finish();//finishing activity
                    } else {
                        Intent i = new Intent(getApplicationContext(), CreateGroupModule.class);
                        i.putExtra("createdgroupid", "" + ActivityResult.getString("grdId"));
                        startActivity(i);
                    }

                }

            } catch (Exception e) {
                Log.d("exec", "Exception :- " + e.toString());
            }

        }

        private void getresult_addeddata(String val) {
            try {
                JSONObject jsonObj = new JSONObject(val);
                JSONObject EventResult = jsonObj.getJSONObject("TBGetGroupResult");
                final String status = EventResult.getString("status");
                if (status.equals("0")) ;
                {
                    savePreference(CreateGroup.this,"isGroupEdited","Yes");
                    JSONArray EventListResdult = EventResult.getJSONArray("getGroupDetailResult");
                    for (int i = 0; i < EventListResdult.length(); i++) {
                        JSONObject object = EventListResdult.getJSONObject(i);
                        JSONObject objects = object.getJSONObject("GetGroupInfo");

                        groupid = objects.getString("grpId").toString();

                        et_grpname.setText(objects.getString("grpName").toString());
                        et_email_address.setText(objects.getString("emailid").toString());
                        // et_website.setText(objects.getString("website").toString());
                        et_mobile.setText(objects.getString("mobile").toString());
                        et_address1.setText(objects.getString("addrss1").toString());
                        // et_city.setText(objects.getString("city").toString());
                        // et_pincode.setText(objects.getString("pincode").toString());
                        // et_state.setText(objects.getString("state").toString());
                        city = objects.getString("city").toString();
                        state = objects.getString("state").toString();
                        postalCode = objects.getString("pincode").toString();

                        selectedgrouptype = objects.getString("grpType").toString();
                        categoeryid = objects.getString("grpCategory").toString();
                        Log.d("TOUCHBASE", "OTHER CAT" + objects.getString("grpCategoryName").toString());
                        et_other.setText("" + objects.getString("grpCategoryName").toString());
                        if(categoeryid.equals("5")){
                            et_other.setVisibility(View.VISIBLE);
                        }else{
                            et_other.setVisibility(View.GONE);
                        }

                        countryid = objects.getString("country").toString();

                        objects.getString("grpImage").toString();// IMAGE

                        if (objects.has("grpImage")) {
                            String a = objects.getString("grpImage").toString();
                            if (objects.getString("grpImage").toString().equals("") || objects.getString("grpImage").toString() == null) {
                                // linear_image.setVisibility(View.GONE);
                            } else {
                                //  imageurl = objects.getString("eventImg").toString();
                                //progressbar.setVisibility(View.VISIBLE);
                                hasimageflag = "1";
                                Picasso.with(CreateGroup.this).load(objects.getString("grpImage").toString())
                                        .placeholder(R.drawable.imageplaceholder)
                                        .into(iv_groupimg, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                //  progressbar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });
                            }
                        }




                        Log.d("TOUCHBASE", "COUNTRY ID--" + objects.getString("country").toString());
                        //   selectSpinnerValue(spinner_group_type, objects.getString("grpType").toString());
                        spinner_group_category.setSelection(getIndexofCategoery(objects.getString("grpCategory").toString()));
                        spinner_group_country.setSelection(getIndexofCountry(objects.getString("country").toString()));


                    }

                }

            } catch (Exception e) {
                Log.d("exec", "Exception :- " + e.toString());
            }

        }

    }

    public void get_AddressDetails(String result) {
        if (!getLatLong(getLocationInfo(result))) {
            latitude = 0.00;
            longitute = 0.00;
        } else {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitute, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("exec", "Exception:-" + e.toString());
            }

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            Log.d("TOUCHBASE", "ADDRESS :-" + address + " - " + city + " - " + state + " - " + country + " - " + postalCode);
        }
    }

    private void getresultOfRemovephoto(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {
                //Intent i = new Intent(Announcement_details.this,Announcement.class);
                //startActivityForResult(i,1);
                iv_groupimg.setImageResource(R.drawable.edit_image);
                hasimageflag = "0";
            } else {
                //Utils.showToastWithTitleAndContext(getApplicationContext(), "Failed to Remove Photo...");
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Photo Remove failed, Please try Again!");

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    public int getIndexofCategoery(String itemName) {
        for (int i = 0; i < listcategoery.size(); i++) {
            CategoryData cat = listcategoery.get(i);
            if (itemName.equals(cat.getCatId())) {
                return i;
            }
        }

        return -1;
    }

    public int getIndexofCountry(String itemName) {
        for (int i = 0; i < listcounty.size(); i++) {
            CountryData cat = listcounty.get(i);
            if (itemName.equals(cat.getCountryId())) {
                return i;
            }
        }

        return -1;
    }

    private void selectSpinnerValue(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {

                spinner.setSelection(i);
                break;
            }
        }
    }

    private void selectSpinnerValueCustome(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                Log.d("TOUCHBASE", "IN THE SPIENER");
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void selectImage() {


        final CharSequence[] options;

        if (hasimageflag.equals("0")) {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            hasimageflag = "1";
        } else {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateGroup.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {


                    String fileName = "temp.jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(intent, Capture_Camera);


                } else if (options[item].equals("Choose from Gallery")) {


                    Crop.pickImage(CreateGroup.this);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (options[item].equals("Remove Photo")) {
                    if (groupid.equals("0")) {
                        iv_groupimg.setImageResource(R.drawable.edit_image);

                    } else {
                        remove_photo_webservices();
                    }
                    //dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        } else if (requestCode == Capture_Camera && resultCode == RESULT_OK) {
            //----- Correct Image Rotation ----//
            Uri correctedUri = null;
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCapturedImageURI);

                //imageBitmap = imageOrientationValidator(imageBitmap, getRealPathFromURI(mCapturedImageURI));
                correctedUri = Utils.getImageUri(imageBitmap, getApplicationContext());
                Log.d("==== Uri ===", "======" + correctedUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            beginCrop(correctedUri);

        }
        if (requestCode == 5) {
            if (data != null) {
                String result = data.getStringExtra("address");
                Log.d("TOUCHBASE", "ADDRESS :-" + result);
                et_address1.setText(result);
                // et_address1.setFocusable(true);

            }
        }
    }

    public boolean getLatLong(JSONObject jsonObject) {

        try {
            Log.d("TOUCHBASE ", "JSON OBJECT-" + jsonObject.toString());
            longitute = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("TOUCHBASE ", "LAT-" + latitude + " LONG-" + longitute);
        } catch (JSONException e) {
            //Toast.makeText(CreateGroup.this, "Please Enter Valid Address", Toast.LENGTH_SHORT).show();
            Log.d("exec", "Exception getLatLong :- " + e.toString());
            return false;

        }

        return true;
    }

    public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //address = address.trim().replaceAll(" ", "+");
            Log.d("TouchBase","Address"+address);
            //address = address.trim().replaceAll(".", "");
            address = address.replaceAll("(\\r|\\n)", "");
            address = URLEncoder.encode(address.trim().replaceAll(" ", "+"), "UTF-8");

            Log.d("TouchBase","Address"+address);
            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            Log.d("TouchBase","Exec Client:-"+e.toString());
        } catch (IOException e) {
            Log.d("TouchBase","Exec IOException:-"+e.toString());
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("TouchBase", "Exec JSON:-" + e.toString());
        }

        return jsonObject;
    }

    /**************************************
     * Image Crop Methods
     *****************************************/

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            Uri croppedUri = Crop.getOutput(result);

            if (croppedUri != null) {
                ImageCompression imageCompression = new ImageCompression();
                String path = "";
                Bitmap csBitmap = null;
                try {
                    csBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);


                    path = Utils.getRealPathFromURI(croppedUri, getApplicationContext());
                    Log.d("==== Path ===", "======" + path);

                    imageCompression = new ImageCompression();
                    picturePath = imageCompression.compressImage(path, getApplicationContext());
                    Log.d("==picturePath====","0000...."+picturePath);

                    uploadedimgid = Utils.doFileUpload(new File(picturePath), "event"); // Upload File to server
                    Log.d("TOUCHBASE", "RESPONSE FILE UPLOAD " + uploadedimgid);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                updatedProfileImage = csBitmap;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
              //  csBitmap = Bitmap.createScaledBitmap(csBitmap, 500, 500, true);
              //  csBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
              //  iv_groupimg.setImageBitmap(csBitmap);
                updateProfileImage = true;

                iv_groupimg.setImageDrawable(Drawable.createFromPath(picturePath));
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);

    }

    /************************************************************************************************/
    public boolean validation() {

        if (et_grpname.getText().toString().trim().matches("") || et_grpname.getText().toString().trim() == null) {
            Toast.makeText(CreateGroup.this, "Please enter an Entity Name", Toast.LENGTH_LONG).show();
            return false;
        }

        if (et_email_address.getText().toString().trim().matches("") || et_email_address.getText().toString().trim() == null) {
            Toast.makeText(CreateGroup.this, "Please enter an Email Address", Toast.LENGTH_LONG).show();
            return false;
        }
        if (EMAIL_PATTERN.matcher(et_email_address.getText().toString()).matches() == false) {

            Toast.makeText(getApplicationContext(), "Enter Valid Email Address ", Toast.LENGTH_LONG).show();
            return false;

        }

        if (et_mobile.getText().toString().trim().matches("") || et_mobile.getText().toString().trim() == null) {
            Toast.makeText(CreateGroup.this, "Please enter a Mobile Number", Toast.LENGTH_LONG).show();
            return false;
        }
       /* if (selectedgrouptype.equals("")) {
            Toast.makeText(CreateGroup.this, "Please specify an Entity Nature", Toast.LENGTH_LONG).show();
            return false;
        }*/
        if (categoeryid.equals("")) {
            Toast.makeText(CreateGroup.this, "Please specify an Entity Category", Toast.LENGTH_LONG).show();
            return false;
        }
        if (categoeryid.equals("5")) {
            if (et_other.getText().toString().trim().matches("") || et_other.getText().toString().trim() == null) {
                Toast.makeText(CreateGroup.this, "Please specify a Other Categoty Name", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (countryid.equals("")) {
            Toast.makeText(CreateGroup.this, "Please specify an Entity Country", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        popupback();
        //
        // super.onBackPressed();
    }
}
