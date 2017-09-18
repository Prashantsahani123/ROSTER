package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.SampleApp.row.AddFamilyDetailToProfile;
import com.SampleApp.row.Data.FamilyData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by USER on 01-04-2016.
 */
public class FamilyDetailsAdapter extends ArrayAdapter<FamilyData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<FamilyData> profileListDatas = new ArrayList<FamilyData>();
    private int lastFocussedPosition = -1;
    private Handler handler = new Handler();
    String memberprofileid;
    private int pos;
    private int deletePosition = -1;
    public FamilyDetailsAdapter(Context mContext, int layoutResourceId, ArrayList<FamilyData> profileListDatas, String memberprofileid) {
        super(mContext, layoutResourceId, profileListDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.profileListDatas = profileListDatas;
        this.memberprofileid = memberprofileid;
    }

    public void setGridData(ArrayList<FamilyData> profileListDatas) {
        this.profileListDatas = profileListDatas;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();


            holder.tv_name = (TextView) row.findViewById(R.id.tv_name);
            holder.tv_email = (TextView) row.findViewById(R.id.tv_email);
            holder.tv_dob = (TextView) row.findViewById(R.id.tv_dob);
            holder.tv_mobile = (TextView) row.findViewById(R.id.tv_mobile);
            holder.tv_boodgrp = (TextView) row.findViewById(R.id.tv_boodgrp);
            holder.tv_relation = (TextView) row.findViewById(R.id.tv_relation);

            holder.iv_edit = (ImageView) row.findViewById(R.id.iv_edit);
            holder.iv_delete = (ImageView) row.findViewById(R.id.iv_delete);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final FamilyData item = profileListDatas.get(position);

        pos = position;
        holder.tv_name.setText(item.getMemberName());
        holder.tv_email.setText(item.getEmailID());
        holder.tv_dob.setText(item.getdOB());
        holder.tv_mobile.setText(item.getContactNo());
        holder.tv_boodgrp.setText(item.getBloodGroup());
        holder.tv_relation.setText(item.getRelationship());


        //ADMIN HIDE
        if (PreferenceManager.getPreference(mContext, PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            holder.iv_edit.setVisibility(View.GONE);
            holder.iv_delete.setVisibility(View.GONE);
            if (PreferenceManager.getPreference(mContext, PreferenceManager.GRP_PROFILE_ID).equals(memberprofileid)) {
                holder.iv_edit.setVisibility(View.VISIBLE);
                holder.iv_delete.setVisibility(View.VISIBLE);
            }

        } else {
            holder.iv_edit.setVisibility(View.VISIBLE);
            holder.iv_delete.setVisibility(View.VISIBLE);

        }
        //ADMIN HIDE

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        deletePosition = position;
                        if (InternetConnection.checkConnection(mContext)) {
                            //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
                            deletewebservices(item.getFamilyMemberId());
                            dialog.dismiss();
                        } else {
                            Utils.showToastWithTitleAndContext(mContext, "No Internet Connection!");
                            dialog.dismiss();

                        }
                    }
                });

                tv_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, AddFamilyDetailToProfile.class);
                i.putExtra("memberprofileid", memberprofileid);
                i.putExtra("Editing", item);
                ((Activity) mContext).startActivityForResult(i, 3);

            }
        });
        return row;

    }

    static class ViewHolder {
        TextView tv_name, tv_email, tv_dob, tv_mobile, tv_boodgrp, tv_relation;
        ImageView iv_edit, iv_delete;


    }

    public void datepicker(final TextView setdatetext, final int pos) {
        // Get Current Date
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        setdatetext.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
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

    private void deletewebservices(String id) {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", id));
        arrayList.add(new BasicNameValuePair("type", "FamilyMember"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("profileID", PreferenceManager.getPreference(mContext, PreferenceManager.GRP_PROFILE_ID)));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "♦♦♦♦PARAMETERS " + Constant.DeleteByModuleName + " :- " + arrayList.toString());
        new WebConnectionEventDetail(Constant.DeleteByModuleName, arrayList, mContext).execute();
    }

    public class WebConnectionEventDetail extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(mContext, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public WebConnectionEventDetail(String url, List<NameValuePair> argList, Context ctx) {
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

                getdata(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getdata(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {
               /* //Intent i = new Intent(Announcement_details.this,Announcement.class);
                //startActivityForResult(i,1);
                Intent intent = new Intent();
                mContext.setResult(1, intent);
                finish();//finishing activity*/
                Log.e("TouchBase", "♦♦♦♦Position of deleting data : "+pos);
                profileListDatas.remove(deletePosition);
                notifyDataSetChanged();

            } else {
                Utils.showToastWithTitleAndContext(mContext, "Failed to DELETE, please Try Again!");
            }

        } catch (Exception e) {
            Log.e("TouchBase", "♦♦♦♦Error is : " + e.toString());
            e.printStackTrace();
        }
    }
}