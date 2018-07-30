package com.SampleApp.row;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Data.FamilyData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by user on 30-03-2016.
 */
public class AddFamilyDetailToProfile extends Activity {


    TextView tv_select_country, tv_country_code, tv_spinner1_textview, tv_spinner2_textview;
    private int mRequestCode = 100;
    TextView tv_title, tv_birthdate, tv_add, tv_cancel;
    private Spinner spinner1, spinner2;
    EditText name, et_email, et_mobile;
    String flag_click = "0";
    ImageView iv_backbutton;
    String relation, blood;
    private String memberprofileid;
    private String familyMemberId = "0";
    FamilyData model;
    private String moduleName = "";
    // final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9 ]{2,25}");
    final Pattern MOBILE_PATTERN = Pattern.compile("[0-9]{3,20}");

    final Pattern EMAIL_PATTERN = Pattern.compile( "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_family_detail);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Family");
        tv_select_country = (TextView) findViewById(R.id.tv_select_country);
        tv_country_code = (TextView) findViewById(R.id.tv_country_code);
        tv_birthdate = (TextView) findViewById(R.id.tv_birthdate);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        name = (EditText) findViewById(R.id.name);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_email = (EditText) findViewById(R.id.et_email);
        tv_spinner1_textview = (TextView) findViewById(R.id.tv_spinner1_textview);
        tv_spinner2_textview = (TextView) findViewById(R.id.tv_spinner2_textview);

        tv_country_code.setTag("1");


        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            memberprofileid = intent.getString("memberprofileid");

