package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.Data.E_BulletineListData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 11-02-2016.
 */
public class E_BulletineAdapter extends ArrayAdapter<E_BulletineListData>{

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<E_BulletineListData> list_ebulletineData = new ArrayList<E_BulletineListData>();
    private ArrayList<E_BulletineListData> filterList = null;
    String link;
    private int pos;
    public static int count_read_ebulletines = 0;
    String ebulletinId;
    MarshMallowPermission marshMallowPermission;
    public E_BulletineAdapter(Context mContext, int layoutResourceId, ArrayList<E_BulletineListData> list_ebulletineData) {
        super(mContext, layoutResourceId, list_ebulletineData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_ebulletineData = list_ebulletineData;
        this.filterList=new ArrayList<>();
        this.filterList.addAll(list_ebulletineData);
        marshMallowPermission = new MarshMallowPermission((Activity)mContext);
    }

    public void setGridData(ArrayList<E_BulletineListData> list_ebulletineData) {
        this.list_ebulletineData = list_ebulletineData;
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
            holder.tv_EBulletine_name = (TextView) row.findViewById(R.id.tv_EBulletine_name);
            holder.tv_date = (TextView) row.findViewById(R.id.tv_date);
            holder.iv_delete = (ImageView) row.findViewById(R.id.iv_delete);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final E_BulletineListData item = list_ebulletineData.get(position);

        //pos = position;
        holder.iv_delete.setTag(position);
        holder.tv_EBulletine_name.setText(item.getEbulletinTitle());
        holder.tv_date.setText(item.getPublishDateTime());




        if (item.getIsRead().equalsIgnoreCase("no") && item.getFilterType().equals(Constant.FILTER_TYPE_PUBLISHED)) {
            holder.tv_EBulletine_name.setTextColor(mContext.getResources().getColor(R.color.bluecolor));
        } else {
            holder.tv_EBulletine_name.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ebulletinId = item.getEbulletinID();
                Log.e("========","=== E ID======"+ebulletinId);
                if(!list_ebulletineData.get(position).getIsRead().equalsIgnoreCase("Yes")) {
                    if(!list_ebulletineData.get(position).getFilterType().equalsIgnoreCase(Constant.FilterTypes.FILTER_TYPE_EXPIRED)) {
                        count_read_ebulletines++;
                    }
                }

                if (item.getEbulletinType().equals("Link")) {

                    String url = item.getEbulletinlink();
                    if(url!=null && !url.isEmpty()){
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(browserIntent);
                        readFlagWebservices();
                    }else {
                        Utils.showToastWithTitleAndContext(mContext,mContext.getString(R.string.noDoc));
                    }

                } else {
                    final String link = item.getEbulletinlink();
                    if(link!=null && !link.isEmpty()){
                        Uri u = Uri.parse(link);
                        File f = new File("" + u);
                        String fileName = f.getName();
                        Log.d("-----LINK---------", "-----Downloaded-----" + fileName);
                        String servicestring = Context.DOWNLOAD_SERVICE;
                        DownloadManager downloadmanager;
                        downloadmanager = (DownloadManager) mContext.getSystemService(servicestring);
                        Uri uri = Uri.parse(link);
                        Log.d("-----LINK---------", "-----Downloaded-----" + uri);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        Log.d("-----LINK---------", "-----Downloaded-----" + request);
                        //  Long reference = downloadmanager.enqueue(request);
                        // Log.d("-----LINK---------","-----Downloaded-----"+reference);

                        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                            marshMallowPermission.requestPermissionForExternalStorage();
                        } else {
                            long id = downloadmanager.enqueue(request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE).setAllowedOverRoaming(false).setTitle("File Downloading...").setDescription("File Download").setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName));
                            Log.d("-----LINK---------", "-----Downloaded-----" + id);
                            //documentsafeat6d89-cover23032016021718PM.png
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                            mContext.startActivity(browserIntent);
                            readFlagWebservices();
                        }
                    }else {
                        Utils.showToastWithTitleAndContext(mContext,mContext.getString(R.string.noDoc));
                    }

                }
            }
        });
        if (PreferenceManager.getPreference(mContext, PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            holder.iv_delete.setVisibility(View.GONE);
        } else {
            holder.iv_delete.setVisibility(View.VISIBLE);
        }
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Integer index = (Integer) v.getTag();
                Log.d("TOUCHBASE", "POSITION:-" + index);
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

                        if (InternetConnection.checkConnection(mContext)) {
                            //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
                            deletewebservices(item.getEbulletinID());

                          //  list_ebulletineData.remove(pos);
                          //  notifyDataSetChanged();


                            list_ebulletineData.remove(index.intValue());
                            notifyDataSetChanged();

                            dialog.dismiss();
                        } else {
                            Utils.showToastWithTitleAndContext(mContext, "No Internet Connection !!!");
                            dialog.dismiss();

                        }
                    }
                });

                dialog.show();
            }

        });


        return row;


        // return super.getView(position, convertView, parent);
    }

    static class ViewHolder {
        TextView tv_EBulletine_name, tv_date;
        ImageView iv_delete;

    }

    private void deletewebservices(String id) {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", id));
        arrayList.add(new BasicNameValuePair("type", "Ebulletin"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("profileID", PreferenceManager.getPreference(mContext, PreferenceManager.GRP_PROFILE_ID)));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteByModuleName + " :- " + arrayList.toString());
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
              /*  list_ebulletineData.remove(pos);
                notifyDataSetChanged();*/
                Utils.showToastWithTitleAndContext(mContext, "Deleted Successfully");
            } else {
                Utils.showToastWithTitleAndContext(mContext, "Failed to Delete...");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    //=========================== Read Flag Webservice ==========================

    private void readFlagWebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("ebulletinID", ebulletinId));
        arrayList.add(new BasicNameValuePair("memberProfileID", PreferenceManager.getPreference(mContext, PreferenceManager.GRP_PROFILE_ID)));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.GetReadFlag + " :- " + arrayList.toString());
        new WebConnectionOfReadFlag(Constant.GetReadFlag, arrayList, mContext).execute();
    }

    public class WebConnectionOfReadFlag extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(mContext, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public WebConnectionOfReadFlag(String url, List<NameValuePair> argList, Context ctx) {
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

                getDataReadFlag(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getDataReadFlag(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBEbulletinListResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                //String msg = ActivityResult.getString("message");

                //Toast.makeText(mContext, "Read Successfully", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    // Filter Class
    public void filter(String charText) {
        charText=charText.toLowerCase();
        list_ebulletineData.clear();
        if (charText.length() == 0) {
            list_ebulletineData.addAll(filterList);
        }
        else
        {
            for (E_BulletineListData wp : filterList)
            {
                Utils.log(wp.getEbulletinTitle());
                if (wp.getEbulletinTitle().toLowerCase().contains(charText))
                {
                    list_ebulletineData.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
