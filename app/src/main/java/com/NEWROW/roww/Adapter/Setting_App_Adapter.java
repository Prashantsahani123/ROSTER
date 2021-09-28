package com.NEWROW.row.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Data.SettingsData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.PreferenceManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 05-04-2016.
 */
public class Setting_App_Adapter  extends ArrayAdapter<SettingsData> {

    private Context mContext;
    private ArrayList<SettingsData> settinglist;
    private int layoutResourceId;


    public Setting_App_Adapter(Context mContext, int layoutResourceId, ArrayList<SettingsData> settinglist) {
        super(mContext, layoutResourceId, settinglist);
        this.settinglist = settinglist;
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;


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
            @Override
            public void onClick(View v) {
                String flagvalue = "0";
                if (finalHolder.iv_toggle_on.getDrawable().getConstantState() == mContext.getResources().getDrawable(R.drawable.on_toggle_btn).getConstantState()) {
                    finalHolder.iv_toggle_on.setImageResource(R.drawable.off_toggle_btn);
                    flagvalue = "0";
                    settinglist.get(position).setGrpVal("0");
                } else {
                    finalHolder.iv_toggle_on.setImageResource(R.drawable.on_toggle_btn);
                    flagvalue = "1";
                    settinglist.get(position).setGrpVal("0");
                }

                webservice(item.getGrpId().toString(), flagvalue);
            }
        });


        return convertView;
    }

    private void webservice(String selectecvalue, String flagvalue) {
        //{"groupProfileID":"43","grpId":"74","Type":"0","Admin":"0","searchText":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("mainMasterId", PreferenceManager.getPreference(mContext, PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("GroupId", selectecvalue));
        arrayList.add(new BasicNameValuePair("UpdatedValue", flagvalue));

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
            JSONObject EventResult = jsonObj.getJSONObject("TBSettingResult");
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


