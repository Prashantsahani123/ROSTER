package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
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
import android.widget.Toast;

import com.SampleApp.row.Data.DocumentListData;
import com.SampleApp.row.PDFViewActivity;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 22-03-2016.
 */
public class DocumentAdapter extends ArrayAdapter<DocumentListData> {

    public static String MODE_VIEW = "VIEW", MODE_DOWNLOAD = "DOWNLOAD";
    public static int OPEN_FILE_REQUEST = 11;
    private String filePath = "";
    int fileOpenCounter = 0;
    DocumentListData item;
    private static String mode = "";
    private static int count = 0;
    DownloadManager downloadmanager;
    FileDownloadReceiver fileReceiver;
    ProgressDialog progress;
    Tracker tracker;
    private long enqueId;
    private Context mContext;
    private int layoutResourceId;
    public static int count_read_documents = 0;
    private ArrayList<DocumentListData> list_documentData = new ArrayList<DocumentListData>();
    private int pos;
    String isAdmin;
    File internalPath;
    String docID,accessType;
    //private String fileName;
    private String filenameArray[];
    private String extension;
    public DocumentAdapter(Context mContext, int layoutResourceId, ArrayList<DocumentListData> list_documentData, String isAdmin) {
        super(mContext, layoutResourceId, list_documentData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_documentData = list_documentData;
        this.isAdmin = isAdmin;
        fileReceiver = new FileDownloadReceiver();
        this.mContext.registerReceiver(fileReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        downloadmanager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void setGridData(ArrayList<DocumentListData> list_documentData) {
        this.list_documentData = list_documentData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.tv_title = (TextView) row.findViewById(R.id.tv_title);
            holder.tv_date = (TextView) row.findViewById(R.id.tv_date);
            holder.iv_delete = (ImageView) row.findViewById(R.id.iv_delete);
            holder.iv_download = (ImageView) row.findViewById(R.id.iv_download);
            holder.ivView = (ImageView)row.findViewById(R.id.iv_view);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        if(isAdmin.equals("No")){
            holder.iv_delete.setVisibility(View.GONE);
        }else{
            holder.iv_delete.setVisibility(View.VISIBLE);
        }

        item = list_documentData.get(position);
        pos=position;

        holder.iv_delete.setTag(position);
        holder.ivView.setTag(position);
        holder.iv_download.setTag(position);

        holder.tv_title.setText(item.getDocTitle());
        holder.tv_date.setText(item.getCreateDateTime());

        accessType = item.getAccessType();

        if(accessType.equals("0"))
        {
            holder.iv_download.setVisibility(View.GONE);
        }
        else {
            holder.iv_download.setVisibility(View.VISIBLE);
            holder.iv_download.setTag(position);
        }
        if(accessType.equals("1")) {
            holder.ivView.setVisibility(View.GONE);
        } else
             holder.ivView.setVisibility(View.VISIBLE);


        if (item.getIsRead().equals("1")) {
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.black));
        } else {
            holder.tv_title.setTextColor(mContext.getResources().getColor(R.color.bluecolor));
        }

        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                int myPosition = ((Integer)v.getTag()).intValue();
                DocumentListData data = list_documentData.get(myPosition);
                Log.e("Touchbase", "♦♦♦♦Document data : "+data);
                Log.e("Touchbase", "♦♦♦♦Position of clicked doc : "+myPosition);
                mode = MODE_DOWNLOAD;

                docID = data.getDocID();
                readFlagWebservices();
                if(data.getIsRead().equals("0")) {
                    count_read_documents++;
                }


                MarshMallowPermission marshMallowPermission = new MarshMallowPermission((Activity) mContext);
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                    return;
                }
                // selectImage();
                if ( ! InternetConnection.checkConnection(mContext)) {
                    Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (data.getDocURL().equals("")) {
                    Toast.makeText(mContext, "Download Link not found.", Toast.LENGTH_SHORT).show();
                    return;
                }
                progress = ProgressDialog.show(mContext, "Downloading",
                "File Downloading", true);


                final String link = data.getDocURL();
                Uri u = Uri.parse(link);
                File f = new File("" + u);
                String fileName = f.getName();

                if (fileExists(fileName)) {
                    progress.dismiss();
                    Toast.makeText(mContext, "File is already downloaded", Toast.LENGTH_LONG).show();
                    String filenameArray[] = fileName.split("\\.");
                    String extension = filenameArray[filenameArray.length - 1];

                    File sdcard = Environment.getExternalStorageDirectory();
                    File myfile = new File(sdcard.getPath()+"/Touchbase", fileName);
                    Log.d("***********", "-----" + extension);
                    String pdf = "pdf";
                    if (extension.equals(pdf)) {
                        Intent i = new Intent(mContext, PDFViewActivity.class);

                        i.putExtra("fileName", myfile.getPath());
                        i.putExtra("mode", mode);
                        i.putExtra("filePath", filePath);
                        Log.e("TouchBase", "♦♦♦♦File Path : " + filePath + " Mode : " + mode);
                        mContext.startActivity(i);
                    }
                } else {
                    filePath = f.getPath();
                    Log.d("-----FILE NAME---------", "-----Downloaded-----" + fileName);

                    File myDirectory = new File(Environment.getExternalStorageDirectory(), "/Touchbase");
                    String path = myDirectory.getAbsolutePath();
                    Log.e("===Directory PaTH===", "=====" + path);

                    if (!myDirectory.exists()) {
                        myDirectory.mkdirs();
                    }

                    downloadmanager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(link);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    enqueId = downloadmanager.enqueue(
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                    .setAllowedOverRoaming(false).setTitle(fileName)
                                    .setDescription("File Downloading")
                                    .setDestinationInExternalPublicDir("/Touchbase", fileName));
                    Log.e("TouchBase", "♦♦♦♦Enque ID : " + enqueId);
                }
            }
        });


        holder.ivView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                count = 0;
                mode = MODE_VIEW;
                int myPosition = ((Integer)v.getTag()).intValue();
                DocumentListData data = list_documentData.get(myPosition);
                Log.e("Touchbase", "♦♦♦♦Document Info : " + data);
                Log.e("Touchbase", "♦♦♦♦Position of clicked doc : "+position);
                docID = data.getDocID();
                readFlagWebservices();
                if(data.getIsRead().equals("0")) {
                    count_read_documents++;
                }

                MarshMallowPermission marshMallowPermission = new MarshMallowPermission((Activity) mContext);
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                    return;
                }
                // selectImage();
                if ( ! InternetConnection.checkConnection(mContext)) {
                    Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (data.getDocURL().equals("")) {
                    Toast.makeText(mContext, "Download Link not found.", Toast.LENGTH_SHORT).show();
                    return;
                }


                progress = ProgressDialog.show(mContext, "Loading", "Loading file", true);

                final String link = data.getDocURL();
                Uri u = Uri.parse(link);
                File f = new File("" + u);
                String fileName = f.getName();
                filePath = f.getPath();
                Log.d("-----FILE NAME---------", "-----Downloaded-----" + fileName);

                File myDirectory = new File(Environment.getExternalStorageDirectory(), "/Touchbase");
                String path = myDirectory.getAbsolutePath();
                Log.e("===Directory PaTH===", "=====" + path);

                if (!myDirectory.exists()) {
                    myDirectory.mkdirs();
                }

                downloadmanager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(link);
                // As file is taped for open purpose only, saving it in app directory. So that it will not be visible in file explorer of the system
                File appFolder = mContext.getFilesDir();
                File downloadFile = new File(appFolder, fileName);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                enqueId = downloadmanager.enqueue(
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle(fileName)
                    .setDescription("File Downloading")
                    //.setDestinationUri(Uri.fromFile(downloadFile)));
                    .setDestinationInExternalPublicDir("/Touchbase", fileName));
                Log.e("TouchBase", "♦♦♦♦Enque ID : " + enqueId);
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentListData data = list_documentData.get(position);
                final Integer index = (Integer) v.getTag();
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
                            deletewebservices(data.getDocID());
                            list_documentData.remove(index.intValue());
                            notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            Utils.showToastWithTitleAndContext(mContext, "No Internet Connection!");
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        //  holder.tv_date.setText(item.get);
        //  holder.tv_time.setText(item.get);

        return row;

        // return super.getView(position, convertView, parent);
    }

    static class ViewHolder {
        TextView tv_title, tv_date;
        ImageView iv_delete, iv_download,ivView;

    }

    // Checking whether the file which is to be downloaded is already there in filesystem.
    public boolean fileExists(String fileName) {
        File sdcard = Environment.getExternalStorageDirectory();
        File myfile = new File(sdcard.getPath()+"/Touchbase", fileName);
        return myfile.exists();
    }
    private void deletewebservices(String id) {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", id));
        arrayList.add(new BasicNameValuePair("type", "Document"));
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
            if (status.equals("0"))
            {
               /* //Intent i = new Intent(Announcement_details.this,Announcement.class);
                //startActivityForResult(i,1);
                Intent intent = new Intent();
                mContext.setResult(1, intent);
                finish();//finishing activity*/
            /*    Log.d("TEST","notifyDataSetChanfge"+pos);
                list_documentData.remove(pos);
                notifyDataSetChanged();*/

            }else{
                Utils.showToastWithTitleAndContext(mContext, "Failed to DELETE, please Try Again!");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private String getIntentDataAndType(String filePath) {
        String exten = "";
        int i = filePath.lastIndexOf('.');
        // If the index position is greater than zero then get the substring.
        if (i > 0) {
            exten = filePath.substring(i + 1);
        }
        String mimeType = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(exten);
        mimeType = (mimeType == null) ? "*/*" : mimeType;
        return mimeType;
    }

    //=========================== Read Flag Webservice ==========================

    private void readFlagWebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("docID", docID));
        arrayList.add(new BasicNameValuePair("memberProfileID", PreferenceManager.getPreference(mContext, PreferenceManager.GRP_PROFILE_ID)));
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.UpdateDocumentIsRead + " :- " + arrayList.toString());
        new WebConnectionOfReadFlag(Constant.UpdateDocumentIsRead, arrayList, mContext).execute();
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
            Log.d("TouchBase","♦♦♦♦Count Update Result : "+ result.toString());

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
            JSONObject ActivityResult = jsonObj.getJSONObject("TBDocumentUpdateResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");

                //Toast.makeText(mContext, "Flag updated successfully", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    public class FileDownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (  progress != null ) progress.dismiss();
            if ( count > 0 ) return;

            count++;
            String action = intent.getAction();



            if ( mode.equals(MODE_DOWNLOAD)) {
                Toast.makeText(context, "File downloaded successfully", Toast.LENGTH_LONG).show();
                Log.e("Touchbase", "♦♦♦♦Mode : " + mode);
            }

            Log.e("TouchBase", "♦♦♦♦Inside receiver");
            if(action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE )){
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueId);
                Cursor cur = downloadmanager.query(query);
                //DownloadManager.COLUMN_LOCAL_FILENAME
                if ( cur.moveToNext()) {
                    int status = cur.getInt(cur.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    //Log.e("Touchbase", "♦♦♦♦URI : "+cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
                    String fileName = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    int colCount = cur.getColumnCount();
                    for(int i=0;i<colCount;i++) {
                        String columnName = cur.getColumnName(i);
                        String value = cur.getString(i);
                        Log.e("Touchbase", "♥♥♥♥Column Name : " + columnName+" - "+value);
                    }
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        //Log.i("FLAG", "done");
                        //Toast.makeText(mContext, "Download SUCCESSFULL!", Toast.LENGTH_SHORT).show();

                        String filenameArray[] = fileName.split("\\.");
                        String extension = filenameArray[filenameArray.length - 1];

                        String pdf = "pdf";
                        if (extension.equals(pdf)) {
                            Intent i = new Intent(mContext, PDFViewActivity.class);
                            i.putExtra("fileName", fileName);
                            i.putExtra("mode", mode);
                            i.putExtra("filePath", filePath);
                            Log.e("TouchBase", "♦♦♦♦File Path : " + filePath + " Mode : " + mode);
                            mContext.startActivity(i);
                        }
                    } else {
                        Toast.makeText(context, "Failed to download file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Touchbase", "♦♦♦♦No records found for download");
                    Toast.makeText(context, "Failed to download", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Unable to download file", Toast.LENGTH_SHORT).show();
            }
        }
    }
}