package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.SampleApp.row.Adapter.SelectCountryAdapter;
import com.SampleApp.row.Data.CountryData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 16-12-2015.
 */
public class SelectCountry extends Activity {

    ListView listview;
    ArrayAdapter<String> adapter;
  //  final String[] announcements = new String[]{"aaa", "bbb", "ccc", "ddd", "eee", "fff", "aaa", "bbb", "ccc", "ddd", "eee", "fff", "aaa", "bbb", "ccc", "ddd", "eee", "fff", "aaa", "bbb", "ccc", "ddd", "eee", "fff"};
    RadioButton radio;
    TextView tv_continue;
    TextView tv_title;
    ImageView iv_backbutton;
    EditText et_serach_country;


    private ArrayList<CountryData> list_countryData = new ArrayList<CountryData>();
    private SelectCountryAdapter adapter_countryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_country);

        listview = (ListView) findViewById(R.id.listView);
        tv_continue = (TextView) findViewById(R.id.tv_continue);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        et_serach_country = (EditText)findViewById(R.id.et_serach_country);
        iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Select Country");


      //  adapter = new ArrayAdapter<String>(this, R.layout.select_country_item, R.id.tv_lang, announcements);
       // listview.setAdapter(adapter);

        webservices_getdata();

        init();

    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();


    }

    private void init() {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent i = getIntent();
                i.putExtra("selectedValue", list_countryData.get(position).getCountryName());
                i.putExtra("countryCode", list_countryData.get(position).getCountryCode());
                i.putExtra("countryid", list_countryData.get(position).getCountryId());

                setResult(RESULT_OK, i);
                finish();
            }
        });

        et_serach_country.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //adapter.getFilter().filter(cs);
                //adapter.getFilter().filter(cs.toString());

                int textlength = cs.length();
                ArrayList<CountryData> tempArrayList = new ArrayList<CountryData>();
                for (CountryData c : list_countryData) {
                    if (textlength <= c.getCountryName().length()) {
                        if (c.getCountryName().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                //Data_array= tempArrayList;
                //   DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);

                SelectCountryAdapter adapter = new SelectCountryAdapter(SelectCountry.this, R.layout.select_country_item, tempArrayList);
                listview.setTextFilterEnabled(true);
                listview.setAdapter(adapter);


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

    }
    public void finishActivity(View v) {
        finish();
    }

    private void webservices_getdata() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        //arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));

        Log.d("Response", "PARAMETERS " + Constant.GetAllCountriesAndCategories + " :- " + arrayList.toString());
        new WebConnectionAsync(Constant.GetAllCountriesAndCategories, arrayList, SelectCountry.this).execute();
    }


    public class WebConnectionAsync extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(SelectCountry.this, R.style.TBProgressBar);

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
                getresult(result.toString());

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
                    for (int i = 0; i < grpsarray.length(); i++) {
                        JSONObject object = grpsarray.getJSONObject(i);
                        JSONObject objects = object.getJSONObject("GrpCountryList");

                        CountryData gd = new CountryData();
                        gd.setCountryId(objects.getString("countryId").toString());
                        gd.setCountryCode(objects.getString("countryCode").toString());
                       gd.setCountryName(objects.getString("countryName").toString());

                        list_countryData.add(gd);

                    }

                    adapter_countryData = new SelectCountryAdapter(SelectCountry.this, R.layout.select_country_item, list_countryData);
                    listview.setAdapter(adapter_countryData);

                }

            } catch (Exception e) {
                Log.d("exec", "Exception :- " + e.toString());
            }

        }

    }

}
