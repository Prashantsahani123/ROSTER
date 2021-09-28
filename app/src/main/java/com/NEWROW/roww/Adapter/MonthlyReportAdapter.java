package com.NEWROW.row.Adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Data.MonthlyReportData;
import com.NEWROW.row.ImageZoom;
import com.NEWROW.row.MonthlyReportActivity;
import com.NEWROW.row.PDFViewActivity;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.MarshMallowPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MonthlyReportAdapter extends RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder> {

    private Context context;
    private List<MonthlyReportData> allItems;
    private List<MonthlyReportData> filterList;
    private String flag; //submitted or not submitted
    String title = "";
    private String mode;
    private long enqueId;
    MonthlyReportActivity activity;
    private ProgressDialog progress;
    private  FileDownloadReceiver receiver;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView iv_view,iv_download;
        public final TextView tv_ClubTitle,tv_title;
        public final TextView tv_submittedDate;
        public final TextView tv_submittedTime;
        public final TextView tv_divider,tv_ClubAG;
        public final LinearLayout layout_not_submitted,layout_submitted;

        public ViewHolder(View view) {
            super(view);

            this.mView = view;

            this.tv_ClubTitle = (TextView) view.findViewById(R.id.tv_ClubTitle);
            this.tv_title = (TextView) view.findViewById(R.id.tv_title);
            this.tv_submittedDate = (TextView) view.findViewById(R.id.tv_submittedDate);
            this.tv_submittedTime = (TextView) view.findViewById(R.id.tv_submittedTime);
            this.tv_divider = (TextView) view.findViewById(R.id.tv_divider);
            this.tv_ClubAG = (TextView) view.findViewById(R.id.tv_ClubAG);
            this.iv_download = (ImageView) view.findViewById(R.id.iv_download);
            this.iv_view = (ImageView) view.findViewById(R.id.iv_view);
            layout_submitted = (LinearLayout) view.findViewById(R.id.layout_submitted);
            layout_not_submitted = (LinearLayout) view.findViewById(R.id.layout_not_submitted);

            /*this.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                }
            });*/
        }
    }

    public MonthlyReportAdapter(Context context, List<MonthlyReportData> items, String flag, MonthlyReportActivity activity) {

        allItems = items;
        this.context = context;
        filterList = new ArrayList<>();
        filterList.addAll(items);
        this.flag=flag;
        this.activity = activity;

        receiver = new FileDownloadReceiver();

        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.monthly_list_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        bindReportViewHolder(holder, position);
    }

    public void bindReportViewHolder(ViewHolder holder, int position){

        final MonthlyReportData item = allItems.get(position);

        String title = item.getName();

        final String date = item.getDate();

        String time = item.getTime();

        holder.tv_ClubTitle.setText(title);
        holder.tv_submittedDate.setText(date);
        holder.tv_title.setText(title);

        holder.tv_submittedTime.setText(time);

        String ag = item.getClubAG();

        if(ag.equalsIgnoreCase("")){
            holder.tv_ClubAG.setText("");
        } else {
            holder.tv_ClubAG.setText("AG : " + item.getClubAG());
        }

//        holder.tv_ClubAG.setText("AG : " + item.getClubAG());

        if(date.equalsIgnoreCase("") || time.equalsIgnoreCase("")){
            holder.tv_divider.setVisibility(View.GONE);
        } else {
            holder.tv_divider.setVisibility(View.VISIBLE);
        }

        if(flag.equalsIgnoreCase("1")){
            holder.iv_view.setVisibility(View.VISIBLE);
            holder.iv_download.setVisibility(View.VISIBLE);
            holder.layout_submitted.setVisibility(View.VISIBLE);
            holder.layout_not_submitted.setVisibility(View.GONE);
        }else{
            holder.layout_submitted.setVisibility(View.GONE);
            holder.layout_not_submitted.setVisibility(View.VISIBLE);
        }

        holder.iv_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mode = Constant.MODE_VIEW;

               //item.setReportURL("http://webtest.rosteronwheels.com/Documents/documentsafe/Group2765/ROW_23072018055022PM.pdf");

                viewDocument(item);

                /*String link = item.getReportURL();

                if (link.equalsIgnoreCase("")) {
                    Toast.makeText(context, "Link not found.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

                final PackageManager pm = context.getPackageManager();

                if (pm.resolveActivity(browserIntent, 0) != null) {
                    context.startActivity(browserIntent);
                }else{
                    Toast.makeText(context,"Unable to open",Toast.LENGTH_SHORT).show();
                }*/
            }

        });


        holder.iv_download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mode = Constant.MODE_DOWNLOAD;
              //  item.setReportURL("http://webtest.rosteronwheels.com/Documents/documentsafe/Group2765/ROW_23072018055022PM.pdf");
                downloadDocument(item);
            }
        });


    }

    public int getItemCount() {
        return this.allItems.size();
    }

    // Filter Class
    public void filter(String charText) {

        charText = charText.toLowerCase();

        allItems.clear();

        if (charText.length() == 0) {

            allItems.addAll(filterList);

        } else {

            for (MonthlyReportData wp : filterList) {

                if (wp.getName().toLowerCase().contains(charText)) {
                    allItems.add(wp);
                }
            }

        }

        if(allItems.size()==0) {
            activity.noRecordFound(View.VISIBLE);
        }else{
            activity.noRecordFound(View.GONE);
        }

        notifyDataSetChanged();
    }

    private void viewDocument(MonthlyReportData data) {

        title = data.getName();

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

        if (data.getReportURL().equals("")) {
            Toast.makeText(context, "Download link not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        progress = ProgressDialog.show(context, "Loading", "Loading file", true);

        final String link = data.getReportURL();

        Uri u = Uri.parse(link);

        File f = new File("" + u);

        String fileName = f.getName();

//        String filePath = f.getPath();

        Log.d("-----FILE NAME---------", "satish----Downloaded-----" + fileName);

        File myDirectory = new File(Environment.DIRECTORY_DOWNLOADS);

        String path = myDirectory.getAbsolutePath();

        Log.e("==Directory PaTH===", "=satsih =>" + path);

        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }

        DownloadManager downloadManager;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(link);
        // As file is taped for open purpose only, saving it in app directory. So that it will not be visible in file explorer of the system
        DownloadManager.Request request = new DownloadManager.Request(uri);

        enqueId = downloadManager.enqueue(request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle(fileName)
                        .setDescription("File Downloading")
                        //.setDestinationUri(Uri.fromFile(downloadFile)));
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName));

//        Log.e("TouchBase", "♦♦♦♦Enque ID : " + enqueId);

    }

    private void downloadDocument(MonthlyReportData data) {

//        String docId = data.getId();

        title = data.getName();

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

        if (data.getReportURL().equals("")) {
            Toast.makeText(context, "Download Link not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        final String link = data.getReportURL();
        Uri u = Uri.parse(link);
        File f = new File("" + u);

        String fileName = f.getName();

        Log.e("monthly","satish url "+data.getReportURL()+" filename=> "+fileName);

        if (fileExists(fileName)) {

            Toast.makeText(context, "File is already downloaded", Toast.LENGTH_LONG).show();


           /* File sdcard = Environment.getExternalStorageDirectory();
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
            }*/

        } else {
            String filePath = f.getPath();
            Log.d("-----FILE NAME---------", "-----Downloaded-----" + fileName);
            File myDirectory = new File(Environment.DIRECTORY_DOWNLOADS);
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
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName));

//            Log.e("TouchBase", "♦♦♦♦Enque ID : " + enqueId);
        }
    }


    // Checking whether the file which is to be downloaded is already there in filesystem.
    public boolean fileExists(String fileName) {
        File sdcard = Environment.getExternalStorageDirectory();
        File myfile = new File(sdcard.getPath() + "/Touchbase", fileName);
        return myfile.exists();
    }

    public void unregisterFileDownloadReceiver() {
        context.unregisterReceiver(receiver);
    }

    public class FileDownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            if (progress != null) progress.dismiss();

            String action = intent.getAction();

            try {
                Log.e("Touchbase", "♦♦♦♦Mode : " + mode);

                if (mode.equals(Constant.MODE_DOWNLOAD)) {
                    Toast.makeText(context, "File downloaded successfully", Toast.LENGTH_LONG).show();
                    return;
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

                    //  Log.e("Touchbase", "♦♦♦♦satish  URI : "+cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));

                   // String fileName = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME)); //COLUMN_LOCAL_FILENAME is deprecated; use ContentResolver.openFileDescriptor() instead on 7.0
                   // Log.e("Touchbase", "♦♦♦♦satish filename : "+cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME)));

                    String str = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    String fileName = "";//str.replace("file://","");

                    if(str!=null && !str.equalsIgnoreCase("")){
                        fileName = str.replace("file://","");
                    }

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
                          //  i.putExtra("mom_gaurav", "0");


                            Log.e("TouchBase", "pdf File Path : " + fileName + " Mode : " + mode);

                            context.startActivity(i);

                        } else if (mediaType.startsWith("image")) {

                            Intent imageIntent = new Intent(context, ImageZoom.class);
                            imageIntent.putExtra("imgageurl", Uri.fromFile(new File(fileName)).toString());
                            imageIntent.putExtra("mode", mode);
                            imageIntent.putExtra("title", title);
                            context.startActivity(imageIntent);

                        } else {

                            if(mode.equals(Constant.MODE_VIEW)) {
                                new File(fileName).delete();
                                Toast.makeText(context, "Not able to open this file to view", Toast.LENGTH_LONG).show();
                            }
                        }

                    } else {

                        if (mode.equals(Constant.MODE_DOWNLOAD)) {
                            Toast.makeText(context, "Failed to download file", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Failed to open file", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Log.e("Touchbase", "♦♦♦♦No records found for download");
                    //Toast.makeText(context, "Failed to download", Toast.LENGTH_LONG).show();
                }
                //DownloadManager.COLUMN_LOCAL_FILENAME
            } else {
                Toast.makeText(context, "Unable to download file", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