            Intent i = getIntent();
            if (i.hasExtra("Editing")) {
                model = (FamilyData) getIntent().getSerializableExtra("Editing");
                Log.d("TOUCHBASE", "MODEL ---" + model.toString());
                familyMemberId = model.getFamilyMemberId();
                name.setText(model.getMemberName());
                et_email.setText(model.getEmailID());
                // et_mobile.setText(model.getContactNo());
                if (model.getdOB().equals("") || model.getdOB().equals(null) || model.getdOB().isEmpty()) {
                    tv_birthdate.setText("");
                } else {

                    tv_birthdate.setText(date_formatter(model.getdOB()));
                }
                tv_spinner1_textview.setText(model.getRelationship());
                // Log.d("*********", ".............." + tv_spinner1_textview.getText().toString());
                tv_spinner2_textview.setText(model.getBloodGroup());


                // --SPIT TO COUNTRY CODE
                Log.d("TOUCHBAASE", " SPLIT ---" + model.getContactNo());
                String[] separated_eventDate = model.getContactNo().trim().split(" ");
                Log.d("TOUCHBAASE", " SPLIT ---" + separated_eventDate.length + "--" + separated_eventDate[0]);
                if (separated_eventDate.length == 1) {
                    et_mobile.setText(separated_eventDate[0]);
                } else if (separated_eventDate.length == 2) {
                    et_mobile.setText(separated_eventDate[1]);
                    tv_country_code.setText(separated_eventDate[0]);
                }
                // --SPIT TO COUNTRY CODE

            }
        }
        init();

    }

    private void init() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_country_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
                    Intent i = new Intent(AddFamilyDetailToProfile.this, SelectCountry.class);
                    startActivityForResult(i, mRequestCode);

                    //webservices_update();
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }


            }
        });



        tv_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker(tv_birthdate);
            }
        });


        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //   Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();

            }
        });

        tv_spinner1_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(AddFamilyDetailToProfile.this, tv_spinner1_textview);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.relation, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(AddFamilyDetailToProfile.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        tv_spinner1_textview.setText(item.getTitle());
                        return true;
                    }
                });

                popup.show();

            }
        });

        tv_spinner2_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(AddFamilyDetailToProfile.this, tv_spinner1_textview);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.blood_group, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(AddFamilyDetailToProfile.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        tv_spinner2_textview.setText(item.getTitle());
                        return true;
                    }
                });

                popup.show();

            }
        });

    }


    public void datepicker(final TextView setdatetext) {
        // Get Current Date
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        setdatetext.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode && resultCode == RESULT_OK) {


            //tv_country_code.setText(data.getStringExtra("selectedValue"));
            tv_country_code.setText(data.getStringExtra("countryCode"));
            tv_country_code.setTag(data.getStringExtra("countryid"));

        }
    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new BasicNameValuePair("familyMemberId", familyMemberId));
        arrayList.add(new BasicNameValuePair("memberName", name.getText().toString()));
        arrayList.add(new BasicNameValuePair("relationship", tv_spinner1_textview.getText().toString()));
        arrayList.add(new BasicNameValuePair("dOB", tv_birthdate.getText().toString()));
        arrayList.add(new BasicNameValuePair("anniversary", ""));
        arrayList.add(new BasicNameValuePair("emailId", et_email.getText().toString()));
        if(et_mobile.getText().toString().trim().length() > 0) {
            arrayList.add(new BasicNameValuePair("contactNo", tv_country_code.getText().toString() + " " + et_mobile.getText().toString()));
        }else{
            arrayList.add(new BasicNameValuePair("contactNo", ""));
        }
        arrayList.add(new BasicNameValuePair("particulars", ""));
        arrayList.add(new BasicNameValuePair("bloodGroup", tv_spinner2_textview.getText().toString()));
        arrayList.add(new BasicNameValuePair("profileId", memberprofileid));

        Log.d("Response", "PARAMETERS " + Constant.UpdateFamilyDetails + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.UpdateFamilyDetails, arrayList, AddFamilyDetailToProfile.this).execute();
    }

    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(AddFamilyDetailToProfile.this, R.style.TBProgressBar);

        public WebConnectionAsyncLogin(String url, List<NameValuePair> argList, Context ctx) {
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
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {
                getresult(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("UpdateFamilyResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");


                Intent intent = new Intent();
                // intent.putExtra("MESSAGE",message);
                setResult(3, intent);

                finish();


            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    public boolean validation() {

        if (name.getText().toString().trim().matches("") || name.getText().toString().trim() == null) {
            Toast.makeText(AddFamilyDetailToProfile.this, "Please enter a name", Toast.LENGTH_LONG).show();
            return false;
        }

      /*  if (et_email.getText().toString().trim().matches("") || et_email.getText().toString().trim() == null) {
            Toast.makeText(AddFamilyDetailToProfile.this, "Please enter an Email Address", Toast.LENGTH_LONG).show();
            return false;
        }*/

        if(et_email.getText().toString().length() != 0) {
            if (EMAIL_PATTERN.matcher(et_email.getText().toString()).matches() == false) {
                Toast.makeText(getApplicationContext(), "Please enter a valid Email Address", Toast.LENGTH_LONG).show();
                return false;
            }
            else{

                //return true;
            }
        }

        if (tv_spinner1_textview.getText().toString().trim().matches("") || tv_spinner1_textview.getText().toString().trim() == null) {
            Toast.makeText(AddFamilyDetailToProfile.this, "Please specify the Relation", Toast.LENGTH_LONG).show();
            return false;
        }

        if(et_mobile.getText().toString().length()!=0 )
        {
            if(MOBILE_PATTERN.matcher(et_mobile.getText().toString()).matches()==false){

                Toast.makeText(getApplicationContext(), "Please enter a valid mobile number", Toast.LENGTH_LONG).show();
                return false;

            }else{

                //return true;
            }
        }
      /*  if (tv_birthdate.getText().toString().trim().matches("") || tv_birthdate.getText().toString().trim() == null) {
            Toast.makeText(AddFamilyDetailToProfile.this, "Please enter a Birth Date", Toast.LENGTH_LONG).show();
            return false;
        }

      //  String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        if (!(compare_date(todayDate.toString(),tv_birthdate.getText().toString())).equals("1")) {
            Toast.makeText(AddFamilyDetailToProfile.this, "Enter Valid Birth date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (tv_spinner2_textview.getText().toString().trim().matches("") || tv_spinner2_textview.getText().toString().trim() == null) {
            Toast.makeText(AddFamilyDetailToProfile.this, "Please specify the  blood group", Toast.LENGTH_LONG).show();
            return false;
        }*/

        return true;
    }

    public String date_formatter(String inputdate) {
        //String date = "2011/11/12";
        String date = inputdate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..." + newFormat);
        return newFormat;
    }
}
