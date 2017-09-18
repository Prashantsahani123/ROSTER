package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Data.AlbumData;
import com.SampleApp.row.Gallery;
import com.SampleApp.row.GalleryDescription;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.holders.EmptyViewHolder;
import com.SampleApp.row.holders.GalleryListHolder;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 22-12-2016.
 */
public class GalleryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int EMPTY_VIEW = 1, NON_EMPTY_VIEW = 2;
    public ArrayList<AlbumData> albumlist;
    Context context;
    String flag;
    String isdelete = "false";

    public GalleryListAdapter (Context con,ArrayList<AlbumData> list,String edit){
        this.context = con;
        this.albumlist = list;
        this.flag = edit;
    }

    @Override
    public int getItemViewType(int position) {
        if (albumlist.get(position).getAlbumId().equals("-1")) {
            return EMPTY_VIEW;
        }
        return NON_EMPTY_VIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view,parent,false);
            return new EmptyViewHolder(v);
        } else if ( viewType == NON_EMPTY_VIEW ) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_listview_holder,parent,false);
            GalleryListHolder holder = new GalleryListHolder(v);
            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( getItemViewType(position) == EMPTY_VIEW) bindEmptyView(holder, position);
        else bindNonEmptyView(holder, position);
    }

    public void bindEmptyView(RecyclerView.ViewHolder holder, final int position) {
        ((EmptyViewHolder) holder).getEmptyView().setText("No albums found");
    }

    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {
        GalleryListHolder hol = (GalleryListHolder) holder;
        if(flag.equals("0")){
            hol.iv_delete.setVisibility(View.VISIBLE);
        }

//        if(albumlist.get(position).getDescription().equalsIgnoreCase("")) {
//           //hol.tv_tile.setPadding(0,30,0,0);
//            hol.tv_description.setVisibility(View.GONE);
//        }else{

            hol.tv_description.setText(albumlist.get(position).getDescription());
      //  }
        if (albumlist.get(position).getImage().trim().length() == 0 || albumlist.get(position).getImage() == null || albumlist.get(position).getImage().isEmpty()) {
            hol.image.setImageResource(R.drawable.dashboardplaceholder);
        } else {
            Picasso.with(context).load(albumlist.get(position).getImage())
                    .placeholder(R.drawable.dashboardplaceholder)
                    .into(hol.image);
        }

        hol.tv_tile.setText(albumlist.get(position).getTitle());
        hol.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, GalleryDescription.class);
                i.putExtra("albumname", albumlist.get(position).getTitle());
                i.putExtra("albumDescription", albumlist.get(position).getDescription());
                i.putExtra("albumId", albumlist.get(position).getAlbumId());
                i.putExtra("albumImage", albumlist.get(position).getImage());
                ((Activity)context).startActivityForResult(i, Gallery.UPDATE_ALBUM_REQEUST);
            }
        });
        hol.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Album");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";
                        if (InternetConnection.checkConnection(context)){
                            String deletedAlbumId = albumlist.get(position).getAlbumId();
                            deleteAlbum(deletedAlbumId);
                            albumlist.remove(position);
                            notifyDataSetChanged();

                        }
                        else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumlist.size();
    }

    public void deleteAlbum(String albumId){
        Log.e("Touchbase", "------ deleteAlbum() is called");
        String url = Constant.DeleteAlbum;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID",albumId));
        arrayList.add(new BasicNameValuePair("type","Gallery"));
        arrayList.add(new BasicNameValuePair("profileID", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID)));

        DeleteAlbumAsyncTask task = new DeleteAlbumAsyncTask(url, arrayList,context);
        task.execute();

        Log.d("Request", "PARAMETERS " + Constant.DeleteAlbum + " :- " + arrayList.toString());
    }

    public class DeleteAlbumAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
        Context con = null;
        String url = null;
        List<NameValuePair> argList = null;


        public DeleteAlbumAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            con = ctx;
        }



        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
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
                Log.d("Response", "calling DeleteAlbum");



            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("DeleteResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");
                Toast.makeText(context, "Album deleted successfully.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public void setEmptyView(View v) {

    }
}
