package com.NEWROW.row.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Data.AlbumData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 07-09-2016.
 */
public class AlbumAdapter extends BaseAdapter {

    Context context;
    ArrayList<AlbumData> listAlbum = new ArrayList<>();
    private static LayoutInflater inflater = null;
    String editflag; //0-edit grps . 1- normal listing
    String isdelete = "false";

    public AlbumAdapter(Context cnt, ArrayList<AlbumData> list, String edit) {
        context = cnt;
        listAlbum = list;
        editflag = edit;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listAlbum.size();
    }

    @Override
    public Object getItem(int position) {
        return listAlbum.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;

        if (convertView == null) {
            rowView = inflater.inflate(R.layout.layout_album_adapter, null);
            holder.img = (ImageView) rowView.findViewById(R.id.picture);
            holder.text = (TextView) rowView.findViewById(R.id.text);
            holder.framelayout = (FrameLayout) rowView.findViewById(R.id.framelayout);
            holder.imgview = (ImageView) rowView.findViewById(R.id.cb_select);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        if(editflag.equalsIgnoreCase("0")){
            holder.imgview.setVisibility(View.VISIBLE);
        }
        holder = (Holder) rowView.getTag();

        if (listAlbum.get(position).getImage().trim().length() == 0 || listAlbum.get(position).getImage() == null || listAlbum.get(position).getImage().isEmpty()) {
            holder.img.setImageResource(R.drawable.placeholder_new);
        } else {
            Picasso.with(context).load(listAlbum.get(position).getImage())
                    .placeholder(R.drawable.placeholder_new)
                    .into(holder.img);
                    //.resize(150, 150)
        }
        holder.text.setText(listAlbum.get(position).getTitle());

        holder.imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this project");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";
                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";
                        if (InternetConnection.checkConnection(context)){
                            String deletedAlbumId = listAlbum.get(position).getAlbumId();
                            deleteAlbum(deletedAlbumId);
                            listAlbum.remove(position);
                            notifyDataSetChanged();
                         //   Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
            }
        });
        return rowView;
    }
    public class Holder {

        ImageView img;
        TextView text;
        FrameLayout framelayout;
        ImageView imgview;
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



}
