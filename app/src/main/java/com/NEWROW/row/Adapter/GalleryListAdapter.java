package com.NEWROW.row.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Data.AlbumData;
import com.NEWROW.row.Gallery;
import com.NEWROW.row.GalleryDescription;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.BitmapTransform;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.holders.EmptyViewHolder;
import com.NEWROW.row.holders.GalleryListHolder;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 22-12-2016.
 */
public class GalleryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int EMPTY_VIEW = 1, NON_EMPTY_VIEW = 2;
    public static ArrayList<AlbumData> albumlist = new ArrayList<>();
    static Context context;
    String flag;
    String isdelete = "false";
    String type;

    static int pst ;

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    //this is added By me for delete title;
    static String deleteTitle;

    SimpleDateFormat oldSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat newSdf = new SimpleDateFormat("dd MMMM yyyy");

    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

    int detail = 0;

    public GalleryListAdapter (Context con, ArrayList<AlbumData> list, String edit, String type){
        this.context = con;
        this.albumlist = list;
        this.flag = edit;
        this.type = type;
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



        final GalleryListHolder hol = (GalleryListHolder) holder;



        if(flag.equals("0")){
            hol.iv_delete.setVisibility(View.VISIBLE);
            hol.iv_delete.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));
            hol.iv_delete.setEnabled(true);
         //   hol.ontimep.setVisibility(View.VISIBLE);


            try {
                String main_tlt = albumlist.get(position).getTtlOfNewOngoingProj();


                String subcount = albumlist.get(position).getSubCount();
                int subc_d = Integer.parseInt(subcount);

                if( main_tlt == null)
                {
                    subc_d = 0;
                }
                else if(main_tlt.isEmpty())
                {
                    subc_d = 0;
                }




                if( main_tlt == null || main_tlt.isEmpty() || main_tlt.equalsIgnoreCase("null"))
                {


                }


                else  if (!main_tlt.equalsIgnoreCase("") && subc_d > 0 ) {//subc_d > 1

                    hol.iv_delete.setEnabled(false);
                    hol.iv_delete.setVisibility(View.GONE);



                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



            detail = 0;




        }


        else if(flag.equals("2") || flag.equals("3")){



            if(flag.equals("2"))
            {
                hol.iv_delete.setVisibility(View.VISIBLE);
                hol.iv_delete.setEnabled(true);
                hol.iv_delete.setImageDrawable(context.getResources().getDrawable(R.drawable.stoc));


                try {
                String main_tlt = albumlist.get(position).getTtlOfNewOngoingProj();
                String subcount = albumlist.get(position).getSubCount();
                int subc = Integer.parseInt(subcount);

                if( main_tlt == null)
                {
                    subc = 0;
                }
                else if(main_tlt.isEmpty())
                {
                    subc = 0;
                }

                else  if (!main_tlt.equalsIgnoreCase("") && subc > 0  ) {



                       hol.iv_delete.setEnabled(false);
                       hol.iv_delete.setVisibility(View.GONE);




                           }

                }
                 catch (Exception e)
                 {
                       e.printStackTrace();
                 }



                detail = 0;
            }else if(flag.equals("3"))
            {
                hol.iv_delete.setVisibility(View.VISIBLE);
                hol.iv_delete.setImageDrawable(context.getResources().getDrawable(R.drawable.ctoss));

                hol.iv_delete.setEnabled(true);
                detail = 0;
            }


        }
        else
        {
            if (context instanceof Gallery) {
                ((Gallery) context).tv_title.getText();
                // moderator








                ////






                String tt = ((Gallery) context).tv_title.getText().toString();

                Log.i("titleee", tt);
                if(tt.equalsIgnoreCase("Service Projects")) {




                    String modert_status =   albumlist.get(position).getModerator_status();
                    Log.d("modert",modert_status);


//                    0-N/Ase
//                    1-Approved
//                    2-waiting for approval
//                    3-Onhold

                    try {


                        if (modert_status.equalsIgnoreCase("0")) {
                            hol.mdrt_text.setText("N/A");
                            if (PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN).equals("No")) {
                                hol.modert.setVisibility(View.GONE);

                            } else {
                             //   hol.modert.setVisibility(View.VISIBLE);   used

                            }



                        } else if (modert_status.equalsIgnoreCase("1")) {
                            hol.mdrt_text.setText("Approved");
                            if (PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN).equals("No")) {
                                hol.modert.setVisibility(View.GONE);

                            } else {
                              //  hol.modert.setVisibility(View.VISIBLE);   used

                            }

                        } else if (modert_status.equalsIgnoreCase("2")) {
                            hol.mdrt_text.setText("Waiting for approval");
                            if (PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN).equals("No")) {
                                hol.modert.setVisibility(View.GONE);

                            } else {
                             //   hol.modert.setVisibility(View.VISIBLE);     used

                            }
                        } else if (modert_status.equalsIgnoreCase("3")) {
                            hol.mdrt_text.setText("Onhold");
                            if (PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN).equals("No")) {
                                hol.modert.setVisibility(View.GONE);

                            } else {
                              //  hol.modert.setVisibility(View.VISIBLE);   used

                            }
                        }

                        if(Gallery.moderate_flag.equalsIgnoreCase("1"))
                        {
                          //  hol.modert.setVisibility(View.VISIBLE);   used
                        }
                        else
                        {
                            hol.modert.setVisibility(View.GONE);
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }




                   // hol.modert.setText(modert_status);






                    if (PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN).equals("No")) {
                        hol.ontimep.setVisibility(View.GONE);

                    } else {
                        hol.ontimep.setVisibility(View.VISIBLE);

                    }


                //  hol.ontimep.setVisibility(View.VISIBLE);

                  String flg =   albumlist.get(position).getOnetimeOrOngoing();
                    if(flg.equalsIgnoreCase("0"))
                    {
                        hol.ontimep.setText(" Onetime Project ");
                        hol.ontimep.setVisibility(View.INVISIBLE);
//                        hol.ontimep.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.nonclick));

                    }

                  else  if(flg.equalsIgnoreCase("1"))
                  {
                      hol.ontimep.setText(" Onetime Project ");
                      hol.ontimep.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.nonclick));

                  }
                  else
                  {
                      hol.ontimep.setText(" Ongoing/Repeat Project ");
                      hol.ontimep.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.nonclick));

                      //------
                      try {
                          String main_tlt = albumlist.get(position).getTtlOfNewOngoingProj();

                          if(main_tlt.isEmpty() || main_tlt == null)
                          {

                          }

                          else  if (!main_tlt.equalsIgnoreCase("")) {

                              //hol.iv_delete.setEnabled(false);


                              hol.ontimep.setText(" Ongoing/Repeat Main Project ");
                              hol.ontimep.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkround));

