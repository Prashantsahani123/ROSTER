package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.DependentData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.holders.DependentHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DependentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<DependentData> list=new ArrayList<>();
    String type;

    public DependentAdapter(Context context, ArrayList<DependentData> list, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater  inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=null;
        if(type.equalsIgnoreCase(Constant.Dependent.MEMBER)){
            view = inflater.inflate(R.layout.added_member_list_item, parent, false);
        }else {
            view = inflater.inflate(R.layout.dependent_item, parent, false);
        }

        DependentHolder holder = new DependentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DependentData data=list.get(position);
        DependentHolder dependentHolder=(DependentHolder)holder;

        if(type.equalsIgnoreCase(Constant.Dependent.MEMBER)){
            dependentHolder.ll_mobile.setVisibility(View.GONE);
            dependentHolder.tvDescr.setText(data.getMemberName());
            //dependentHolder.tv_mobile.setText(data.getMemberDesignation());
            String pic=data.getPic();
            if(pic!=null && !pic.isEmpty()){
                Picasso.with(context).load(data.getPic())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.profile_pic)
                        .into(dependentHolder.imageView1);
            }
        }
        else if(type.equalsIgnoreCase(Constant.Dependent.ANNS)){

            if(data.getAnnsName()!=null && !data.getAnnsName().isEmpty()){
                dependentHolder.ll_item.setVisibility(View.VISIBLE);
                dependentHolder.txt_name.setText(data.getAnnsName());
            }

        }else if(type.equalsIgnoreCase(Constant.Dependent.ANNETS)){

            if(data.getAnnetsName()!=null && !data.getAnnetsName().isEmpty()){
                dependentHolder.ll_item.setVisibility(View.VISIBLE);
                dependentHolder.txt_name.setText(data.getAnnetsName());
            }

        }else if(type.equalsIgnoreCase(Constant.Dependent.VISITORS)){
            dependentHolder.txt_name.setText(data.getVisitorName());
            if(data.getBrought()!=null && !data.getBrought().isEmpty()){

                dependentHolder.txt_designation.setVisibility(View.VISIBLE);
                dependentHolder.txt_designation.setText(data.getBrought());
            }


        }else if(type.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){
//            if(data.getRotarianName()!=null && !data.getRotarianName().isEmpty()){
//                dependentHolder.ll_item.setVisibility(View.VISIBLE);
//                dependentHolder.txt_name.setText(data.getRotarianName()+" ( "+data.getRotarianID()+" ) ");
//                dependentHolder.txt_designation.setVisibility(View.VISIBLE);
//                dependentHolder.txt_designation.setText(data.getClubName());
//            }

            dependentHolder.txt_name.setText(data.getRotarianName());
            if(data.getRotarianID()!=null && !data.getRotarianID().isEmpty()){

                dependentHolder.txt_name.setText(data.getRotarianName()+" ( "+data.getRotarianID()+" ) ");

            }

            if(data.getClubName()!=null && !data.getClubName().isEmpty()){
                dependentHolder.txt_designation.setVisibility(View.VISIBLE);
                dependentHolder.txt_designation.setText(data.getClubName());
            }

        }else if(type.equalsIgnoreCase(Constant.Dependent.DELEGETS)){
            dependentHolder.txt_name.setText(data.getRotarianName());
            if(data.getDistrictDesignation()!=null && !data.getDistrictDesignation().isEmpty()){

                dependentHolder.txt_name.setText(data.getRotarianName()+" ( "+data.getDistrictDesignation()+" ) ");

            }

            if(data.getClubName()!=null && !data.getClubName().isEmpty()){
                dependentHolder.txt_designation.setVisibility(View.VISIBLE);
                dependentHolder.txt_designation.setText(data.getClubName());
            }


        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
