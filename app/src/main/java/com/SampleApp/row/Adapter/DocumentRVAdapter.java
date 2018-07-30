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
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Data.DocumentListData;
import com.SampleApp.row.ImageZoom;
import com.SampleApp.row.PDFViewActivity;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.holders.DocumentListItemHolder;
import com.SampleApp.row.holders.EmptyViewHolder;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by USER1 on 16-03-2017.
 */
public class DocumentRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    FileDownloadReceiver receiver;
    int count = 0;
    String mode;
    ProgressDialog progress;
    private static int DOCUMENT_TYPE = 1, EMPTY_TYPE = 0;
    public static int count_read_documents = 0;
    long enqueId;
    Context context;
    ArrayList<DocumentListData> list;
    String title = "";

    public DocumentRVAdapter(Context context, ArrayList<DocumentListData> list) {
        this.context = context;
        this.list = list;
        receiver = new FileDownloadReceiver();

        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getDocID().equals("-1")) {
            return EMPTY_TYPE;
        } else {
            return DOCUMENT_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == DOCUMENT_TYPE) {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.document_list_item, parent, false);
            DocumentListItemHolder holder = new DocumentListItemHolder(view);
            return holder;
        } else {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_view, parent, false);
            EmptyViewHolder holder = new EmptyViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == DOCUMENT_TYPE) {
            bindDocumentViewHolder(holder, position);
        } else if (type == EMPTY_TYPE) {
            bindEmptyViewHolder(holder, position);
        }
    }

    public void bindDocumentViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DocumentListData data = list.get(position);

        DocumentListItemHolder docHolder = (DocumentListItemHolder) holder;

        String isAdmin = PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN);

        if (isAdmin.equals("No")) {  // 1 means user is Admin
            docHolder.getIvDelete().setVisibility(View.GONE);

        } else {
            docHolder.getIvDelete().setVisibility(View.VISIBLE);
            docHolder.getIvDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDoc(data, position);
                }
            });
        }

        if (data.getAccessType().equals("0")) {  // access type view
            docHolder.getIvView().setVisibility(View.VISIBLE);
            docHolder.getIvDownload().setVisibility(View.GONE);
            docHolder.getIvView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mode = Constant.MODE_VIEW;
                    count = 0;
                    viewDocument(data);
                }
            });
            docHolder.getIvDownload().setVisibility(View.GONE);
        } else if (data.getAccessType().equals("1")) { // access type download
            docHolder.getIvView().setVisibility(View.GONE);
            docHolder.getIvDownload().setVisibility(View.VISIBLE);
            docHolder.getIvDownload().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mode = Constant.MODE_DOWNLOAD;
                    count = 0;
                    downloadDocument(data);
                }
            });
        }
        docHolder.getTvDate().setText(data.getCreateDateTime());
        docHolder.getTvTitle().setText(data.getDocTitle());
        if (data.getIsRead().equals("0") && data.getFilterType().equals(Constant.FILTER_TYPE_PUBLISHED)) {
            docHolder.getTvTitle().setTextColor(context.getResources().getColor(R.color.bluecolor));
        } else {
            docHolder.getTvTitle().setTextColor(context.getResources().getColor(R.color.black));
        }
    }


    public void viewDocument(DocumentListData data) {

        String docID = data.getDocID();
        title = data.getDocTitle();
        updateReadFlag(docID);

        if (data.getIsRead().equals("0")) {
            count_read_documents++;
        }

        MarshMallowPermission marshMallowPermission = new MarshMallowPermission((Activity) context);
        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage();
            return;
        }
        // selectImage();
        if (!InternetConnection.checkConnection(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        if (data.getDocURL().equals("")) {
            Toast.makeText(context, "Download Link not found.", Toast.LENGTH_SHORT).show();
            return;
        }


        progress = ProgressDialog.show(context, "Loading", "Loading file", true);

        final String link = data.getDocURL();
        Uri u = Uri.parse(link);
        File f = new File("" + u);
        String fileName = f.getName();
        String filePath = f.getPath();
        Log.d("-----FILE NAME---------", "-----Downloaded-----" + fileName);

        File myDirectory = new File(Environment.getExternalStorageDirectory(), "/Touchbase");
        String path = myDirectory.getAbsolutePath();
        Log.e("===Directory PaTH===", "=====" + path);

        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }
        DownloadManager downloadManager;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(link);
        // As file is taped for open purpose only, saving it in app directory. So that it will not be visible in file explorer of the system
        DownloadManager.Request request = new DownloadManager.Request(uri);
        enqueId = downloadManager.enqueue(
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle(fileName)
                        .setDescription("File Downloading")
                        //.setDestinationUri(Uri.fromFile(downloadFile)));
                        .setDestinationInExternalPublicDir("/Touchbase", fileName));
        Log.e("TouchBase", "♦♦♦♦Enque ID : " + enqueId);
    }

    public void downloadDocument(DocumentListData data) {
        String docId = data.getDocID();
        updateReadFlag(docId);
        if (data.getIsRead().equals("0")) {
            count_read_documents++;
        }
        title = data.getDocTitle();

        MarshMallowPermission marshMallowPermission = new MarshMallowPermission((Activity) context);
        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage();
            return;
        }
        // selectImage();
        if (!InternetConnection.checkConnection(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        if (data.getDocURL().equals("")) {
            Toast.makeText(context, "Download Link not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        final String link = data.getDocURL();
        Uri u = Uri.parse(link);
        File f = new File("" + u);
        String fileName = f.getName();

        if (fileExists(fileName)) {
            Toast.makeText(context, "File is already downloaded", Toast.LENGTH_LONG).show();
            File sdcard = Environment.getExternalStorageDirectory();
            File myfile = new File(sdcard.getPath() + "/Touchbase", fileName);

            String pdf = "pdf";
            if (fileName.endsWith(pdf)) {
                Intent i = new Intent(context, PDFViewActivity.class);
                i.putExtra("fileName", myfile.getPath());
                i.putExtra("mode", Constant.MODE_DOWNLOAD);
                i.putExtra("filePath", myfile.getPath());
                i.putExtra("title", title);
                Log.e("TouchBase", "♦♦♦♦File Path : " + myfile.getPath() + " Mode : Download");
                context.startActivity(i);
            } else if (myfile.getName().endsWith("jpg") || myfile.getName().endsWith("png")) {
                Intent imageIntent = new Intent(context, ImageZoom.class);

                imageIntent.putExtra("imgageurl", Uri.fromFile(myfile).toString());
                imageIntent.putExtra("mode", mode);
                imageIntent.putExtra("title", title);
                context.startActivity(imageIntent);
            }
        } else {
            String filePath = f.getPath();
            Log.d("-----FILE NAME---------", "-----Downloaded-----" + fileName);

            File myDirectory = new File(Environment.getExternalStorageDirectory(), "/Touchbase");
            String path = myDirectory.getAbsolutePath();
            Log.e("===Directory PaTH===", "=====" + path);

            if (!myDirectory.exists()) {
                myDirectory.mkdirs();
            }
            DownloadManager downloadManager;
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(link);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            progress = ProgressDialog.show(context, "Downloading", "Downloading file", true);
            enqueId = downloadManager.enqueue(
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(false).setTitle(fileName)
                            .setDescription("File Downloading")
                            .setDestinationInExternalPublicDir("/Touchbase", fileName));
            Log.e("TouchBase", "♦♦♦♦Enque ID : " + enqueId);
        }
    }

    public void bindEmptyViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((EmptyViewHolder) holder).getEmptyView().setText("No documents found");
    }

    public void deleteDoc(final DocumentListData data, final int position) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
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

                if (InternetConnection.checkConnection(context)) {
                    //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
                    processDocDelete(data.getDocID(), position);
                    dialog.dismiss();
                } else {
                    Utils.showToastWithTitleAndContext(context, "No Internet Connection!");
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void processDocDelete(String docId, final int position) {

        Hashtable<String, String> params = new Hashtable<>();
        params.put("typeID", docId);
        params.put("type", "Document");
        params.put("profileID", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID));

        try {

            final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            //new WebConnectionEventDetail(Constant.DeleteByModuleName, arrayList, mContext).execute();
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.DeleteByModuleName,
                    requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            JSONObject deleteResult = null;
                            try {
                                deleteResult = response.getJSONObject("DeleteResult");
                                final String status = deleteResult.getString("status");
                                if (status.equals("0")) {
                                    list.remove(position);
                                    if (list.size() == 0) {
                                        list.add(new DocumentListData("-1", "", "", "", "", "", "", ""));
                                    }
                                    Toast.makeText(context, "Deleted successfully", Toast.LENGTH_LONG).show();
                                    count_read_documents++;
                                    notifyDataSetChanged();
                                } else {
                                    Utils.showToastWithTitleAndContext(context, "Failed to DELETE, please Try Again!");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.e("Touchbase", "♦Error : " + error);
                            error.printStackTrace();
                            Toast.makeText(context, "Failed to delete document. Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );

            AppController.getInstance().addToRequestQueue(context, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // Checking whether the file which is to be downloaded is already there in filesystem.
    public boolean fileExists(String fileName) {
        File sdcard = Environment.getExternalStorageDirectory();
        File myfile = new File(sdcard.getPath() + "/Touchbase", fileName);
        return myfile.exists();
    }

    public void updateReadFlag(String docID) {
        Hashtable<String, String> params = new Hashtable<>();
        params.put("docID", docID);
        params.put("memberProfileID", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID));

        Gson gson = new Gson();
        JSONObject requestData = null;
        try {
            requestData = new JSONObject(gson.toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.UpdateDocumentIsRead,
                    requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Touchbase", "♦Response : " + response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            AppController.getInstance().addToRequestQueue(context, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //AppController.getInstance().addToRequestQueue(context,);
    }


    public void unregisterFileDownloadReceiver() {
        context.unregisterReceiver(receiver);
    }

    public class FileDownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (progress != null) progress.dismiss();
            if (count > 0) return;

            count++;
            String action = intent.getAction();

            try {
                Log.e("Touchbase", "♦♦♦♦Mode : " + mode);
                if (mode.equals(Constant.MODE_DOWNLOAD)) {
                    Toast.makeText(context, "File downloaded successfully", Toast.LENGTH_LONG).show();

                }
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
            Log.e("TouchBase", "♦♦♦♦Inside receiver");
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueId);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor cur = downloadManager.query(query);
                if (cur.moveToNext()) {
                    int status = cur.getInt(cur.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    //Log.e("Touchbase", "♦♦♦♦URI : "+cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
                    String fileName = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    String mediaType = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                    /*int colCount = cur.getColumnCount();
                    for(int i=0;i<colCount;i++) {
                        String columnName = cur.getColumnName(i);
                        String value = cur.getString(i);
                        Log.e("Touchbase", "♥♥♥♥Column Name : " + columnName+" - "+value);
                    }*/
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        //Log.i("FLAG", "done");
                        //Toast.makeText(mContext, "Download SUCCESSFULL!", Toast.LENGTH_SHORT).show();

                        String filenameArray[] = fileName.split("\\.");
                        String extension = filenameArray[filenameArray.length - 1];

                        String pdf = "pdf";
                        if (extension.equals(pdf)) {
                            Intent i = new Intent(context, PDFViewActivity.class);
                            i.putExtra("fileName", fileName);
                            i.putExtra("mode", mode);
                            i.putExtra("title", title);
                            Log.e("TouchBase", "♦♦♦♦File Path : " + fileName + " Mode : " + mode);
                            context.startActivity(i);
                        } else if (mediaType.startsWith("image")) {
                            Intent imageIntent = new Intent(context, ImageZoom.class);

                            imageIntent.putExtra("imgageurl", Uri.fromFile(new File(fileName)).toString());
                            imageIntent.putExtra("mode", mode);
                            imageIntent.putExtra("title", title);
                            context.startActivity(imageIntent);
                        } else {
                            if (mode.equals(Constant.MODE_VIEW)) {
                                new File(fileName).delete();
                                Toast.makeText(context, "Not able to open this file to view", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(context, "Failed to download file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Touchbase", "♦♦♦♦No records found for download");
                    Toast.makeText(context, "Failed to download", Toast.LENGTH_LONG).show();
                }
                //DownloadManager.COLUMN_LOCAL_FILENAME
            } else {
                Toast.makeText(context, "Unable to download file", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
