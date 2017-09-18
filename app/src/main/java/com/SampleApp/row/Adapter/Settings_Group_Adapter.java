package com.SampleApp.row.Adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.SampleApp.row.Data.SettingsData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.PreferenceManager;

/**
 * Created by user on 14-03-2016.
 */
public class Settings_Group_Adapter extends ArrayAdapter<SettingsData> {

    private Context mContext;
    private ArrayList<SettingsData> settinglist;
    private int layoutResourceId;
    private String callflag; //1- Touchbase Settings 2- GroupSettings
    String phone_myclub,phone_allclub,email_myclub,email_allclub = "";


    public Settings_Group_Adapter(Context mContext, int layoutResourceId, ArrayList<SettingsData> settinglist, String callfalg,String phone_myclub,String phone_allclub,String email_myclub,String email_allclub ) {
        super(mContext, layoutResourceId, settinglist);
        this.settinglist = settinglist;
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.callflag = callfalg;
        this.phone_myclub = phone_myclub;
        this.phone_allclub = phone_allclub;
        this.email_myclub = email_myclub;
        this.email_allclub = email_allclub;


    }

    private class ViewHolder {
        TextView tv_group_name;
        ImageView iv_toggle_on;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        convertView = null;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            holder.iv_toggle_on = (ImageView) convertView.findViewById(R.id.iv_toggle_on);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SettingsData item = settinglist.get(position);

        holder.tv_group_name.setText(item.getGrpName());

        if (item.getGrpVal().equals("1")) {
            holder.iv_toggle_on.setImageResource(R.drawable.on_toggle_btn);
        } else {
            holder.iv_toggle_on.setImageResource(R.drawable.off_toggle_btn);
        }

        final ViewHolder finalHolder = holder;
        holder.iv_toggle_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String flagvalue = "0";

                if(item.getGrpVal().equals("1")){
                    finalHolder.iv_toggle_on.setImageResource(R.drawable.off_toggle_btn);
                    flagvalue = "0";
                    settinglist.get(position).setGrpVal("0");
                }else {
                    finalHolder.iv_toggle_on.setImageResource(R.drawable.on_toggle_btn);
                    flagvalue = "1";
                    settinglist.get(position).setGrpVal("1");
                }
                if (callflag.equals("1")) {
                    webserviceTouchbaseSettings(item.getGrpId().toString(), flagvalue);
                } else {
                    webservice(item.getGrpId().toString(), flagvalue);
                }
                /*
                if (finalHolder.iv_toggle_on.getDrawable().getConstantState().equals(mContext.getResources().getDrawable(R.drawable.on_toggle_btn).getConstantState())) {
                    finalHolder.iv_toggle_on.setImageResource(R.drawable.off_toggle_btn);
                    flagvalue = "0";
                    settinglist.get(position).setGrpVal("0");
                } else {
                    finalHolder.iv_toggle_on.setImageResource(R.drawable.on_toggle_btn);
                    flagvalue = "1";
                    settinglist.get(position).setGrpVal("1");
                }
                if (callflag.equals("1")) {
                    webserviceTouchbaseSettings(item.getGrpId().toString(), flagvalue);
                } else {
                    webservice(item.getGrpId().toString(), flagvalue);
                }*/
            }
        });


        return convertView;
    }


    //{"GroupId":"","UpdatedValue":"","mainMasterId":""}

    private void webservice(String selectecvalue, String flagvalue) {
        //{"groupProfileID":"43","grpId":"74","Type":"0","Admin":"0","searchText":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("GroupId", PreferenceManager.getPreference(mContext, PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("GroupProfileId", PreferenceManager.getPreference(mContext, PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("ModuleId", selectecvalue));
        arrayList.add(new BasicNameValuePair("UpdatedValue", flagvalue));
        arrayList.add(new BasicNameValuePair("showMobileSeflfClub",phone_myclub));
        arrayList.add(new BasicNameValuePair("showMobileOutsideClub",phone_allclub));
        arrayList.add(new BasicNameValuePair("showEmailSeflfClub", email_myclub));
        arrayList.add(new BasicNameValuePair("showEmailOutsideClub", email_allclub));


//        arrayList.add(new BasicNameValuePair("isMob", "9")); // 9 is scrap value as no validation is applied in webservce
//        arrayList.add(new BasicNameValuePair("isEmail", "9"));
//        arrayList.add(new BasicNameValuePair("isPersonal", "9"));
//        arrayList.add(new BasicNameValuePair("isFamily", "9"));
//        arrayList.add(new BasicNameValuePair("isBusiness", "9"));

        Log.d("Response", "PARAMETERS " + Constant.GroupSetting + " :- " + arrayList.toString());
        new WebConnectionAsyncDirectory(Constant.GroupSetting, arrayList, mContext).execute();
    }


    private void webserviceTouchbaseSettings(String selectecvalue, String flagvalue) {
        //{"groupProfileID":"43","grpId":"74","Type":"0","Admin":"0","searchText":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("GroupId", selectecvalue));
        arrayList.add(new BasicNameValuePair("UpdatedValue", flagvalue));
        arrayList.add(new BasicNameValuePair("mainMasterId", PreferenceManager.getPreference(mContext, PreferenceManager.MASTER_USER_ID))); // 9 is scrap value as no validation is applied in webservce


        Log.d("Response", "PARAMETERS " + Constant.TouchbaseSetting + " :- " + arrayList.toString());
        new WebConnectionAsyncDirectory(Constant.TouchbaseSetting, arrayList, mContext).execute();
    }


    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(mContext, R.style.TBProgressBar);
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
                // eventListDatas.clear();
                getresult(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject EventResult = jsonObj.getJSONObject("TBGroupSettingResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {

                Toast.makeText(mContext, "Settings updated Successfully!", Toast.LENGTH_LONG).show();

            } else {


            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());

        }
    }

}