// pending.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_rounded));
                          }

                      }
                      catch (Exception e)
                      {
                          e.printStackTrace();
                      }

                      //--------
                  }

//                    hol.iv_delete.setVisibility(View.VISIBLE);
//                    hol.iv_delete.setImageDrawable(context.getResources().getDrawable(R.drawable.detail));
                    detail = 1;
                }
            }


        }



        Date date = null;

        try {
            date = oldSdf.parse(albumlist.get(position).getProject_date());
            hol.tv_date.setText(newSdf.format(date));
        } catch (ParseException e) {
            hol.tv_date.setText(albumlist.get(position).getProject_date());
            e.printStackTrace();
        }

//        if(albumlist.get(position).getDescription().equalsIgnoreCase("")) {
//           //hol.tv_tile.setPadding(0,30,0,0);
//            hol.tv_description.setVisibility(View.GONE);
//        }else{

        if(type.equals("1")){
            hol.tv_description.setText(albumlist.get(position).getDescription());
            hol.tv_tile.setText(albumlist.get(position).getTitle());

//            try {
//                String main_tlt = albumlist.get(position).getTtlOfNewOngoingProj();
//                if (!main_tlt.equalsIgnoreCase("")) {
//                    hol.tv_tile.setText(main_tlt);
//                }
//
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }


        }else{
            hol.tv_description.setText(albumlist.get(position).getTitle());
            hol.tv_tile.setText(albumlist.get(position).getClub_Name());

            try {
                String main_tlt = albumlist.get(position).getTtlOfNewOngoingProj();
                if (!main_tlt.equalsIgnoreCase("")) {
                   // hol.tv_tile.setText(main_tlt);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

      //  }
        if (albumlist.get(position).getImage() == null || albumlist.get(position).getImage().trim().length() == 0 || albumlist.get(position).getImage().isEmpty()) {
            hol.image.setImageResource(R.drawable.placeholder_new);
        } else {

            Picasso.with(context).load(albumlist.get(position).getImage())
                    .placeholder(R.drawable.placeholder_new)
                    .skipMemoryCache()
                    .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                    //.fit()
                    .into(hol.image);
        }


        hol.linear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, GalleryDescription.class);
//                i.putExtra("albumname", albumlist.get(position).getTitle());
//                i.putExtra("albumDescription", albumlist.get(position).getDescription());
//                i.putExtra("albumId", albumlist.get(position).getAlbumId());
//                i.putExtra("albumImage", albumlist.get(position).getImage());
                i.putExtra("albumData",albumlist.get(position));
               // i.putExtra("Financeyear",albumlist.get(position));
                //Financeyear


                i.putExtra("fromShowcase",type);
              //  Log.i("flg",   hol.ontimep.getText().toString().trim());

                Log.i("fromShowcasee",type);

                ((Activity)context).startActivityForResult(i, Gallery.UPDATE_ALBUM_REQEUST);
            }
        });


        hol.ontimep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              String text =   hol.ontimep.getText().toString();

              if(text.equalsIgnoreCase(" Onetime Project ") || text.equalsIgnoreCase(" Ongoing/Repeat Project "))
              {

              }
              else
              {
                  if (context instanceof Gallery) {
                      ((Gallery) context).getong_pro( albumlist.get(position).getAlbumId(),albumlist.get(position).getTitle());
                  }

              }


            }
        });


        hol.iv_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (detail == 1) {


                } else {


                    detail = 0;

                    final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_confrm_delete);
                    TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                    TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                    TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);

                    String groupType = PreferenceManager.getPreference(context, PreferenceManager.MY_CATEGORY, "1");

