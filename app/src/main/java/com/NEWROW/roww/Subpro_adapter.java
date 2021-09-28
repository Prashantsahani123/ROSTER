package com.NEWROW.row;
//RecyclerView.Adapter<ferladapter.Myholder>
// RecyclerView.Adapter<RecyclerView.ViewHolder>


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.NEWROW.row.Data.AlbumData;
import com.NEWROW.row.Utils.MarshMallowPermission;

import java.util.ArrayList;
import java.util.List;

public class Subpro_adapter extends RecyclerView.Adapter<Subpro_adapter.Myholder> implements Filterable {


        ArrayList<Subpro_model> dataModelArrayList;
        private Object Knowldgbasemodel;
        Context context;
        MarshMallowPermission marshMallowPermission;

        public Subpro_adapter(Context cont, List<Subpro_model> dataModelArrayList) {
        this.context = cont;
        this.dataModelArrayList = (ArrayList<Subpro_model>) dataModelArrayList;
      //  marshMallowPermission = new MarshMallowPermission((Activity) context);

        }

@Override
public Filter getFilter() {
        return null;
        }


class Myholder extends RecyclerView.ViewHolder{


    TextView title,datee,tv2,tv3;
    ImageView img1, img2 ;

    public Myholder(View itemView)
    {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.titles);
        datee = (TextView) itemView.findViewById(R.id.descriptions);
        //img1 = (ImageView) itemView.findViewById(R.id.img1);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo == null){

                   //i.putExtra("albumData",albumlist.get(position));
                    Toast.makeText(context.getApplicationContext(),"No internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlbumData albumlist = new AlbumData();
                    Subpro_model subpro = new Subpro_model();

                    Intent intentt = new Intent(context, GalleryDescription.class);
                    int p = getLayoutPosition();

                 //   i.putExtra("fromShowcase",type);
                    intentt.putExtra("fromShowcase","1");

                    AlbumData aldata = new AlbumData();
                    String grpid  =   dataModelArrayList.get(p).getFk_group_master_id();
                    String ddt =  dataModelArrayList.get(p).getPk_gallery_id();
                    intentt.putExtra("grpid",dataModelArrayList.get(p).getFk_group_master_id());
                    intentt.putExtra("albumData",dataModelArrayList.get(p).getPk_gallery_id());
                    intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentt);

//                    int p = getLayoutPosition();
//                    System.out.println("LongClick: " + p);
//                    String pk_rfl_id = dataModelArrayList.get(p).getPk_referrals_id();
//                    Intent intentt = new Intent(context, Referallist.class);
//                    intentt.putExtra("pk_id", pk_rfl_id);
//                    context.startActivity(intentt);
                }


            }
        });


    }
}

    @NonNull
    @Override
    public Subpro_adapter.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subpro_list,null);

        return new Subpro_adapter.Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Subpro_adapter.Myholder holder, int position) {

        Subpro_model dataModel=dataModelArrayList.get(position);
        holder.title.setText(dataModel.getAlbum_title());
        holder.datee.setText(dataModel.getDate_of_project());

        //descriptions
//         holder.img1.setText(dataModel.getPublishDateTime());
//        holder.tv1.setText(dataModel.getPostedDate());
//        holder.img2.setText(dataModel.getEbulletinType());
//        holder.tv2.setText(dataModel.getLocation());
//          holder.tv3.setText(dataModel.getPostedDate());

    }
    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }
    public void filterList(ArrayList<Subpro_model> filteredList) {
        dataModelArrayList = filteredList;

//        if(filteredList.size() == 0)
//        {
//            Knowledgebase.Recylerv.setVisibility(View.GONE);
//            Knowledgebase.rd.setVisibility(View.VISIBLE);
//        }
//        else {
//            Knowledgebase.Recylerv.setVisibility(View.VISIBLE);
//            Knowledgebase.rd.setVisibility(View.GONE);
//            notifyDataSetChanged();
//        }

    }
}