//
//                    Bitmap bitmap = ((BitmapDrawable) hol.iv_delete.getDrawable()).getBitmap();
//                    String bit = String.valueOf(bitmap);
//                    Toast.makeText(v.getContext(), bit, Toast.LENGTH_LONG).show();


                    if (groupType.equals("" + Constant.GROUP_CATEGORY_DT)) {
                        //  tv_line1.setText("Are you sure you want to delete this Album");

                        deleteTitle = method(Gallery.deletetitle);

                        tv_line1.setText("Are you sure you want to delete this " + deleteTitle);

                        if (flag.equalsIgnoreCase("2")) {
                            tv_line1.setText("Are you sure you want to  move this project to Club Meeting");
                        }
                        if (flag.equalsIgnoreCase("3")) {
                            tv_line1.setText("Are you sure you want to  move this project to Service Project");
                        }
                        if (detail == 1) {

                        }


                    } else {
                        deleteTitle = Gallery.deletetitle;

                        tv_line1.setText("Are you sure you want to delete this " + deleteTitle);
                        if (flag.equalsIgnoreCase("2")) {
                            tv_line1.setText("Are you sure you want to move this project to Club Meeting");

                        }

                        if (flag.equalsIgnoreCase("3")) {
                            tv_line1.setText("Are you sure you want to move this project to Service Project");
                        }
                        if (detail == 1) {

                        }

                    }

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
                            //((Gallery) context).delete_single(albumlist.get(position).getAlbumId(), "2",position);

                            if (InternetConnection.checkConnection(context)) {

                                if (flag.equalsIgnoreCase("2")) {
                                    if (context instanceof Gallery) {
                                       // ((Gallery) context).movetoclub(albumlist.get(position).getAlbumId(),"2");
                                        ((Gallery) context).movetestt(albumlist.get(position).getAlbumId(),"2");
                                    }
                                } else if (flag.equalsIgnoreCase("3")) {
                                    if (context instanceof Gallery) {
                                        ((Gallery) context).movetoclub(albumlist.get(position).getAlbumId(),"3");

                                    }

                                } else if (detail == 0) {
                                    String deletedAlbumId = albumlist.get(position).getAlbumId();

                                     String tt =   Gallery.tv_title.getText().toString().trim();
                                     if(tt.equalsIgnoreCase("Service Projects"))
                                     {

                                       if( ((Gallery) context).delete_single(albumlist.get(position).getAlbumId(), "2",position).equalsIgnoreCase(""));
                                         {
                                             notifyDataSetChanged();
                                         }
                                        notifyDataSetChanged();
                                     }
                                     else{


                                         String ttt =   Gallery.tv_title.getText().toString().trim();
                                         if(ttt.equalsIgnoreCase("Service Projects"))
                                         {
                                             ((Gallery) context).delete_single(albumlist.get(position).getAlbumId(), "2",position);
                                             notifyDataSetChanged();


                                         }
                                         else{
                                             Gallery.delt_flg = 0;

                                             deleteAlbum(deletedAlbumId,position);
                                             albumlist.remove(position);
                                             notifyDataSetChanged();

                                         }
                                         notifyDataSetChanged();
//
                                     }
                                    notifyDataSetChanged();

                               //     notifyDataSetChanged();
                                    Log.d("deletetype", String.valueOf(detail+ "fff"+flag));

                                } else if (flag.equalsIgnoreCase("1")) {

                                    String deletedAlbumId = albumlist.get(position).getAlbumId();

                                    String tt =   Gallery.tv_title.getText().toString().trim();
                                    if(tt.equalsIgnoreCase("Service Projects"))
                                    {
                                        ((Gallery) context).delete_single(albumlist.get(position).getAlbumId(), "2",position);
                                        notifyDataSetChanged();



                                    }
                                    else {
                                        Gallery.delt_flg = 0;

                                        deleteAlbum(deletedAlbumId,position);
                                        albumlist.remove(position);
                                        notifyDataSetChanged();
                                    }
                                    notifyDataSetChanged();
                                    Log.d("deletetype", String.valueOf(detail+ "fff"+flag));
                                }
                                notifyDataSetChanged();

//                            String deletedAlbumId = albumlist.get(position).getAlbumId();
//                            deleteAlbum(deletedAlbumId);
//                            albumlist.remove(position);
//                            notifyDataSetChanged();
//

                            } else {
                                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                            }
                            notifyDataSetChanged();
                        }

                    });
                    notifyDataSetChanged();


                    if (flag.equalsIgnoreCase("2")) {



                    }
                    notifyDataSetChanged();

                    dialog.show();
                }
                notifyDataSetChanged();

            }

            //////////////


        });
        //notifyDataSetChanged();
        //notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return albumlist.size();
    }

    public static void deleteAlbum(String albumId, int position){

        Log.e("Touchbase", "------ deleteAlbum() is called");

        String url = Constant.DeleteAlbum;
        pst = position;

        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID",albumId));
        arrayList.add(new BasicNameValuePair("type","Gallery"));
        arrayList.add(new BasicNameValuePair("profileID", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID)));

        arrayList.add(new BasicNameValuePair("Financeyear",Gallery.year));

        //Financeyear
        DeleteAlbumAsyncTask task = new DeleteAlbumAsyncTask(url, arrayList,context);
        task.execute();

        Log.d("Requestttt", "PARAMETERS " + Constant.DeleteAlbum + " :- " + arrayList.toString());
    }

    public static class DeleteAlbumAsyncTask extends AsyncTask<String, Object, Object> {

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

                getresult(result.toString(),pst);

                Log.d("Response", "calling DeleteAlbum");



            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    private static void getresult(String val, int pst) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("DeleteResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");
                Toast.makeText(context, deleteTitle+" deleted successfully.", Toast.LENGTH_SHORT).show();
                String groupType = PreferenceManager.getPreference(context, PreferenceManager.MY_CATEGORY, "1");
                if ( groupType.equals(""+Constant.GROUP_CATEGORY_DT)) {




               //     ((Activity)context).startActivity(i);


                    Toast.makeText(context, deleteTitle+" deleted successfully.", Toast.LENGTH_SHORT).show();
                    String tt =   Gallery.tv_title.getText().toString().trim();

                    //Gallery.getAlbumList();
                    if(tt.equalsIgnoreCase("Service Projects"))
                    {

                        albumlist.remove(pst);

                        albumlist.notifyAll();


                    }
                } else {

                   // Intent i = new Intent(context, Gallery.class);
//
                  //  ((Activity)context).startActivity(i);

                    Toast.makeText(context,deleteTitle+ " deleted successfully.", Toast.LENGTH_SHORT).show();
                    String tt =   Gallery.tv_title.getText().toString().trim();
                    if(tt.equalsIgnoreCase("Service Projects"))
                    {


                        albumlist.remove(pst);
                        albumlist.notifyAll();




                    }
                }

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }
    private static void getresultservice(String val, int pst) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("DeleteResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");

                String groupType = PreferenceManager.getPreference(context, PreferenceManager.MY_CATEGORY, "1");
                if ( groupType.equals(""+Constant.GROUP_CATEGORY_DT)) {

                    albumlist.remove(pst);
                    //     ((Activity)context).startActivity(i);


                    Toast.makeText(context, deleteTitle+" deleted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    albumlist.remove(pst);
                    // Intent i = new Intent(context, Gallery.class);
//
                    //  ((Activity)context).startActivity(i);

                    Toast.makeText(context,deleteTitle+ " deleted successfully.", Toast.LENGTH_SHORT).show();
                }

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

    //this is added By Gaurav for Last String Remove 's"
    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 's') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
